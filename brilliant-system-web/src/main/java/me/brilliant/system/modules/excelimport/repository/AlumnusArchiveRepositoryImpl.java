package me.brilliant.system.modules.excelimport.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.brilliant.system.modules.excelimport.entity.AlumnusArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 * @author Star Chou
 * @description 注意，jdbc 需要配置批处理才能生效
 * &useServerPrepStmts=false&rewriteBatchedStatements=true
 * @create 2024/7/17
 */
public class AlumnusArchiveRepositoryImpl {

    @PersistenceContext
    private EntityManager em;

    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<AlumnusArchive> list) {
        Iterator<AlumnusArchive> iterator = list.iterator();
        int index = 0;
        int batchSize = 1000;
        while (iterator.hasNext()) {
            em.persist(iterator.next());
            index++;
            if (index % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }
        if (index % batchSize != 0) {
            em.flush();
            em.clear();
        }
    }
}
