package me.brilliant.system.modules.batch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
@Repository
public interface MemberLevelDegradeTaskRepository extends JpaRepository<MemberLevelDegradeTask, Long>, JpaSpecificationExecutor<MemberLevelDegradeTask> {

    @Query(value = "select * from MemberLevelDegradeTask as t where t.taskStatus = 'Init' and t.nextProcessTime <= current_timestamp limit 1", nativeQuery = true)
    MemberLevelDegradeTask findNextResumeTask();

    @Query("update MemberLevelDegradeTask set nextProcessTime=?3 where id=?1 and nextProcessTime=?2")
    @Transactional
    @Modifying
    int lockTask(Long id, LocalDateTime currentProcessTime, LocalDateTime nextProcessTime);

    MemberLevelDegradeTask findFirstByTaskStatusAndCreateTimeGreaterThanEqual(DegradeTaskStatus taskStatus, LocalDateTime localDateTime);
}
