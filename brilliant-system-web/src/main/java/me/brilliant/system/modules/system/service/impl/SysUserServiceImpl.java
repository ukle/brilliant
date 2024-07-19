package me.brilliant.system.modules.system.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.brilliant.boot.web.base.BaseServiceImpl;
import me.brilliant.boot.web.security.dto.UserAuthInfo;
import me.brilliant.system.modules.system.model.dto.*;
import me.brilliant.system.modules.system.model.entity.SysUser;
import me.brilliant.system.modules.system.repository.SysUserRepository;
import me.brilliant.system.modules.system.service.SysUserService;
import me.brilliant.boot.web.result.PageResult;
import me.brilliant.boot.web.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    /**
     * 根据条件分页查询
     *
     * @param criteria 查询条件
     * @param pageable 分页信息
     * @return 分页数据
     */
    public PageResult<SysUserListDto> queryByPage(SysUserQueryCriteria criteria, PageRequest pageable) {
        Page<SysUser> page = this.repository.findAll((root, criteriaQuery, cb) ->
                QueryHelp.getPredicate(root, criteria, cb), pageable);
        List<SysUserListDto> convert = page.getContent().stream().map(SysUser::convertToSysUserListDto).collect(Collectors.toList());
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
        SysUser sysUser = this.repository.save(createDto.convertToSysUser());
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
        updateDto.mergeTo(sysUser);
        this.repository.save(sysUser);
        return sysUser.convertToSysUserDto();
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
}

