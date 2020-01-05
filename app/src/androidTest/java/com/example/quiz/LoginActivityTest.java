package com.example.quiz;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule=new ActivityTestRule<LoginActivity>(LoginActivity.class);

    private LoginActivity mActivity=null;

    @Before
    public void setUp() throws Exception {
        mActivity=mActivityTestRule.getActivity();
    }

    @Test
    public void TestLaunch()
    {
        View view=mActivity.findViewById(R.id.email);
        View view2=mActivity.findViewById(R.id.password);

        assertNotNull("murat@ogr.com",view);
        assertNotNull("muratogr",view2);
    }

    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }
}