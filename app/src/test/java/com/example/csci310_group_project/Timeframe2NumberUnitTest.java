package com.example.csci310_group_project;

import org.junit.Test;

import static org.junit.Assert.*;

public class Timeframe2NumberUnitTest {
    Long testnum = Long.valueOf(30006600);
    Long testnum1 = Long.valueOf(30006720);
    @Test
    public void testTimeframe2Number(){
        assertEquals(testnum, EventDetailActivity.timeframe2Number("1/25/2023, 10:00", 120).get(0));
    }

    @Test
    public void testTimeframe2Number1(){
        assertEquals(testnum1, EventDetailActivity.timeframe2Number("1/25/2023, 10:00", 120).get(1));
    }
}
