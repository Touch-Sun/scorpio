package com.touchsun.scorpio;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import com.touchsun.scorpio.config.ScorpioConfig;
import com.touchsun.scorpio.plugin.VirtualBrowser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Scorpio容器健康监测[测试]
 *
 * @author Lee
 */
public class TestScorpio {

    @BeforeClass
    public static void beforeClass() {
        // 所有的单元测试启动之前，先看Scorpio是否启动了
        if (!scorpioIsEnable()) {
            LogFactory.get().error("在端口[io-" + ScorpioConfig.DEFAULT_PORT + "]未检测到Scorpio进程，请启动Scorpio后进行测试");
            System.exit(1);
        } else {
            LogFactory.get().info("在端口[io-" + ScorpioConfig.DEFAULT_PORT + "]检测到Scorpio进程，开始进行单元测试");
        }
    }

    /**
     * 测试路径[/]访问状况
     */
    @Test
    public void testHelloScorpio() {
        String html = getContent("/");
        Assert.assertEquals(html, ScorpioConfig.MSG_WELCOME);
    }

    /**
     * 测试HTML文件[/hello.html]访问状况
     */
    @Test
    public void testHtmlScorpio() {
        String html = getContent("/hello.html");
        Assert.assertEquals(html, "Hi,Scorpio,this is html file!");
    }

    /**
     * 测试并发访问场景下的处理速度
     *
     * @throws InterruptedException 线程中断异常
     */
    @Test
    public void testTimeConsumeScorpio() throws InterruptedException {
        // 准备一个线程池
        ThreadPoolExecutor executor = new
                ThreadPoolExecutor(20, 20, 60,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
        // 计时器开始计时
        TimeInterval timeInterval = DateUtil.timer();
        // 连续执行3个任务
        for (int i = 0; i < 20; i++) {
            executor.execute(() -> getContent(ScorpioConfig.URI_ROOT + ScorpioConfig.PAGE_NAME_HTML_TIME_CONSUME));
        }
        // 尝试关闭线程池
        executor.shutdown();
        // 池内若有线程任务未执行完毕,则等待执行完毕,给出1小时的时间,1小时之内若执行完毕,直接返回,超过1小时直接返回
        boolean executeStatus = executor.awaitTermination(1, TimeUnit.HOURS);
        // 断言时间间隔
        if (executeStatus) {
            long totalTime = timeInterval.interval();
            LogFactory.get().info("请求总用时{}ms", totalTime);
            Assert.assertTrue(totalTime < 6000);
        }
    }

    /**
     * 测试webApps目录下配置的新App
     */
    @Test
    public void testLocalAppScorpio() {
        String html = getContent("/numbers/index.html");
        Assert.assertEquals(html, "This is the numbers' app!");
    }

    /**
     * 测试server.xml配置的App
     */
    @Test
    public void testXMLAppScorpio() {
        String html = getContent("/score/index.html");
        Assert.assertEquals(html, "This is a configuration type application of score!");
    }

    /**
     * 测试访问不存在的文件[notExist.html],时Scorpio的响应情况
     */
    @Test
    public void testNotExistScorpio() {
        // 访问一个不存在的文件,查看Http的响应信息
        String html = getHttpContent("/notExist.html");
        boolean match = StrUtil.containsAny(html, "HTTP/1.1 404 Not Found");
        Assert.assertTrue(match);
    }

    /**
     * 测试访问主动异常页面[exception.html],时Scorpio的响应情况
     */
    @Test
    public void testExceptionScorpio() {
        // 访问一个不存在的文件,查看Http的响应信息
        String html = getHttpContent("/exception.html");
        boolean match = StrUtil.containsAny(html, "HTTP/1.1 500 Internal Server Error");
        Assert.assertTrue(match);
    }

    /**
     * 测试访问应用,但不指定文件时,时Scorpio的响应情况
     */
    @Test
    public void testDefaultWelcomeScorpio() {
        // 访问一个应用但不指定文件,查看Http的响应信息
        String html = getHttpContent("/student");
        boolean match = StrUtil.containsAny(html, "This is a configuration type application of student!");
        Assert.assertTrue(match);
    }

    /**
     * 测试访问JPG文件,时Scorpio的响应情况
     */
    @Test
    public void testJPGScorpio() {
        // 访问一个JPG,查看Http的响应信息
        String html = getHttpContent("/girl.jpg");
        boolean match = StrUtil.containsAny(html, "jpeg");
        Assert.assertTrue(match);
    }

    /**
     * 测试访问JPG文件,时Scorpio的响应情况
     */
    @Test
    public void testJPGBytesScorpio() {
        // 访问一个JPG,查看Http的响应信息
        byte[] body = getContentBytes("/girl.jpg");
        boolean more = body.length > 300000;
        Assert.assertTrue(more);
    }

    /**
     * 测试Scorpio对Servlet的解析支持
     */
    @Test
    public void testServletScorpio() {
        String html = getContent("/servlet");
        Assert.assertEquals(html, "你好Scorpio,我是你的第一个Servlet");
    }

    /**
     * 调用虚拟浏览器请求Scorpio[返回Body信息]
     *
     * @param uri URI
     * @return HTML
     */
    public String getContent(String uri) {
        String url = StrUtil.format("http://{}:{}{}",
                ScorpioConfig.DEFAULT_ADDRESS, ScorpioConfig.DEFAULT_PORT, uri);
        return VirtualBrowser.getContent(url);
    }

    /**
     * 调用虚拟浏览器请求Scorpio[返回Body字节信息]
     *
     * @param uri URI
     * @return byte[]
     */
    public byte[] getContentBytes(String uri) {
        String url = StrUtil.format("http://{}:{}{}",
                ScorpioConfig.DEFAULT_ADDRESS, ScorpioConfig.DEFAULT_PORT, uri);
        return VirtualBrowser.getContentBytes(url);
    }

    /**
     * 调用虚拟浏览器请求Scorpio[返回Http全部信息]
     *
     * @param uri URI
     * @return HTML
     */
    public String getHttpContent(String uri) {
        String url = StrUtil.format("http://{}:{}{}",
                ScorpioConfig.DEFAULT_ADDRESS, ScorpioConfig.DEFAULT_PORT, uri);
        return VirtualBrowser.getHttpContent(url);
    }

    /**
     * 判定Scorpio是否启动
     * 向Scorpio[/]URI发生请求，返回内容不为空，证明存活
     *
     * @return 是否启动布尔值
     */
    public static boolean scorpioIsEnable() {
        String url = StrUtil.format("http://{}:{}{}",
                ScorpioConfig.DEFAULT_ADDRESS, ScorpioConfig.DEFAULT_PORT, "/");
        String html = VirtualBrowser.getContent(url);
        return StrUtil.isNotEmpty(html);
    }
}
