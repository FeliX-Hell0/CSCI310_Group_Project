package com.example.csci310_group_project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.not;

import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.csci310_group_project.ui.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;

public class LogoutTestEspresso {
    public static final String loginEmail = "final2@usc.edu";
    public static final String loginPassword = "123456";
    private View decorView;
    @Rule
    public ActivityScenarioRule<MainActivity> rule= new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void testCorrect() {
        rule.getScenario().onActivity(
                activity -> decorView = activity.getWindow().getDecorView()
        );
        onView(withText("YES")).inRoot(isDialog()).check(matches(isDisplayed()))
                .perform(click());
        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username)).perform(typeText(loginEmail));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.login)).perform(click());
        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.explore_button)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (Exception e) {}
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.nav_profile)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (Exception e) {}

        Intents.init();
        onView(withId(R.id.logout_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}
        // check if successfully redirected to main welcome page

        intended(hasComponent(MainActivity.class.getName()));

        onView(withText("CONTINUE AS GUEST")).
                inRoot(RootMatchers.withDecorView(not(decorView)))
                .check(matches(isDisplayed()));

        Intents.release();
    }
}
