package com.example.csci310_group_project;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GetEventDatePartsUnitTest {
    private Event event;
    @Before
    public void setup(){
        event = new Event("TestEvent", "Concert", "1/22/2023, 15:00","Org", "This is a test", "LA", 10, 0, false, false, 10.0, 10.0);
    }
    @Test
    public void firstElementCorrect() {
        assertEquals("1", event.getEventDateParts()[0]);
    }
    @Test
    public void secondElementCorrect() {
        assertEquals("22", event.getEventDateParts()[1]);
    }
    @Test
    public void thirdElementCorrect() {
        assertEquals("2023", event.getEventDateParts()[2]);
    }
}
