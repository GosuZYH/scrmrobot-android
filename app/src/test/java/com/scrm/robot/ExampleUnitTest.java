package com.scrm.robot;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String str1 = "余庆测试标签";
        String str2 = "余庆测试标签";
        Boolean result = str1.contains(str2);
        assertEquals(true, result);

    }
}