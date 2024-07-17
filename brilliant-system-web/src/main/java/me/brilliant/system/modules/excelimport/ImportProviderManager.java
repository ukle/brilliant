package me.brilliant.system.modules.excelimport;

import me.brilliant.system.modules.excelimport.constant.ImportType;
import me.brilliant.system.modules.excelimport.dto.BaseExcelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
@Component
public class ImportProviderManager {

    @Autowired
    private OtherImportProvider otherImportProvider;
//    @Autowired
//    private StaffImportProvider staffImportProvider;
//    @Autowired
//    private StudentImportProvider studentImportProvider;

    public AbstractImportProvider<? extends BaseExcelDto> getImportProvider(ImportType importType) {
        if (importType == ImportType.OTHER) {
            return otherImportProvider;
        }else {
            throw new IllegalArgumentException(String.format("不存在类型 %s 对应的 ImportProvider", importType));
        }
    }

}
