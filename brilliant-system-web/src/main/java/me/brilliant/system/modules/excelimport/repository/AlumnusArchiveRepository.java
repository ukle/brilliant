package me.brilliant.system.modules.excelimport.repository;

import me.brilliant.system.modules.excelimport.entity.AlumnusArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
@Repository
public interface AlumnusArchiveRepository extends JpaRepository<AlumnusArchive, Long>, JpaSpecificationExecutor<AlumnusArchive> {

    void batchSave(List<AlumnusArchive> list);
}
