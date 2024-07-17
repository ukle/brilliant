package me.brilliant.system.modules.batch;

import jakarta.persistence.*;
import lombok.Data;
import me.brilliant.boot.web.base.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * @author Star Chou
 * @description 会员降级任务
 * @create 2024/7/17
 */
@Data
@Entity
@Table(name = "member_level_degrade_task")
public class MemberLevelDegradeTask extends BaseEntity {

    @Column(name = "customer_id", nullable = false, columnDefinition = "bigint COMMENT '会员标识'")
    private Long customerId;

    @Column(name = "merchant_id", nullable = false, columnDefinition = "bigint COMMENT '商户标识'")
    private Long merchantId;

    @Column(name = "grade_dode", nullable = false, columnDefinition = "int COMMENT ''")
    private int gradeCode;

    @Column(name = "next_process_time", nullable = false, columnDefinition = "dateTime COMMENT '下次运行时间（用于重新运行）'")
    private LocalDateTime nextProcessTime;

    @Column(name = "duration", nullable = false, columnDefinition = "int COMMENT '运行时间（毫秒）'")
    private int duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status", nullable = false, columnDefinition = "varchar(32) COMMENT '任务状态'")
    private DegradeTaskStatus taskStatus = DegradeTaskStatus.Init;

}



