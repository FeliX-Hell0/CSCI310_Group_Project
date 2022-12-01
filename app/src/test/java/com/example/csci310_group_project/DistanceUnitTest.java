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
public class DistanceUnitTest {
    private static final double DELTA = 1e-15;
    @Test
    public void result1() {
        assertEquals(1435263, (int)(BasicFragment.distance(30, 40, -100, -110, 'K') * 1000));
    }
    @Test
    public void result2() {
        assertEquals(774466, (int)(BasicFragment.distance(30, 40, -100, -110, 'N') * 1000));
    }
    @Test
    public void result3() {
        assertEquals(0, (int)(BasicFragment.distance(30, 30, -100, -100, 'K') * 1000));
    }
    @Test
    public void result4() {
        assertEquals(0, (int)(BasicFragment.distance(30, 30, -100, -100, 'N') * 1000));
    }
    @Test
    public void result5(){
        assertEquals(BasicFragment.distance(30, 40, -100, -110, 'K'), BasicFragment.distance(40, 30, -110, -100, 'K'), DELTA);
    }
    @Test
    public void result6(){
        assertEquals(BasicFragment.distance(30, 40, -100, -110, 'N'), BasicFragment.distance(40, 30, -110, -100, 'N'), DELTA);
    }
    @Test
    public void result7(){
        assertNotEquals(BasicFragment.distance(30, 40, -100, -110, 'K'), BasicFragment.distance(30, 40, -100, -110, 'N'), DELTA);
    }
}

