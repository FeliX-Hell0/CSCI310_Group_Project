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

public class SortEventsUnitTest {

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
        Event e0 = new Event("ThriftCon LA", "Party", "1/1/2023, 12:00:00", "ThriftCon", "The #1 Vintage Clothing and Collectible Convention",
                "Los Angeles Convention Center, 1201 S Figueroa St, Los Angeles, CA 90015", 5, 0, false, false, 34.040569, -118.268761);
        Event e1 = new Event("Sunset Vibes Silent Disco", "Concert", "12/4/2022, 10:30:00", "Vista", "A silent disco festival",
                "Vista, 11 Pier Ave, Harmosa Beach, CA 90254", 10, 0, false, false, 33.862350, -118.401650);
        Event e2 = new Event("Anime Pasadena", "Concert", "1/1/2023, 15:30:00", "ThriftCon", "The #1 Vintage Clothing and Collectible Convention",
                "Los Angeles Convention Center, 1201 S Figueroa St, Los Angeles, CA 90015", 5, 0, false, false, 34.040569, -118.268761);
        Event e3 = new Event("Beat the Bruins", "Party", "1/1/2023, 11:30:00", "USC", "Pre-game Festival",
                "University of Southern California, 3620 S Vermont Ave, Los Angeles, CA", 0, 0, false, false, 34.022260, -118.291420);
        Event e4 = new Event("USC vs UCLA Football Game", "Sport", "1/3/2023, 10:30:00", "NCAA", "Must-seen game between famous USC-UCLA rivalry",
                "Los Angeles Convention Center, 1201 S Figueroa St, Los Angeles, CA 90015", 100, 0, false, false, 34.040569, -118.268761);

        e0.setDistanceToUser(0.1);
        e1.setDistanceToUser(0.0);
        e2.setDistanceToUser(10.9);
        e3.setDistanceToUser(8.5);
        e4.setDistanceToUser(100);

        eventList.add(e0);
        eventList.add(e1);
        eventList.add(e2);
        eventList.add(e3);
        eventList.add(e4);
    }

    @Test
    public void testGetSearchedEventsByCost() {
        expectedEvents.add(eventList.get(3));
        expectedEvents.add(eventList.get(0));
        expectedEvents.add(eventList.get(2));
        expectedEvents.add(eventList.get(1));
        expectedEvents.add(eventList.get(4));

        assertEquals("sort events via cost", expectedEvents, tester.SortEvents(eventList, "cost"));
    }

    @Test
    public void testGetSearchedEventsByDistance() {
        expectedEvents.add(eventList.get(1));
        expectedEvents.add(eventList.get(0));
        expectedEvents.add(eventList.get(3));
        expectedEvents.add(eventList.get(2));
        expectedEvents.add(eventList.get(4));

        assertEquals("sort events via distance", expectedEvents, tester.SortEvents(eventList, "distance"));
    }

    @Test
    public void testGetSearchedEventsByTime() {
        expectedEvents.add(eventList.get(1));
        expectedEvents.add(eventList.get(3));
        expectedEvents.add(eventList.get(0));
        expectedEvents.add(eventList.get(2));
        expectedEvents.add(eventList.get(4));

        assertEquals("sort events via time", expectedEvents, tester.SortEvents(eventList, "time"));
    }

    @Test
    public void testGetSearchedEventsByAlphabet() {
        expectedEvents.add(eventList.get(2));
        expectedEvents.add(eventList.get(3));
        expectedEvents.add(eventList.get(1));
        expectedEvents.add(eventList.get(0));
        expectedEvents.add(eventList.get(4));

        assertEquals("sort events via alphabetic order", expectedEvents, tester.SortEvents(eventList, "alphabetic"));
    }

}