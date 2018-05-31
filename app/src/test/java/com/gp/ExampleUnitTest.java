package com.gp;

import org.junit.Test;

import static org.junit.Assert.*;

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
}