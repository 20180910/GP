package com.gp;

import org.junit.Test;

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


}