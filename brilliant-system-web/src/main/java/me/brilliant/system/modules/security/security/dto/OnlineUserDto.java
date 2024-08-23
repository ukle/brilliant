package me.brilliant.system.modules.security.security.dto;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.brilliant.boot.web.utils.IPUtils;

import java.time.LocalDate;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/15
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUserDto {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 岗位
     */
    private String dept;
    /**
     * IP 地址
     */
    private String ip;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 浏览器版本
     */
    private String browserVersion;

    /**
     * 终端系统
     */
    private String os;

    /**
     * token
     */
    private String key;

    /**
     * 登录时间
     */
    private LocalDate loginTime;

    public OnlineUserDto(HttpServletRequest request, JwtUserDto jwtUserDto, String token) {
        this.userName = jwtUserDto.getUsername();
        this.nickName = jwtUserDto.getNickName();
        this.loginTime = LocalDate.now();
        this.key = token;
        String ipAddr = IPUtils.getIpAddr(request);
        if (StrUtil.isNotBlank(ipAddr)) {
            this.setIp(ipAddr);
            String region = IPUtils.getRegion(ipAddr);
            // 中国|0|四川省|成都市|电信 解析省和市
            if (StrUtil.isNotBlank(region)) {
                String[] regionArray = region.split("\\|");
                if (regionArray.length > 2) {
                    this.setProvince(regionArray[2]);
                    this.setCity(regionArray[3]);
                }
            }
        }
        // 获取浏览器和终端系统信息
        String userAgentString = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgentUtil.parse(userAgentString);
        // 系统信息
        this.setOs(userAgent.getOs().getName());
        // 浏览器信息
        this.setBrowserVersion(userAgent.getBrowser().getVersion(userAgentString));
    }

}
