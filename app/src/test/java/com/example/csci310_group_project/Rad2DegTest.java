package com.example.csci310_group_project;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.csci310_group_project.fragment.BasicFragment;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class Rad2DegTest {
    @Test
    public void result1() {
        assertEquals( 57295, (int)(BasicFragment.rad2deg(1) * 1000));
    }
    @Test
    public void result3() {
        assertEquals(171887, (int)(BasicFragment.rad2deg(3) * 1000));
    }
    @Test
    public void resultNeg1() {
        assertEquals(-57295, (int)(BasicFragment.rad2deg(-1) * 1000));
    }
    @Test
    public void result0() {
        assertEquals(0, (int)(BasicFragment.rad2deg(0) * 1000));
    }
}
