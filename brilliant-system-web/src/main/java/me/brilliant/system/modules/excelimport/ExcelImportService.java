package me.brilliant.system.modules.excelimport;

import me.brilliant.boot.web.exception.ResultException;
import me.brilliant.system.constant.ErrorCode;
import me.brilliant.system.modules.excelimport.constant.ImportType;
import me.brilliant.system.modules.excelimport.dto.BaseExcelDto;
import me.brilliant.system.modules.excelimport.dto.ValidResultDto;
import me.brilliant.system.modules.excelimport.entity.AlumnusArchive;
import me.brilliant.system.modules.excelimport.repository.AlumnusArchiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
@Service
public class ExcelImportService {

    @Autowired
    private ImportProviderManager importProviderManager;

    @Autowired
    private AlumnusArchiveRepository alumnusArchiveRepository;

    /**
     * 导入数据
     *
     * @param type
     * @param file
     * @return
     */
    public ValidResultDto importData(Integer type, MultipartFile file) throws IOException {
        ImportType importType = ImportType.getImportType(type);
        if (importType == null) {
            throw new ResultException(ErrorCode.IMPORT_TYPE_ERROR);
        }
        AbstractImportProvider<? extends BaseExcelDto> importProvider = importProviderManager.getImportProvider(importType);
        ValidResultDto validResultDto = importProvider.executeValidData(file);
        if (validResultDto.isValidSuccess()) {
            // 批量插入 validResultDto.detailList
            batchInsert(validResultDto.getAlumnusArchives());
        }
        return validResultDto;
    }


    private void batchInsert(List<AlumnusArchive> list) {
        alumnusArchiveRepository.batchSave(list);
    }

}

