package com.example.csci310_group_project;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.core.AllOf.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class FilterEventByTypeTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Before
    public void init() {
        onView(withText("YES")).inRoot(isDialog()).check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.explore_button)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (Exception e) {}

        onView(withId(R.id.nav_explore)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
    }

    @Test
    public void testGetTypeParty(){

        onView(withId(R.id.type_spinner)).perform(click());
        onView(withText("Party")).perform(click());

        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }

        // there are 5 party events
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(0, R.id.custom_event_type))
                .check(matches(withText("Party")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(1, R.id.custom_event_type))
                .check(matches(withText("Party")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(2, R.id.custom_event_type))
                .check(matches(withText("Party")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(3, R.id.custom_event_type))
                .check(matches(withText("Party")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(4, R.id.custom_event_type))
                .check(matches(withText("Party")));


    }

    @Test
    public void testGetTypeConcert(){

        onView(withId(R.id.type_spinner)).perform(click());
        onView(withText("Concert")).perform(click());

        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }

        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(0, R.id.custom_event_type))
                .check(matches(withText("Concert")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(1, R.id.custom_event_type))
                .check(matches(withText("Concert")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(2, R.id.custom_event_type))
                .check(matches(withText("Concert")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(3, R.id.custom_event_type))
                .check(matches(withText("Concert")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(4, R.id.custom_event_type))
                .check(matches(withText("Concert")));


    }

    @Test
    public void testGetTypeSport(){

        onView(withId(R.id.type_spinner)).perform(click());
        onView(withText("Sport")).perform(click());

        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }

        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(0, R.id.custom_event_type))
                .check(matches(withText("Sports")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(1, R.id.custom_event_type))
                .check(matches(withText("Sports")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(2, R.id.custom_event_type))
                .check(matches(withText("Sports")));
    }

    @Test
    public void testGetTypeExhibition(){

        onView(withId(R.id.type_spinner)).perform(click());
        onView(withText("Exhibition")).perform(click());

        try {
            Thread.sleep(4000);
        } catch (Exception e) {

        }

        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(0, R.id.custom_event_type))
                .check(matches(withText("Exhibition")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(1, R.id.custom_event_type))
                .check(matches(withText("Exhibition")));
    }

    @Test
    public void testGetTypeComedy(){

        onView(withId(R.id.type_spinner)).perform(click());
        onView(withText("Comedy")).perform(click());

        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }

        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(0, R.id.custom_event_type))
                .check(matches(withText("Comedy")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(1, R.id.custom_event_type))
                .check(matches(withText("Comedy")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(2, R.id.custom_event_type))
                .check(matches(withText("Comedy")));
        onView(new RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(3, R.id.custom_event_type))
                .check(matches(withText("Comedy")));
    }
}
