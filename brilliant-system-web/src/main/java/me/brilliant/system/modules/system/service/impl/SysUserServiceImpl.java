package me.brilliant.system.modules.system.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.brilliant.boot.web.base.BaseServiceImpl;
import me.brilliant.system.constant.SystemConstants;
import me.brilliant.system.modules.security.SecurityUtils;
import me.brilliant.system.modules.security.security.dto.UserAuthInfo;
import me.brilliant.system.modules.system.model.dto.*;
import me.brilliant.system.modules.system.model.entity.SysUser;
import me.brilliant.system.modules.system.model.vo.UserInfoVO;
import me.brilliant.system.modules.system.model.vo.UserPageVO;
import me.brilliant.system.modules.system.repository.SysUserRepository;
import me.brilliant.system.modules.system.service.SysUserService;
import me.brilliant.boot.web.result.PageResult;
import me.brilliant.boot.web.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends BaseServiceImpl<SysUserRepository, SysUser, Long> implements SysUserService {

    private final PasswordEncoder passwordEncoder;

    /**
     * 根据条件分页查询
     *
     * @param criteria 查询条件
     * @param pageable 分页信息
     * @return 分页数据
     */
    public PageResult<UserPageVO> queryByPage(SysUserQueryCriteria criteria, PageRequest pageable) {
        Page<SysUser> page = this.repository.findAll((root, criteriaQuery, cb) ->
                QueryHelp.getPredicate(root, criteria, cb), pageable);
        List<UserPageVO> convert = page.getContent().stream().map(SysUser::convertToUserPageVO).collect(Collectors.toList());
        return new PageResult<>(page.getTotalElements(), convert);
    }

    /**
     * 根据主键获取包装数据
     *
     * @param sysUserId 主键id
     * @return 返回实体Dto
     */
    public SysUserDto getById(long sysUserId) {
        //查询用户信息
        SysUser one = getOne(sysUserId);
        return one.convertToSysUserDto();
    }

    /**
     * 新增实体
     *
     * @param createDto 新增数据
     * @return 实体Dto
     */
    @Transactional(rollbackFor = Exception.class)
    public SysUserDto create(SysUserForCreateDto createDto) {
        SysUser sysUser = createDto.convertToSysUser();
        // 设置默认加密密码
        String defaultEncryptPwd = passwordEncoder.encode(SystemConstants.DEFAULT_PASSWORD);
        sysUser.setPassword(defaultEncryptPwd);
        sysUser = this.repository.save(sysUser);
        return sysUser.convertToSysUserDto();
    }

    /**
     * 更新实体
     *
     * @param updateDto 更新数据
     * @return 实体Dto
     */
    @Transactional(rollbackFor = Exception.class)
    public SysUserDto update(SysUserForUpdateDto updateDto) {
        SysUser sysUser = getOne(updateDto.getId());
        String username = updateDto.getUsername();

        long count = repository.countByUsernameAndIdNot(username, updateDto.getId());
        Assert.isTrue(count == 0, "用户名已存在");

        updateDto.mergeTo(sysUser);
        this.repository.save(sysUser);

        // TODO 保存用户角色
//        userRoleService.saveUserRoles(entity.getId(), userForm.getRoleIds());
        return sysUser.convertToSysUserDto();
    }

    @Override
    public void delete(SysUser sysUser) {
        super.delete(sysUser);
    }

    @Override
    public SysUser findByMobile(String mobile) {
        return this.repository.findFirstByPhone(mobile);
    }

    @Override
    public UserAuthInfo getUserAuthInfo(String username) {
        SysUser sysUser = this.repository.findByUsername(username);

        return sysUser.convertToUserAuthInfo();
    }

    @Override
    public boolean deleteUsers(String idsStr) {
        Assert.isTrue(StrUtil.isNotBlank(idsStr), "删除的用户数据为空");
        // 逻辑删除
        List<Long> ids = Arrays.stream(idsStr.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        for (Long id : ids) {
            this.deleteById(id);
        }
        return true;
    }

    @Override
    public boolean updatePassword(Long userId, String password) {
        SysUser one = this.getOne(userId);
        one.setPassword(passwordEncoder.encode(password));
        save(one);
        return true;
    }

    @Override
    public boolean updateStatus(Long userId, Integer status) {
        SysUser one = this.getOne(userId);
        one.setStatus(status);
        save(one);
        return true;
    }

    /**
     * 获取登录用户信息
     *
     * @return {@link UserInfoVO}   用户信息
     */
    @Override
    public UserInfoVO getCurrentUserInfo() {

        String username = SecurityUtils.getUsername();
        // 获取登录用户基础信息
        SysUser user = this.repository.findByUsername(username);

        // entity->VO
        UserInfoVO userInfoVO = user.toUserInfoVo(user);

        // 用户角色集合
        Set<String> roles = SecurityUtils.getRoles();
        userInfoVO.setRoles(roles);

        // todo 用户权限集合
        /*if (CollectionUtil.isNotEmpty(roles)) {
            Set<String> perms = permissionService.getRolePermsFormCache(roles);
            userInfoVO.setPerms(perms);
        }*/
        return userInfoVO;
    }

}

