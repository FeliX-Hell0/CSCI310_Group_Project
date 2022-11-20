package com.example.csci310_group_project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class SortEventTest {

    @Rule
    public ActivityScenarioRule<ContentActivity> activityScenarioRule = new ActivityScenarioRule<ContentActivity>(ContentActivity.class);


    @Before
    public void init() {
        onView(withId(R.id.nav_explore)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }
    }


    @Test
    public void testSortByDistance(){

        onView(withId(R.id.sort_spinner)).perform(click());
        onView(withText("Distance")).perform(click());

        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }

        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(0, R.id.custom_event_title))
                .check(matches(withText("Chocolate Sundaes Comedy @ The Laugh Factory Hollywood")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(1, R.id.custom_event_title))
                .check(matches(withText("Atheon Race")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(2, R.id.custom_event_title))
                .check(matches(withText("Jazz Eclectic Night")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(3, R.id.custom_event_title))
                .check(matches(withText("Haunt Massive")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(4, R.id.custom_event_title))
                .check(matches(withText("Joachim Horsley: Caribbean Nocturnes In Concert")));
    }

    @Test
    public void testSortByCost(){

        onView(withId(R.id.sort_spinner)).perform(click());
        onView(withText("Cost")).perform(click());

        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }

        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(0, R.id.custom_event_title))
                .check(matches(withText("49th Annual Dia de los Muertos Exhibition")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(1, R.id.custom_event_title))
                .check(matches(withText("Adaptive Sports Festival 2022")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(2, R.id.custom_event_title))
                .check(matches(withText("An Evening of World Music and Jazz")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(3, R.id.custom_event_title))
                .check(matches(withText("Atheon Race")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(4, R.id.custom_event_title))
                .check(matches(withText("MoreLuv: RnB")));
    }

    @Test
    public void testSortByTime(){

        onView(withId(R.id.sort_spinner)).perform(click());
        onView(withText("Time")).perform(click());

        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }

        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(0, R.id.custom_event_title))
                .check(matches(withText("Jazz Eclectic Night")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(1, R.id.custom_event_title))
                .check(matches(withText("Sunset Vibes Silent Disco")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(2, R.id.custom_event_title))
                .check(matches(withText("Haunt Massive")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(3, R.id.custom_event_title))
                .check(matches(withText("MoreLuv: RnB")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(4, R.id.custom_event_title))
                .check(matches(withText("Chocolate and Art Show")));
    }

    @Test
    public void testSortByAlphabet(){

        onView(withId(R.id.sort_spinner)).perform(click());
        onView(withText("Alphabetic")).perform(click());

        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }

        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(0, R.id.custom_event_title))
                .check(matches(withText("2023 DREAM All-American Bowl")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(1, R.id.custom_event_title))
                .check(matches(withText("49th Annual Dia de los Muertos Exhibition")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(2, R.id.custom_event_title))
                .check(matches(withText("Abbot Kidding: A Comedy Show in Venice")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(3, R.id.custom_event_title))
                .check(matches(withText("Adaptive Sports Festival 2022")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(4, R.id.custom_event_title))
                .check(matches(withText("AfroVibesLA Sunday")));
    }


}
