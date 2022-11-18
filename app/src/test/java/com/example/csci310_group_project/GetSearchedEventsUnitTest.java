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
    private ArrayList<Event> eventList, searchedEvents;

    @Before
    public void setup() {
        tester = new BasicFragment();

        // set 6 hard-coded events
        // String eventName, String eventType, String eventDate, String eventOrganizor, String eventDescription, String eventLocation, Integer eventCost, int img, boolean registered,
        //                 Boolean favorite, double lat, double lng
        Event e0 = new Event("ThriftCon LA", "Party", "1/1/2023", "ThriftCon", "The #1 Vintage Clothing and Collectible Convention",
                "Los Angeles Convention Center, 1201 S Figueroa St, Los Angeles, CA 90015", 5, 0, false, false, 34.040569, -118.268761);
        Event e1 = new Event("Sunset Vibes Silent Disco", "Concert", "12/4/2022", "Vista", "A silent disco festival",
                "Vista, 11 Pier Ave, Harmosa Beach, CA 90254", 10, 0, false, false, 33.862350, -118.401650);
        Event e2 = new Event("Anime Pasadena", "Party", "1/1/2023", "ThriftCon", "The #1 Vintage Clothing and Collectible Convention",
                "Los Angeles Convention Center, 1201 S Figueroa St, Los Angeles, CA 90015", 5, 0, false, false, 34.040569, -118.268761);

        Event e3 = new Event("ThriftCon LA", "Party", "1/1/2023", "ThriftCon", "The #1 Vintage Clothing and Collectible Convention",
                "Los Angeles Convention Center, 1201 S Figueroa St, Los Angeles, CA 90015", 5, 0, false, false, 34.040569, -118.268761);
        Event e4 = new Event("ThriftCon LA", "Party", "1/1/2023", "ThriftCon", "The #1 Vintage Clothing and Collectible Convention",
                "Los Angeles Convention Center, 1201 S Figueroa St, Los Angeles, CA 90015", 5, 0, false, false, 34.040569, -118.268761);
        Event e5 = new Event("ThriftCon LA", "Party", "1/1/2023", "ThriftCon", "The #1 Vintage Clothing and Collectible Convention",
                "Los Angeles Convention Center, 1201 S Figueroa St, Los Angeles, CA 90015", 5, 0, false, false, 34.040569, -118.268761);

    }

    @Test
    public void testGetSearchedEvents0() {
        assertEquals("convert date components to a string ", "1/1/2000", tester.makeDateString(1, 1, 2000));
    }

}