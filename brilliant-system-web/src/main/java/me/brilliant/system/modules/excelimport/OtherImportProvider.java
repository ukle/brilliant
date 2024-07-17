package me.brilliant.system.modules.excelimport;

import cn.hutool.core.date.DateTime;
import com.alibaba.excel.EasyExcel;
import me.brilliant.boot.web.exception.ResultException;
import me.brilliant.system.constant.ErrorCode;
import me.brilliant.system.modules.excelimport.constant.ImportType;
import me.brilliant.system.modules.excelimport.dto.ImportMatchResult;
import me.brilliant.system.modules.excelimport.dto.OtherExcelDto;
import me.brilliant.system.modules.excelimport.match.OtherMatchable;
import me.brilliant.system.modules.excelimport.repository.AlumnusArchiveRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.poi.ss.usermodel.CellType.STRING;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
@Component
public class OtherImportProvider extends AbstractImportProvider<OtherExcelDto> {

    @Autowired
    private AlumnusArchiveRepository alumnusArchiveRepository;

    @Override
    ImportType getImportType() {
        return ImportType.OTHER;
    }

    @Override
    void changeDto(OtherExcelDto excelDto) {
        /*//学历转换
        if(!StringUtils.isEmpty(excelDto.getEduBackgroundStr())){
            excelDto.setEduBackground(getEduBackground(excelDto.getEduBackgroundStr()));
        }
        //性别转换
        if(!StringUtils.isEmpty(excelDto.getSexStr())){
            excelDto.setSex(getSex(excelDto.getSexStr()));
        }
        //民族转换
        if(!StringUtils.isEmpty(excelDto.getNationStr())){
            excelDto.setNation(getNation(excelDto.getNationStr()));
        }
        //政治面貌转换
        if(!StringUtils.isEmpty(excelDto.getPoliticsStatusStr())){
            excelDto.setPoliticsStatus(getPoliticsStatus(excelDto.getPoliticsStatusStr()));
        }*/

    }


    @Override
    List<OtherExcelDto> excelToDto(MultipartFile file) throws IOException {
        //同步读返回，数据量大会把数据放到内存里面
        InputStream is = null;
        try {
            is = file.getInputStream();
            boolean heads = getHeads(file);
            if (heads){
                List<OtherExcelDto> list = EasyExcel.read(is).head(OtherExcelDto.class).sheet().doReadSync();
                return list.stream().filter(e -> !e.isEmptyLine()).collect(Collectors.toList());
            }else {
                throw new ResultException(ErrorCode.EXCEL_ERROR);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return new ArrayList<>();
    }

    @Override
    List<ImportMatchResult> validDto(ImportProviderContext context, OtherExcelDto excelDto, int num) {
        return context.matches(Collections.singletonList(new OtherMatchable(excelDto)),num);
    }

    @Override
    void setProviderContext(List<OtherExcelDto> excelList, ImportProviderContext context) {
        /*List<AlumnusArchive> all = alumnusArchiveRepository.findAll();
        List<String> names = all.stream().map(AlumnusArchive::getName).collect(Collectors.toList());
        List<String> idNumbers = all.stream().map(AlumnusArchive::getIdNumber).collect(Collectors.toList());
        context.setAttributes("all",all);
        context.setAttributes("names",names);
        context.setAttributes("idNumbers",idNumbers);*/
    }

    //校验表头
    public static LinkedHashSet<String> getHead(MultipartFile file) throws IOException {
        LinkedHashSet<String> result = new LinkedHashSet<>();

        InputStream stream = file.getInputStream();
        Workbook wkbook = WorkbookFactory.create(stream);
        Sheet rs = wkbook.getSheetAt(0);
        Row row = rs.getRow(0);
        try {
            for (int i=0; i<row.getLastCellNum(); i++){
                String cellData = null;
                try {
                    cellData = getCellValue(row.getCell(i));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                //            result.add(cellData.replaceAll(" ", ""));
                result.add(cellData);
            }
        } catch (RuntimeException e) {
            throw new ResultException(ErrorCode.EXCEL_ERROR);
        }
        return result;
    }
    public static String getCellValue(Cell cell) throws Exception {
        CellType cellType = cell.getCellType();
        String cellValue = "";
        switch (cellType) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) { // 日期   可能无法识别
                    Date date = cell.getDateCellValue();
                    cellValue = new DateTime(date).toString("yyyy-MM-dd");
                } else {
                    // 不是日期格式 防止数字过长
                    cell.setCellType(STRING);
                    cellValue = cell.toString();
                }
                break;
            case FORMULA:
                cellValue = cell.getCellFormula();
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN: // 布尔
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK: // 空
                break;
            case ERROR:
                throw new Exception("单元格类型异常");
        }
        return cellValue;
    }

    public static final List<String> MY_LIST = Collections.unmodifiableList(new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            add("* 姓名（必填）");
            add("* 身份证号码（必填）");
            add("学历");
            add("性别");
            add("* 培训时间（必填）");
            add("* 培训名称（必填）");
            add("证明人");
            add("主办单位");
            add("民族");
            add("籍贯");
            add("政治面貌");
            add("地址");
            add("手机号码");
            add("邮箱");
        }
    });

    public static boolean getHeads(MultipartFile file) throws IOException {
        LinkedHashSet<String> head = getHead(file);
        for (String s : head) {
            if (!MY_LIST.contains(s)){
                return false;
            }
        }
        return true;
    }
}
