package com.example.csci310_group_project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.example.csci310_group_project.fragment.BasicFragment;
import com.example.csci310_group_project.ui.login.LoginViewModel;

import org.junit.Before;
import org.junit.Test;
import android.util.Patterns;

public class IsUsernameValidUnitTest {
    @Test
    public void test1(){
        assertEquals(true, LoginViewModel.isUserNameValid("usc@usc.edu"));
    }
    @Test
    public void test2(){
        assertEquals(true, LoginViewModel.isUserNameValid("usc123@usc.edu"));
    }
    @Test
    public void test3(){
        assertEquals(false, LoginViewModel.isUserNameValid("usc123usc.edu"));
    }
    @Test
    public void test4(){
        assertEquals(false, LoginViewModel.isUserNameValid("usc123@uscedu"));
    }
    @Test
    public void test5(){
        assertEquals(false, LoginViewModel.isUserNameValid("usc123??@usc.edu"));
    }
}
