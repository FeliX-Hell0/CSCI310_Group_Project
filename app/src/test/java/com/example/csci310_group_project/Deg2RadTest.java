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
public class Deg2RadTest {
    @Test
    public void result100() {
        assertEquals(1745, (int)(BasicFragment.deg2rad(100) * 1000));
    }
    @Test
    public void result200() {
        assertEquals(3490, (int)(BasicFragment.deg2rad(200) * 1000));
    }
    @Test
    public void resultNeg100() {
        assertEquals(-1745, (int)(BasicFragment.deg2rad(-100) * 1000));
    }
    @Test
    public void result0() {
        assertEquals(0, (int)(BasicFragment.deg2rad(0) * 1000));
    }
}