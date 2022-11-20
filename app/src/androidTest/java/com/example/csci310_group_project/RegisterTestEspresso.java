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
import com.example.csci310_group_project.ui.register.RegisterActivity;

import org.junit.Rule;
import org.junit.Test;

public class RegisterTestEspresso {
    public static final int emailPred = (int)(Math.random() * 1000000);
    public static final String loginEmail = String.valueOf(emailPred) + "usc@usc.edu";
    public static final String loginPassword = "123456";
    public static final String confirmPassword = "123456";
    public static final String nickname = "hi";
    public static final String repeatEmail = "final2@usc.edu";

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
        onView(withId(R.id.register_button)).perform(click());
        onView(withId(R.id.username)).perform(typeText(loginEmail));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.repassword)).perform(typeText(confirmPassword));
        Espresso.closeSoftKeyboard();
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.nickname)).perform(typeText(nickname));
        Espresso.closeSoftKeyboard();
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.login)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("Registration Success! Please login")).
                inRoot(RootMatchers.withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testRepeat() {
        rule.getScenario().onActivity(
                activity -> decorView = activity.getWindow().getDecorView()
        );
        onView(withText("YES")).inRoot(isDialog()).check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.register_button)).perform(click());
        onView(withId(R.id.username)).perform(typeText(repeatEmail));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.repassword)).perform(typeText(confirmPassword));
        Espresso.closeSoftKeyboard();
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.nickname)).perform(typeText(nickname));
        Espresso.closeSoftKeyboard();
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.login)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("Username already used!")).
                inRoot(RootMatchers.withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }
}
