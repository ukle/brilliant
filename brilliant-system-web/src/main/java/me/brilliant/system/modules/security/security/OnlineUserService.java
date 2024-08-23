package me.brilliant.system.modules.security.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.brilliant.boot.web.utils.RedisUtils;
import me.brilliant.system.modules.security.security.bean.SecurityProperties;
import me.brilliant.system.modules.security.security.core.TokenProvider;
import me.brilliant.system.modules.security.security.dto.JwtUserDto;
import me.brilliant.system.modules.security.security.dto.OnlineUserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/15
 */
@Service
@Slf4j
@AllArgsConstructor
public class OnlineUserService {

    private final SecurityProperties properties;
    private final TokenProvider tokenProvider;
    private final RedisUtils redisUtils;

    /**
     * 保存在线用户信息
     * @param jwtUserDto /
     * @param token /
     * @param request /
     */
    public void save(JwtUserDto jwtUserDto, String token, HttpServletRequest request){
        OnlineUserDto onlineUserDto = new OnlineUserDto(request, jwtUserDto, token);
        String loginKey = tokenProvider.loginKey(token);
        redisUtils.set(loginKey, onlineUserDto, properties.getTokenValidityInSeconds(), TimeUnit.MILLISECONDS);
    }

    /**
     * 查询全部数据，不分页
     * @param username /
     * @return /
     */
    public List<OnlineUserDto> getAll(String username){
        String loginKey = properties.getOnlineKey() +
                (StringUtils.isBlank(username) ? "" : "*" + username);
        List<String> keys = redisUtils.scan(loginKey + "*");
        Collections.reverse(keys);
        List<OnlineUserDto> onlineUserDtos = new ArrayList<>();
        for (String key : keys) {
            onlineUserDtos.add((OnlineUserDto) redisUtils.get(key));
        }
        onlineUserDtos.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUserDtos;
    }

    /**
     * 退出登录
     * @param token /
     */
    public void logout(String token) {
        String loginKey = tokenProvider.loginKey(token);
        redisUtils.del(loginKey);
    }

    /**
     * 查询用户
     * @param key /
     * @return /
     */
    public OnlineUserDto getOne(String key) {
        return (OnlineUserDto) redisUtils.get(key);
    }

    /**
     * 根据用户名强退用户
     * @param username /
     */
    @Async
    public void kickOutForUsername(String username) {
        String loginKey = properties.getOnlineKey() + username + "*";
        redisUtils.scanDel(loginKey);
    }
}
