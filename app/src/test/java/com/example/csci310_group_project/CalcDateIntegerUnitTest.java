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

public class CalcDateIntegerUnitTest {

    private BasicFragment tester;

    @Before
    public void setup() {
        tester = new BasicFragment();
    }

    @Test
    public void testCalcDateInteger0() {
        assertEquals((Integer) 20221117 , tester.CalcDateInteger(2022, 11, 17));
    }
    @Test
    public void testCalcDateInteger1() {
        assertEquals((Integer) 30920105 , tester.CalcDateInteger(3092, 1, 5));
    }
    @Test
    public void testCalcDateInteger2() {
        assertEquals((Integer) 20050804 , tester.CalcDateInteger(2005, 8, 4));
    }
    @Test
    public void testCalcDateInteger3() {
        assertNotEquals((Integer) 2022117 , tester.CalcDateInteger(2022, 1, 17));
    }
    @Test
    public void testCalcDateInteger4() {
        assertNotEquals( (Integer) 309215 , tester.CalcDateInteger(3092, 1, 5));
    }
    @Test
    public void testCalcDateInteger5() {
        assertNotEquals((Integer) 2082131 , tester.CalcDateInteger(2082, 10, 31));
    }
}