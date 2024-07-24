package me.brilliant.system.modules.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.brilliant.boot.web.annotation.AnonymousGetMapping;
import me.brilliant.boot.web.mvc.BaseController;
import me.brilliant.boot.web.plugin.limit.Limit;
import me.brilliant.boot.web.plugin.limit.LimitType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Star Chou
 * @description 接口限流测试类
 * @create 2024/7/10
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "系统：限流测试管理")
@RequestMapping("/api/limit")
public class LimitController extends BaseController {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    /**
     * 测试限流注解，下面配置说明该接口 60秒内最多只能访问 10次，保存到redis的键名为 limit_test，
     */
    @AnonymousGetMapping("/test")
    @Operation(summary = "测试")
    @Limit(key = "test", period = 10, count = 2, name = "testLimit", prefix = "limit", limitType = LimitType.IP)
    public int testLimit() {
        return ATOMIC_INTEGER.incrementAndGet();
    }

}

