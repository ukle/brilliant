package me.brilliant.system.config;

import me.brilliant.system.modules.security.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/9
 */
@Component("auditorAware")
public class AuditorConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            // 这里应根据实际业务情况获取具体信息
            return Optional.of(SecurityUtils.getCurrentUsername());
        } catch (Exception ignored) {
            // 用户定时任务，或者无Token调用的情况
            return Optional.of("System");
        }
    }
}
