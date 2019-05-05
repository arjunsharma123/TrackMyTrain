package com.example.dell.trackmytrain;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterActivityTest {

    public ActivityTestRule<RegisterActivity> activityTestRule=new ActivityTestRule<RegisterActivity>(RegisterActivity.class);
    private RegisterActivity showActivity=null;


    @Before
    public void setUp() throws Exception {
        showActivity=activityTestRule.getActivity();
    }
    @Test
    public void testLauch()
    {
      assertTrue(RegisterActivity.check("name@gmail.com"));

    }

    @After
    public void tearDown() throws Exception {
        showActivity=null;
    }
}