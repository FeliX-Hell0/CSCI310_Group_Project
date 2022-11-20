package com.example.csci310_group_project;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProfileRedirectUserTest {
//    @Rule
//    public ActivityScenarioRule<ContentActivity> activityScenarioRule = new ActivityScenarioRule<ContentActivity>(ContentActivity.class);

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void testGuestRedirection(){
        onView(withText("YES")).inRoot(isDialog()).check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.explore_button)).perform(click());
        Intents.init();
        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }

        // go to the profile fragment (page)
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.nav_profile)).perform(click());
        onView(withId(R.id.explore_button)).check(matches(isDisplayed()));
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }
}
