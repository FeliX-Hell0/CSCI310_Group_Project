package com.example.csci310_group_project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.example.csci310_group_project.fragment.BasicFragment;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class GenerateEventCollectionUnitTest {
    @Test
    public void test1(){
        assertEquals(";Event A", EventDetailActivity.generateNewEventString(";Event A;Event B", "Event B"));
    }
    @Test
    public void test2(){
        assertEquals(";Event B", EventDetailActivity.generateNewEventString(";Event A;Event B", "Event A"));
    }
    @Test
    public void test3(){
        assertEquals(";Event B;Event C", EventDetailActivity.generateNewEventString(";Event B;Event A;Event C", "Event A"));
    }
}
