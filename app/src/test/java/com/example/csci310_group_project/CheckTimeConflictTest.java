package com.example.csci310_group_project;

import org.junit.Test;

import static org.junit.Assert.*;

public class CheckTimeConflictTest {
    @Test
    public void testCheckTimeConflict1(){
        assertEquals(false, EventDetailActivity.conflictTestHelper("", "1/1/2021, 15:00",120));
    }
    @Test
    public void testCheckTimeConflict2(){
        assertEquals(false, EventDetailActivity.conflictTestHelper(";30006600,30006780", "1/25/2023, 7:59",120));
    }
    @Test
    public void testCheckTimeConflict3(){
        assertEquals(false, EventDetailActivity.conflictTestHelper(";30006600,30006780;29905710,29905890", "1/25/2023, 17:01",120));
    }
    @Test
    public void testCheckTimeConflict4(){
        assertEquals(true, EventDetailActivity.conflictTestHelper(";30006600,30006780;29905710,29905890", "12/27/2022, 6:30",120));
    }
    @Test
    public void testCheckTimeConflict5(){
        assertEquals(true, EventDetailActivity.conflictTestHelper(";30006600,30006780;29905710,29905890", "12/27/2022, 6:30",150));
    }
    @Test
    public void testCheckTimeConflict6(){
        assertEquals(true, EventDetailActivity.conflictTestHelper(";30006600,30006780;29905710,29905890", "12/27/2022, 9:30",150));
    }
}
