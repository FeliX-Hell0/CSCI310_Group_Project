package com.example.csci310_group_project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.not;

import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class GuestRegTestEspresso {
    private View decorView;

    @Rule
    public ActivityScenarioRule<MainActivity> rule= new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void testCorrect() {
        rule.getScenario().onActivity(
                activity -> decorView = activity.getWindow().getDecorView()
        );
        onView(withText("YES")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.explore_button)).perform(click());
        Espresso.closeSoftKeyboard();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("49th Annual Dia de los Muertos Exhibition")).perform(click());
        onView(withId(R.id.custom_event_register_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("Please register to sign up for events")).
                inRoot(RootMatchers.withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }
}

