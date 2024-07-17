package me.brilliant.system.modules.excelimport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import me.brilliant.boot.web.base.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
@Data
@Entity
@Table(name = "alumnus_archive")
@SQLDelete(sql = "update alumnus_archive set deleted = 1 where id = ?")
@Where(clause = "deleted = 0")
public class AlumnusArchive extends BaseEntity {

    @Column(name = "name", nullable = false, columnDefinition = "varchar(50) COMMENT '姓名'")
    private String name;

    @Column(name = "sex", nullable = false, columnDefinition = "int COMMENT '性别：1男，2女'")
    private Integer sex;

    @Column(name = "phone", nullable = false, columnDefinition = "varchar(11) COMMENT '手机号'")
    private String phone;

    @Column(name = "educational_experience", nullable = false, columnDefinition = "text COMMENT '教育经验'")
    private String educationalExperience;

}
