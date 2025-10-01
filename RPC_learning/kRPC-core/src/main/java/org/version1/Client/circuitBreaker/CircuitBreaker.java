package org.version1.Client.circuitBreaker;

import java.util.concurrent.atomic.AtomicInteger;

public class CircuitBreaker {
    private CircuitBreakerState state = CircuitBreakerState.CLOSED; // 熔断器初始状态：关闭
    private AtomicInteger failureCount = new AtomicInteger(0); // 失败请求计数
    private AtomicInteger successCount = new AtomicInteger(0); // 成功请求计数
    private AtomicInteger requestCount = new AtomicInteger(0); // 请求总数
    private final int failureThreshold; // 失败阈值
    private final double halfOpenSuccessRate; // 半开状态下的成功率阈值
    private final long retryTimePeriod; // 重置时间周期
    private long lastFailureTime = 0; // 最后一次失败时间

    // 构造函数初始化熔断器参数
    public CircuitBreaker(int failureThreshold, double halfOpenSuccessRate,long retryTimePeriod) {
        this.failureThreshold = failureThreshold;
        this.halfOpenSuccessRate = halfOpenSuccessRate;
        this.retryTimePeriod = retryTimePeriod;
    }

    //查看当前熔断器是否允许请求通过
    public synchronized boolean allowRequest() {
        //获取当前时间
        long currentTime = System.currentTimeMillis();
        System.out.println("熔断swtich之前!!!!!!!+failureNum=="+failureCount);
        switch (state) {
            case OPEN:
                if (currentTime - lastFailureTime > retryTimePeriod) {
                    state = CircuitBreakerState.HALF_OPEN;
                    resetCounts(); // 重置计数
                    return true; // 允许请求
                }
                System.out.println("熔断生效!!!!!!!");
                return false; // 继续熔断
            case HALF_OPEN:
                requestCount.incrementAndGet(); // 在半开状态下记录请求
                return true; // 允许请求
            case CLOSED:
            default:
                return true; // 服务正常，拒绝请求
        }
    }

    //记录成功
    public synchronized void recordSuccess() {
        if (state == CircuitBreakerState.HALF_OPEN) {
            successCount.incrementAndGet();
            if (successCount.get() >= halfOpenSuccessRate * requestCount.get()) {
                state = CircuitBreakerState.CLOSED; // 恢复正常状态
                resetCounts();
            }
        } else {
            resetCounts(); // 不是半开状态，重置计数
        }
    }
    //记录失败
    public synchronized void recordFailure() {
        failureCount.incrementAndGet(); // 增加失败次数
        System.out.println("记录失败!!!!!!!失败次数"+failureCount);
        lastFailureTime = System.currentTimeMillis(); // 记录最后一次失败时间
        if (state == CircuitBreakerState.HALF_OPEN) {
            state = CircuitBreakerState.OPEN; // 熔断
            lastFailureTime = System.currentTimeMillis();// 记录最后一次失败时间
        } else if (failureCount.get() >= failureThreshold) {// 如果失败次数达到阈值，熔断
            state = CircuitBreakerState.OPEN; // 熔断
        }
    }

    //重置次数
    private void resetCounts() {
        failureCount.set(0);
        successCount.set(0);
        requestCount.set(0);
    }

    public CircuitBreakerState getState() {
        return state;
    }
}
