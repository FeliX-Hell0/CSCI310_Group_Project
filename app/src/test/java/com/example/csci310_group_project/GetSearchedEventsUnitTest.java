package com.example.csci310_group_project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.example.csci310_group_project.fragment.BasicFragment;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class GetSearchedEventsUnitTest {

    private BasicFragment tester;
    private ArrayList<Event> eventList, expectedEvents;

    @Before
    public void setup() {
        tester = new BasicFragment();
        eventList = new ArrayList<>();
        expectedEvents = new ArrayList<>();

        // set 5 hard-coded events
        // String eventName, String eventType, String eventDate, String eventOrganizor, String eventDescription, String eventLocation, Integer eventCost, int img, boolean registered,
        //                 Boolean favorite, double lat, double lng
        Event e0 = new Event("ThriftCon LA", "Party", "1/1/2023", "ThriftCon", "The #1 Vintage Clothing and Collectible Convention",
                "Los Angeles Convention Center, 1201 S Figueroa St, Los Angeles, CA 90015", 5, 0, false, false, 34.040569, -118.268761);
        Event e1 = new Event("Sunset Vibes Silent Disco", "Concert", "12/4/2022", "Vista", "A silent disco festival",
                "Vista, 11 Pier Ave, Harmosa Beach, CA 90254", 10, 0, false, false, 33.862350, -118.401650);
        Event e2 = new Event("Anime Pasadena", "Concert", "1/1/2023", "ThriftCon", "The #1 Vintage Clothing and Collectible Convention",
                "Los Angeles Convention Center, 1201 S Figueroa St, Los Angeles, CA 90015", 5, 0, false, false, 34.040569, -118.268761);
        Event e3 = new Event("Beat the Bruins", "Party", "1/1/2023", "USC", "Pre-game Festival",
                "University of Southern California, 3620 S Vermont Ave, Los Angeles, CA", 0, 0, false, false, 34.022260, -118.291420);
        Event e4 = new Event("USC vs UCLA Football Game", "Sport", "1/3/2023", "NCAA", "Must-seen game between famous USC-UCLA rivalry",
                "Los Angeles Convention Center, 1201 S Figueroa St, Los Angeles, CA 90015", 100, 0, false, false, 34.040569, -118.268761);

        eventList.add(e0);
        eventList.add(e1);
        eventList.add(e2);
        eventList.add(e3);
        eventList.add(e4);
    }

    @Test
    public void testGetSearchedEvents0() {
        expectedEvents = eventList;
        assertEquals("fetch all events", expectedEvents, tester.GetSearchedEvents(eventList, "", "all", "1/1/2000", "3/1/2023"));
    }

    @Test
    public void testGetSearchedEvents1() {
        expectedEvents.add(eventList.get(0));
        expectedEvents.add(eventList.get(3));
        assertEquals("fetch all events of type party", expectedEvents, tester.GetSearchedEvents(eventList, "", "party", "1/1/2000", "3/1/2023"));
    }

    @Test
    public void testGetSearchedEvents2() {
        expectedEvents.add(eventList.get(1));
        expectedEvents.add(eventList.get(2));
        assertEquals("fetch all events of type concert", expectedEvents, tester.GetSearchedEvents(eventList, "", "concert", "1/1/2000", "3/1/2023"));
    }

    @Test
    public void testGetSearchedEvents3() {
        expectedEvents.add(eventList.get(4));
        assertEquals("fetch all events of type sport", expectedEvents, tester.GetSearchedEvents(eventList, "", "sport", "1/1/2000", "3/1/2023"));
    }

    @Test
    public void testGetSearchedEvents4() {
        expectedEvents.add(eventList.get(0));
        expectedEvents.add(eventList.get(2));
        expectedEvents.add(eventList.get(3));
        assertEquals("fetch all events on 1/1/2023", expectedEvents, tester.GetSearchedEvents(eventList, "", "all", "1/1/2023", "1/1/2023"));
    }

    @Test
    public void testGetSearchedEvents5() {
        expectedEvents.add(eventList.get(0));
        expectedEvents.add(eventList.get(2));
        expectedEvents.add(eventList.get(3));
        expectedEvents.add(eventList.get(4));
        assertEquals("fetch all events within date range 1/1/2023 to 1/3/2023", expectedEvents, tester.GetSearchedEvents(eventList, "", "all", "1/1/2023", "1/3/2023"));
    }

    @Test
    public void testGetSearchedEvents6() {
        expectedEvents.add(eventList.get(3));
        expectedEvents.add(eventList.get(4));
        assertEquals("fetch all events w/ 'USC' keywords", expectedEvents, tester.GetSearchedEvents(eventList, "USC", "all", "1/1/2000", "1/3/2024"));
    }

    @Test
    public void testGetSearchedEvents7() {
        expectedEvents.add(eventList.get(0));
        expectedEvents.add(eventList.get(2));
        expectedEvents.add(eventList.get(3));
        expectedEvents.add(eventList.get(4));
        assertEquals("fetch all events held in Los Angeles", expectedEvents, tester.GetSearchedEvents(eventList, "Los Angeles", "all", "1/1/2000", "1/3/2024"));
    }

    @Test
    public void testGetSearchedEvents8() {
        expectedEvents.add(eventList.get(4));
        assertEquals("fetch all events related to UCLA (expecting null)", expectedEvents, tester.GetSearchedEvents(eventList, "UCLA", "all", "1/1/2000", "1/3/2024"));
    }

    @Test
    public void testGetSearchedEvents9() {
        expectedEvents.add(eventList.get(1));
        assertEquals("fetch all events related to Vista and of type concert", expectedEvents, tester.GetSearchedEvents(eventList, "Vista", "concert", "1/1/2000", "1/3/2024"));
    }

    @Test
    public void testGetSearchedEvents10() {
        expectedEvents = eventList;
        assertNotEquals("expect filtered value", expectedEvents, tester.GetSearchedEvents(eventList, "helloworld", "all", "1/1/2000", "1/3/2024"));
    }

    @Test
    public void testGetSearchedEvents11() {
        expectedEvents.add(eventList.get(0));
        expectedEvents.add(eventList.get(1));
        assertNotEquals("fetch all sport events related to USC", expectedEvents, tester.GetSearchedEvents(eventList, "USC", "sport", "1/1/2000", "1/3/2024"));
    }

}