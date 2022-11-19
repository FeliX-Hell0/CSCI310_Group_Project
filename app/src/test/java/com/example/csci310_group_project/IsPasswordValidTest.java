package com.example.csci310_group_project;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.csci310_group_project.ui.register.LoginViewModel;

import org.junit.Before;
import org.junit.Test;

public class IsPasswordValidTest {
    private LoginViewModel tester;
    @Before
    public void setup() {
        tester = new LoginViewModel();
    }

    @Test
    public void testIsPepasswordValid0() {
        assertTrue(tester.isPasswordValid("123456"));
    }
    @Test
    public void testIsPepasswordValid1() {
        assertTrue(tester.isPasswordValid("abcdef"));
    }
    @Test
    public void testIsPepasswordValid2() {
        assertTrue(tester.isPasswordValid("abc123"));
    }
    @Test
    public void testIsPepasswordValid3() {
        assertFalse(tester.isPasswordValid("abc12"));
    }
    @Test
    public void testIsPepasswordValid4() {
        assertFalse(tester.isPasswordValid(""));
    }
}
