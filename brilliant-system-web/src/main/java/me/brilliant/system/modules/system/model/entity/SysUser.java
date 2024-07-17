package me.brilliant.system.modules.system.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import me.brilliant.boot.web.base.BaseEntity;
import me.brilliant.boot.web.security.dto.UserAuthInfo;
import me.brilliant.system.modules.system.model.dto.SysUserDto;
import me.brilliant.system.modules.system.model.dto.SysUserListDto;
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

    @Column(name = "username", nullable = false, columnDefinition = "varchar(30) COMMENT '账号'")
    private String username;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(30) COMMENT '姓名'")
    private String name;

    @Column(name = "phone", nullable = false, columnDefinition = "varchar(11) COMMENT '手机号'")
    private String phone;

    @Column(name = "dept_id", nullable = false, columnDefinition = "bigint COMMENT '部门ID'")
    private Long deptId;

    @Column(name = "password", nullable = false, columnDefinition = "varchar(100) COMMENT '登录密码'")
    private String password;

    @Column(name = "status", nullable = false, columnDefinition = "int COMMENT '状态（1启用，0禁用）'")
    private Integer status;

    @Column(name = "admin", nullable = false, columnDefinition = "tinyint COMMENT '是否为admin账号（0否，1是）'")
    private Boolean admin = false;

    public SysUserDto convertToSysUserDto() {
        SysUserDto sysUserDto = new SysUserDto();
        sysUserDto.setStatus(this.getStatus());
        sysUserDto.setUsername(this.getUsername());
        sysUserDto.setPhone(this.getPhone());
        sysUserDto.setId(this.getId());
        sysUserDto.setName(this.getName());
        return sysUserDto;
    }

    public SysUserListDto convertToSysUserListDto() {
        SysUserListDto sysUserListDto = new SysUserListDto();
        sysUserListDto.setStatus(this.getStatus());
        // sysUserListDto.setRoleNames(null);
        sysUserListDto.setUsername(this.getUsername());
        sysUserListDto.setPhone(this.getPhone());
        sysUserListDto.setCreateTime(this.getCreateTime());
        sysUserListDto.setId(this.getId());
        sysUserListDto.setName(this.getName());
        return sysUserListDto;
    }

    public UserAuthInfo convertToUserAuthInfo() {
        UserAuthInfo userAuthInfo = new UserAuthInfo();
        userAuthInfo.setStatus(this.getStatus());
        // userAuthInfo.setDataScope(null);
        userAuthInfo.setUsername(this.getUsername());
        // userAuthInfo.setPerms(null);
        userAuthInfo.setUserId(this.getId());
        // userAuthInfo.setRoles(null);
        userAuthInfo.setDeptId(this.getDeptId());
        // userAuthInfo.setNickname(null);
        userAuthInfo.setPassword(this.getPassword());
        userAuthInfo.setAdmin(this.getAdmin());
        return userAuthInfo;
    }
}
