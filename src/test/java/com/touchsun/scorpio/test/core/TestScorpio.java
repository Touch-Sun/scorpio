package com.touchsun.scorpio.test.core;

import cn.hutool.core.util.StrUtil;
import com.touchsun.scorpio.core.config.ScorpioConfig;
import com.touchsun.scorpio.core.plugin.VirtualBrowser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Scorpio容器健康监测[测试]
 * @author Lee
 */
public class TestScorpio {

    @BeforeClass
    public static void beforeClass() {
        // 所有的单元测试启动之前，先看Scorpio是否启动了
        if (!scorpioIsEnable()) {
            System.err.println("在端口[io-" + ScorpioConfig.DEFAULT_PORT + "]未检测到Scorpio进程，请启动Scorpio后进行测试");
            System.exit(1);
        } else {
            System.out.println("在端口[io-" + ScorpioConfig.DEFAULT_PORT + "]检测到Scorpio进程，开始进行单元测试");
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
     * 调用虚拟浏览器请求Scorpio
     * @param uri URI
     * @return HTML
     */
    public String getContent(String uri) {
        String url = StrUtil.format("http://{}:{}{}",
                ScorpioConfig.DEFAULT_ADDRESS, ScorpioConfig.DEFAULT_PORT, uri);
        return VirtualBrowser.getContent(url);
    }

    /**
     * 判定Scorpio是否启动
     * 向Scorpio[/]URI发生请求，返回内容不为空，证明存活
     * @return 是否启动布尔值
     */
    public static boolean scorpioIsEnable() {
        String url = StrUtil.format("http://{}:{}{}",
                ScorpioConfig.DEFAULT_ADDRESS, ScorpioConfig.DEFAULT_PORT, "/");
        String html = VirtualBrowser.getContent(url);
        return StrUtil.isNotEmpty(html);
    }
}
