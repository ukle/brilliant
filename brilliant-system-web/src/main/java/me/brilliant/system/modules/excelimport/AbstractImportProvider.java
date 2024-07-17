package me.brilliant.system.modules.excelimport;

import me.brilliant.boot.web.exception.ResultException;
import me.brilliant.system.constant.ErrorCode;
import me.brilliant.system.modules.excelimport.constant.ImportType;
import me.brilliant.system.modules.excelimport.dto.BaseExcelDto;
import me.brilliant.system.modules.excelimport.dto.ImportMatchResult;
import me.brilliant.system.modules.excelimport.dto.ValidResultDto;
import me.brilliant.system.modules.excelimport.entity.AlumnusArchive;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
public abstract class AbstractImportProvider<TExcelDto extends BaseExcelDto>  {

    /**
     * 限制最大一万条
     */
    private static final Integer MAX_ITEM_COUNT = 10000;

    /**
     * 获得导入类型
     */
    abstract ImportType getImportType();

    /**
     * 模型转换补充 需要转换成id或枚举
     *
     * @return
     */
    abstract void changeDto(TExcelDto excelDto);

    /**
     * excel文件转换成ExcelDto列表
     */
    abstract List<TExcelDto> excelToDto(MultipartFile file) throws IOException;

    /**
     * 补充查询数据到上下文，用于模型转换和验证
     */
    abstract void setProviderContext(List<TExcelDto> excelList, ImportProviderContext context);

    /**
     * 验证DTO，生成失败原因列表
     */
    abstract List<ImportMatchResult> validDto(ImportProviderContext context, TExcelDto excelDto,int num);

    public ValidResultDto executeValidData(MultipartFile file) throws IOException {
        ValidResultDto validResultDto = new ValidResultDto();
        //excel文件转换为DTO
        List<TExcelDto> tExcelDtos = excelToDto(file);
        if (tExcelDtos.size() > MAX_ITEM_COUNT) {
            throw new ResultException(ErrorCode.IMPORT_MAX_COUNT_ERROR);
        }
        if (tExcelDtos.size() == 0) {
            throw new ResultException(ErrorCode.IMPORT_ZERO_COUNT_ERROR);
        }
        ImportProviderContext context = new ImportProviderContext();
        setProviderContext(tExcelDtos, context);
        List<ImportMatchResult> resultList =new ArrayList<>();
        for (int i = 0; i<tExcelDtos.size();i++) {
            TExcelDto tExcelDto = tExcelDtos.get(i);
            //dto数据转换
            changeDto(tExcelDto);
            //校验数据
            List<ImportMatchResult> matchResultList = validDto(context, tExcelDto,i+1);
            if (!CollectionUtils.isEmpty(matchResultList)) {
                resultList.addAll(matchResultList);
            }
        }
        if(!CollectionUtils.isEmpty(resultList)){
            validResultDto.setValidSuccess(false);
            validResultDto.setMatchResults(resultList);
        }
        if (validResultDto.isValidSuccess()) {
            List<AlumnusArchive> collect = tExcelDtos.stream().map(BaseExcelDto::toAlumnusArchive).collect(Collectors.toList());
            validResultDto.setAlumnusArchives(collect);
        }
        validResultDto.setTotalCount(tExcelDtos.size());
        return validResultDto;
    }
}
