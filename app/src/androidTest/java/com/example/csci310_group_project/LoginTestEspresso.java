package com.example.csci310_group_project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;


import android.content.Context;
import android.view.View;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import com.example.csci310_group_project.ui.login.LoginActivity;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTestEspresso {
    public static final String loginEmail = "usc@usc.edu";
    public static final String loginPassword = "123456";
    public static final String loginEmailWrong = "usc@usc.com";
    public static final String loginPasswordWrong = "111111";

    private View decorView;
    @Rule
    public ActivityScenarioRule<LoginActivity> rule= new ActivityScenarioRule<>(LoginActivity.class);
    @Before
    public void loadDecorView() {
        Intents.init();
        rule.getScenario().onActivity(
                activity -> decorView = activity.getWindow().getDecorView()
        );
    }
    @After
    public void release(){
        Intents.release();
    }

    /*@Test
    public void testCorrect() {
        onView(withId(R.id.username)).perform(typeText(loginEmail));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.login)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }*/

    @Test
    public void testEmailWrong() {
        onView(withId(R.id.username)).perform(typeText(loginEmailWrong));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.login)).perform(click());
        onView(withText("No such user")).
                inRoot(RootMatchers.withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testPasswordWrong() {
        onView(withId(R.id.username)).perform(typeText(loginEmail));
        onView(withId(R.id.password)).perform(typeText(loginPasswordWrong));
        onView(withId(R.id.login)).perform(click());
        onView(withText("Password error")).
                inRoot(RootMatchers.withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testBothWrong() {
        onView(withId(R.id.username)).perform(typeText(loginEmailWrong));
        onView(withId(R.id.password)).perform(typeText(loginPasswordWrong));
        onView(withId(R.id.login)).perform(click());
        onView(withText("No such user")).
                inRoot(RootMatchers.withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }
}
