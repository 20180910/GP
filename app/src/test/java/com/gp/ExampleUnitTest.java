package com.gp;

import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void asfd() throws Exception {
        String a="a123b";
        String substring = a.substring(a.indexOf("a")+1, a.indexOf("b"));
        System.out.println(substring);
        System.out.println(a.indexOf("a"));
        System.out.println(a.indexOf("b"));
    }
    @Test
    public void aasfsfd() throws Exception {
        String a="v_sz000858=51~五 粮 液~000858~78.98~76.80~77.79~415294~217206~197898~78.98~882~78.97~3~78.96~7~78.95~20~78.94~10~78.99~65~79.00~916~79.01~46~79.02~27~79.03~15~15:00:03/78.98/7062/S/55771095/9604|14:57:00/78.81/50/B/394042/9515|14:56:57/78.81/172/B/1355276/9513|14:56:54/78.79/275/S/2166827/9511|14:56:51/78.80/147/B/1158333/9509|14:56:48/78.80/384/S/3025949/9507~20180531150136~2.18~2.84~79.59~76.85~78.98/415294/3266151334~415294~326615~1.09~27.74~~79.59~76.85~3.57~2997.90~3065.69~5.14~84.48~69.12~1.40~-147~78.66~15.42~31.69";
        String[] split = a.split("~");
        for (int i = 0; i < split.length; i++) {
            System.out.println(i+"=="+split[i]);
        }
    }
    @Test
    public void asdf() throws Exception {
        String a="v_sz002720=51~宏良股份~002720~0.00~0.00~0.00~0~0~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~~20180601150133~0.00~0.00~0.00~0.00~0.00/0/0~0~0~~0.00~U~0.00~0.00~0.00~~~0.00~0.00~0.00~0.00~0~0.00~~";
        String[] split = a.split("~");
        for (int i = 0; i < split.length; i++) {
            System.out.println(i+"=="+split[i]);
        }
    }
    @Test
    public void asddf() throws Exception {
        String webUrl1 = "http://time.tianqi.com/";//bjTime
        String webUrl2 = "http://www.baidu.com";//百度
        String webUrl3 = "http://www.taobao.com";//淘宝
        String webUrl4 = "http://www.ntsc.ac.cn";//中国科学院国家授时中心
        String webUrl5 = "http://www.360.cn";//360
        String webUrl6 = "http://www.beijing-time.org";//beijing-time
        System.out.println(getWebsiteDatetime(webUrl1) + " [bjtime]");
        System.out.println(getWebsiteDatetime(webUrl2) + " [百度]");
        System.out.println(getWebsiteDatetime(webUrl3) + " [淘宝]");
        System.out.println(getWebsiteDatetime(webUrl4) + " [中国科学院国家授时中心]");
        System.out.println(getWebsiteDatetime(webUrl5) + " [360安全卫士]");
        System.out.println(getWebsiteDatetime(webUrl6) + " [beijing-time]");
    }
    private static String getWebsiteDatetime(String webUrl){
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
            return sdf.format(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}