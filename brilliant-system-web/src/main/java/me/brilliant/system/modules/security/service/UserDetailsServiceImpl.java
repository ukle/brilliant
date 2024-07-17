package me.brilliant.system.modules.security.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.brilliant.boot.web.exception.BadRequestException;
import me.brilliant.boot.web.security.UserCacheManager;
import me.brilliant.boot.web.security.dto.JwtUserDto;
import me.brilliant.boot.web.security.dto.UserAuthInfo;
import me.brilliant.system.modules.system.service.SysUserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/15
 */
@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserCacheManager userCacheManager;
    private final SysUserService sysUserService;

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        JwtUserDto jwtUserDto = userCacheManager.getUserCache(username);
        if (jwtUserDto == null) {
            UserAuthInfo userAuthInfo;
            try {
                userAuthInfo = sysUserService.getUserAuthInfo(username);
            } catch (EntityNotFoundException e) {
                // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
                throw new UsernameNotFoundException(username, e);
            }
            if (userAuthInfo == null) {
                throw new UsernameNotFoundException("您的账号不存在，请找管理员添加！");
            } else {
                if (userAuthInfo.getStatus() == 0) {
                    throw new BadRequestException("您的账号已被冻结，请找管理员解锁！");
                }
                jwtUserDto = new JwtUserDto(userAuthInfo);
                // 添加缓存数据
                userCacheManager.addUserCache(username, jwtUserDto);
            }
        }
        return jwtUserDto;
    }
}

