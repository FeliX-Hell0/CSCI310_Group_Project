package com.example.csci310_group_project;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.csci310_group_project.ui.register.RegisterActivity;

import org.junit.Rule;

public class RegisterTestEspresso {
    @Rule
    public ActivityScenarioRule<RegisterActivity> rule= new ActivityScenarioRule<>(RegisterActivity.class);
}
