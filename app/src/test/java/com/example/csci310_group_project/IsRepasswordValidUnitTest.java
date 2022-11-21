package com.example.csci310_group_project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.example.csci310_group_project.fragment.BasicFragment;
import com.example.csci310_group_project.ui.register.LoginViewModel;

import org.junit.Before;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class IsRepasswordValidUnitTest {

    private LoginViewModel tester;

    @Before
    public void setup() {
        tester = new LoginViewModel();
    }

    @Test
    public void testIsPepasswordValid0() {
        assertTrue(tester.isRepasswordValid("123456", "123456"));
    }
    @Test
    public void testIsPepasswordValid1() {
        assertTrue(tester.isRepasswordValid("_*(1)^&23456", "_*(1)^&23456"));
    }
    @Test
    public void testIsPepasswordValid2() {
        assertTrue(tester.isRepasswordValid("wdhjegciybasfjh", "wdhjegciybasfjh"));
    }
    @Test
    public void testIsPepasswordValid3() {
        assertFalse(tester.isRepasswordValid("1234567", "123456"));
    }
    @Test
    public void testIsPepasswordValid4() {
        assertFalse(tester.isRepasswordValid("wfeereqdf", "12356reitfgk"));
    }
    @Test
    public void testIsPepasswordValid5() {
        assertFalse(tester.isRepasswordValid("98765231", "98665231"));
    }

    @Test
    public void testIsPepasswordValid6() {
        assertFalse(tester.isRepasswordValid("", "98665231"));
    }

    @Test
    public void testIsPepasswordValid7() {
        assertFalse(tester.isRepasswordValid("98765231", ""));
    }
}