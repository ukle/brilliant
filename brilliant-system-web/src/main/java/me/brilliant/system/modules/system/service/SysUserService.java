package me.brilliant.system.modules.system.service;


import me.brilliant.boot.web.base.IBaseService;
import me.brilliant.system.modules.security.security.dto.UserAuthInfo;
import me.brilliant.system.modules.system.model.dto.*;
import me.brilliant.system.modules.system.model.entity.SysUser;
import me.brilliant.boot.web.result.PageResult;
import me.brilliant.system.modules.system.model.vo.UserInfoVO;
import me.brilliant.system.modules.system.model.vo.UserPageVO;
import org.springframework.data.domain.PageRequest;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/10
 */
public interface SysUserService extends IBaseService<SysUser, Long> {

    /**
     * 根据条件分页查询
     *
     * @param criteria 查询条件
     * @param pageable 分页信息
     * @return 分页数据
     */
    public PageResult<UserPageVO> queryByPage(SysUserQueryCriteria criteria, PageRequest pageable);

    /**
     * 根据主键获取包装数据
     *
     * @param sysUserId 主键id
     * @return 返回实体Dto
     */
    SysUserDto getById(long sysUserId);

    /**
     * 新增实体
     *
     * @param createDto 新增数据
     * @return 实体Dto
     */
    SysUserDto create(SysUserForCreateDto createDto);

    /**
     * 更新实体
     *
     * @param updateDto 更新数据
     * @return 实体Dto
     */
    SysUserDto update(SysUserForUpdateDto updateDto);

    SysUser findByMobile(String username);

    UserAuthInfo getUserAuthInfo(String username);

    boolean deleteUsers(String idsStr);

    boolean updatePassword(Long userId, String password);

    boolean updateStatus(Long userId, Integer status);

    /**
     * 获取登录用户信息
     *
     * @return
     */
    UserInfoVO getCurrentUserInfo();
}

