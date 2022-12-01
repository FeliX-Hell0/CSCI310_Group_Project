package com.example.csci310_group_project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.example.csci310_group_project.fragment.BasicFragment;

import org.junit.Before;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class MakeDateStringUnitTest {

    private BasicFragment tester;

    @Before
    public void setup() {
        tester = new BasicFragment();
    }

    @Test
    public void testMakeDateString0() {
        assertEquals("convert date components to a string ", "1/1/2000", tester.makeDateString(1, 1, 2000));
    }
    @Test
    public void testMakeDateString1() {
        assertEquals("convert date components to a string ", "5/31/1998", tester.makeDateString(31, 5, 1998));
    }
    @Test
    public void testMakeDateString2() {
        assertEquals("convert date components to a string ", "6/5/2000", tester.makeDateString(5, 6, 2000));
    }
    @Test
    public void testMakeDateString3() {
        assertNotEquals("convert date components to a string ", "01/01/2012", tester.makeDateString(1, 1, 2022));
    }
    @Test
    public void testMakeDateString4() {
        assertNotEquals("convert date components to a string ", "7/11/2012", tester.makeDateString(11, 7, 2022));
    }
    @Test
    public void testMakeDateString5() {
        assertNotEquals("convert date components to a string ", "07/11/2012", tester.makeDateString(11, 7, 2022));
    }
}