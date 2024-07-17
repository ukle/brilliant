package me.brilliant.system.modules.batch.match;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.RegexPool;
import me.brilliant.boot.web.exception.ResultException;
import me.brilliant.system.modules.batch.ImportProviderContext;
import me.brilliant.system.modules.batch.dto.ImportMatchResult;
import me.brilliant.system.modules.batch.dto.OtherExcelDto;
import me.brilliant.system.modules.batch.entity.AlumnusArchive;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
public class OtherMatchable implements ImportMatchable {

    private OtherExcelDto excelDto;

    public OtherMatchable(OtherExcelDto excelDto) {
        this.excelDto = excelDto;
    }

    @Override
    public List<ImportMatchResult> match(ImportProviderContext context, int num) {
        //返回错误信息
        List<ImportMatchResult> list = new ArrayList<>();
        //获取上下文数据
        List<String> names = (List<String>) context.getAttributes("names");
        List<String> idNumbers = (List<String>) context.getAttributes("idNumbers");
        List<AlumnusArchive> all = (List<AlumnusArchive>) context.getAttributes("all");
        /*
        //获取姓名、学号、身份证号
        String name = excelDto.getName();
        String idNumber = excelDto.getIdNumber();

        //校验姓名
        if (StringUtils.isEmpty(name)) {
            list.add(new ImportMatchResult("姓名不能为空",num));
        }
        //校验身份证格式
        if (StringUtils.isEmpty(idNumber)){
            list.add(new ImportMatchResult("身份证不能为空",num));
        } else{
            boolean match = matchIdNumber(idNumber);
            if (match){
                list.add(new ImportMatchResult("身份证格式错误",num));

            }
        }

        //校验政治面貌
        String politicsStatus = excelDto.getPoliticsStatusStr();
        if (!StringUtils.isEmpty(politicsStatus) && matchPoliticsStatus(politicsStatus)) {
            list.add(new ImportMatchResult("政治面貌数据错误",num));
        }
        //校验手机号码
        String phone = excelDto.getPhone();
        if (!StringUtils.isEmpty(phone) && matchPhone(phone)) {
            list.add(new ImportMatchResult("手机号码格式错误",num));
        }
        //校验邮箱
        String email = excelDto.getEmail();
        if (!StringUtils.isEmpty(email) && matchEmail(email)) {
            list.add(new ImportMatchResult("邮箱格式错误",num));
        }*/
        return list;
    }
    /**
     * 校验身份证号
     *
     * @param idNumber
     * @return
     */
    private boolean matchIdNumber(String idNumber) {
        String strPattern = RegexPool.CITIZEN_ID;
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(idNumber);
        return !m.matches();
    }

    /**
     * 校验手机号
     *
     * @param phone
     * @return
     */
    private boolean matchPhone(String phone) {
        String strPattern = RegexPool.MOBILE;
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(phone);
        return !m.matches();
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return
     */
    private boolean matchEmail(String email) {
        String strPattern = RegexPool.EMAIL;
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(email);
        return !m.matches();
    }
}
