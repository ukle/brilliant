package me.brilliant.system.modules.batch;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.brilliant.boot.web.task.AbstractBatchService;
import me.brilliant.boot.web.task.ThreadBatchContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
@Slf4j
@Service
public class MemberLevelDegradeTaskService extends AbstractBatchService<MemberLevelDegradeTask> {

    @Autowired
    private MemberLevelDegradeTaskRepository memberLevelDegradeTaskRepository;

    public MemberLevelDegradeTaskService(TaskExecutor taskExecutor) {
        super(false, 1, 2, taskExecutor);
    }

    // 每日5点开始执行
    @Scheduled(cron = "0 0 5 * * *")
    private void runDegrade() {
        log.info("开始执行会员降级");
        this.awake();
    }

    @Override
    protected MemberLevelDegradeTask nextItem(ThreadBatchContext batchContext) {
        return memberLevelDegradeTaskRepository.findNextResumeTask();
    }

    @Override
    protected boolean lockItem(ThreadBatchContext batchContext, MemberLevelDegradeTask item) {
        return memberLevelDegradeTaskRepository.lockTask(item.getId(), item.getNextProcessTime(), item.getNextProcessTime().plusMinutes(1)) == 1;
    }

    @Override
    protected void processItem(ThreadBatchContext batchContext, MemberLevelDegradeTask item) {
        Long customerId = item.getCustomerId();
        Long merchantId = item.getMerchantId();
        int gradeCode = item.getGradeCode();

        log.info("正在提交顾客 {} 的降级处理, 降级信息 {}", customerId, JSONUtil.toJsonStr(item));
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            //降级前先查询储值卡余额和规则。
//            boolean upgradeConfirm = customerMemberCardService.upgradeConfirm(merchantId, customerId);
            boolean upgradeConfirm = false;
            if (upgradeConfirm) {

                item.setTaskStatus(DegradeTaskStatus.Success);
            } else {
                item.setTaskStatus(DegradeTaskStatus.NeedNot);
            }
        } catch (Exception ex) {
            item.setTaskStatus(DegradeTaskStatus.Fail);
            log.error("处理顾客 {} 降级存在异常. {}", customerId, ex);
        }
        sw.stop();
        item.setDuration((int) sw.getTotalTimeMillis());
        memberLevelDegradeTaskRepository.save(item);
    }

    @Override
    protected boolean endingBatchThread(ThreadBatchContext batchContext) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minusDays = now.minusDays(1);
        LocalDateTime time = LocalDateTime.of(minusDays.toLocalDate(), LocalTime.MIN);
        MemberLevelDegradeTask result = memberLevelDegradeTaskRepository.findFirstByTaskStatusAndCreateTimeGreaterThanEqual(
                DegradeTaskStatus.Init,
                time
        );
        return result == null;
    }
}
