package me.brilliant.system.modules.system.repository;

import me.brilliant.boot.web.base.BaseRepository;
import me.brilliant.system.modules.system.model.entity.SysUser;
import org.springframework.stereotype.Repository;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/11
 */
@Repository
public interface SysUserRepository extends BaseRepository<SysUser, Long> {

    SysUser findFirstByPhone(String username);
    SysUser findByUsername(String username);
}
