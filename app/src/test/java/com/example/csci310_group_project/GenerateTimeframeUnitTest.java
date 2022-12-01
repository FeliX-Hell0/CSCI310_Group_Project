package com.example.csci310_group_project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.example.csci310_group_project.fragment.BasicFragment;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class GenerateTimeframeUnitTest {
    @Test
    public void test1(){
        assertEquals(";30006600,30006780", EventDetailActivity.generateNewTimeframe(";30006600,30006780;29905710,29905890", "29905710,29905890"));
    }
    @Test
    public void test2(){
        assertEquals(";29905710,29905890", EventDetailActivity.generateNewTimeframe(";30006600,30006780;29905710,29905890", "30006600,30006780"));
    }
    @Test
    public void test3(){
        assertEquals(";30006600,30006780;29905710,29905890", EventDetailActivity.generateNewTimeframe(";30006600,30006780;30006540,30006740;29905710,29905890", "30006540,30006740"));
    }
}
