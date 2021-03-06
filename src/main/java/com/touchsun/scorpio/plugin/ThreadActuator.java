package com.touchsun.scorpio.plugin;

import com.touchsun.scorpio.config.ScorpioConfig;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程执行器
 *
 * @author Lee
 */
public class ThreadActuator {

    /**
     * Web请求线程池
     */
    private static final ThreadPoolExecutor WEB_EXECUTE = new ThreadPoolExecutor(
            // 核心线程数
            // [初始20个线程]
            20,
            // 最大线程数
            // [最多有100个线程]
            100,
            // 空闲等待时间
            // [新加入的线程空闲等待时间若超过60时间单元,会被立即回收,最后仅保留20个线程]
            60,
            // 时间单元
            // [秒]
            TimeUnit.SECONDS,
            // 任务队列
            // [短时间内核心线程数被任务占满,新的任务储存到队列中,核心线程有空闲时来此取出任务执行]
            // [若等待任务超过10个,则创建新的线程,否则不创建新的线程]
            new LinkedBlockingQueue<>(10),
            // 线程工厂
            // [设置线程名称]
            r -> new Thread(r, ScorpioConfig.WEB_THREAD_NAME + r.hashCode()));

    /**
     * Scorpio实例线程池
     */
    private static final ThreadPoolExecutor SCORPIO_EXECUTE = new ThreadPoolExecutor(
            // 核心线程数
            // [初始20个线程]
            20,
            // 最大线程数
            // [最多有100个线程]
            100,
            // 空闲等待时间
            // [新加入的线程空闲等待时间若超过60时间单元,会被立即回收,最后仅保留20个线程]
            60,
            // 时间单元
            // [秒]
            TimeUnit.SECONDS,
            // 任务队列
            // [短时间内核心线程数被任务占满,新的任务储存到队列中,核心线程有空闲时来此取出任务执行]
            // [若等待任务超过10个,则创建新的线程,否则不创建新的线程]
            new LinkedBlockingQueue<>(10),
            // 线程工厂
            // [设置线程名称]
            r -> new Thread(r, ScorpioConfig.SCORPIO_THREAD_NAME + r.hashCode()));

    /**
     * 提交Web请求任务到线程池
     *
     * @param task Runnable任务
     */
    public static void runWebTask(Runnable task) {
        WEB_EXECUTE.execute(task);
    }

    /**
     * Scorpio生成启动提交实例线程池
     *
     * @param task Runnable任务
     */
    public static void executeScorpioInstance(Runnable task) {
        SCORPIO_EXECUTE.execute(task);
    }
}
