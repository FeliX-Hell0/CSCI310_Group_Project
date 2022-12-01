package com.example.csci310_group_project;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.*;

import com.example.csci310_group_project.fragment.MapFragment;
import com.example.csci310_group_project.ui.login.LoginActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchExploreTestEspresso {
    public static final String loginEmail = "final2@usc.edu";
    public static final String loginPassword = "123456";

    @Rule
    public ActivityScenarioRule<MainActivity> rule= new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void test1() {
        onView(withText("YES")).inRoot(isDialog()).check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username)).perform(typeText(loginEmail));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.login)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.explore_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}

        Espresso.closeSoftKeyboard();
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(typeText("49th"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("49th Annual Dia de los Muertos Exhibition")).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerView)).check(matches(hasChildCount(1)));
    }

    @Test
    public void test2() {
        onView(withText("YES")).inRoot(isDialog()).check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username)).perform(typeText(loginEmail));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.login)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.explore_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}

        Espresso.closeSoftKeyboard();
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(typeText("1300"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("49th Annual Dia de los Muertos Exhibition")).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerView)).check(matches(hasChildCount(1)));
    }
}
