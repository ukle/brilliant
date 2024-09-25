package me.brilliant.system.modules.system.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import me.brilliant.boot.web.base.BaseEntity;
import me.brilliant.system.modules.security.security.dto.UserAuthInfo;
import me.brilliant.system.modules.system.model.dto.SysUserDto;
import me.brilliant.system.modules.system.model.dto.SysUserListDto;
import me.brilliant.system.modules.system.model.vo.UserInfoVO;
import me.brilliant.system.modules.system.model.vo.UserPageVO;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


/**
 * @author Star Chou
 * @description /
 * @create 2024/7/11
 */
@Data
@Entity
@Table(name = "sys_user")
@SQLDelete(sql = "update sys_user set deleted = 1 where id = ?")
@Where(clause = "deleted = 0")
public class SysUser extends BaseEntity {

    @Column(name = "username", nullable = false, columnDefinition = "varchar(30) COMMENT '用户名'")
    private String username;

    @Column(name = "nickname", nullable = false, columnDefinition = "varchar(30) COMMENT '昵称'")
    private String nickname;

    @Column(name = "gender", nullable = false, columnDefinition = "int COMMENT '性别((1-男 2-女 0-保密)'")
    private Integer gender;

    @Column(name = "password", nullable = false, columnDefinition = "varchar(100) COMMENT '登录密码'")
    private String password;

    @Column(name = "dept_id", nullable = false, columnDefinition = "bigint COMMENT '部门ID'")
    private Long deptId;

    @Column(name = "avatar", nullable = false, columnDefinition = "varchar(100) COMMENT '用户头像'")
    private String avatar;

    @Column(name = "email", nullable = false, columnDefinition = "varchar(100) COMMENT '邮箱'")
    private String email;

    @Column(name = "mobile", nullable = false, columnDefinition = "varchar(11) COMMENT '手机号'")
    private String mobile;

    @Column(name = "status", nullable = false, columnDefinition = "int COMMENT '状态（1启用，0禁用）'")
    private Integer status;

    public SysUserDto convertToSysUserDto() {
        SysUserDto sysUserDto = new SysUserDto();
        sysUserDto.setStatus(this.getStatus());
        sysUserDto.setEmail(this.getEmail());
        sysUserDto.setUsername(this.getUsername());
        sysUserDto.setId(this.getId());
        sysUserDto.setGender(this.getGender());
        // sysUserDto.setRoleIds(null);
        sysUserDto.setMobile(this.getMobile());
        sysUserDto.setDeptId(this.getDeptId());
        sysUserDto.setAvatar(this.getAvatar());
        sysUserDto.setNickname(this.getNickname());
        return sysUserDto;
    }

    public UserAuthInfo convertToUserAuthInfo() {
        UserAuthInfo userAuthInfo = new UserAuthInfo();
        userAuthInfo.setStatus(this.getStatus());
        // userAuthInfo.setDataScope(null);
        userAuthInfo.setUsername(this.getUsername());
        // userAuthInfo.setPerms(null);
        // userAuthInfo.setUserId(null);
        // userAuthInfo.setRoles(null);
        // userAuthInfo.setAdmin(null);
        userAuthInfo.setDeptId(this.getDeptId());
        userAuthInfo.setNickname(this.getNickname());
        userAuthInfo.setPassword(this.getPassword());
        return userAuthInfo;
    }

    public UserPageVO convertToUserPageVO() {
        UserPageVO userPageVO = new UserPageVO();
        userPageVO.setStatus(this.getStatus());
        userPageVO.setEmail(this.getEmail());
        // userPageVO.setRoleNames(null);
        userPageVO.setUsername(this.getUsername());
        // userPageVO.setCreateTime(this.getCreateTime());
        userPageVO.setId(this.getId());
        // userPageVO.setDeptName(null);
        userPageVO.setMobile(this.getMobile());
        // userPageVO.setGenderLabel(null);
        userPageVO.setAvatar(this.getAvatar());
        userPageVO.setNickname(this.getNickname());
        return userPageVO;
    }

    public UserInfoVO toUserInfoVo(SysUser user) {
        if (user == null) return null;
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUsername(this.getUsername());
        // userInfoVO.setPerms(null);
        // userInfoVO.setUserId(null);
        // userInfoVO.setRoles(null);
        userInfoVO.setAvatar(this.getAvatar());
        userInfoVO.setNickname(this.getNickname());
        return userInfoVO;
    }
}
