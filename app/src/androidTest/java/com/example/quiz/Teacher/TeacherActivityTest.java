package com.example.quiz.Teacher;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import com.example.quiz.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class TeacherActivityTest {

    @Rule
    public ActivityTestRule<TeacherActivity> mActivityTestRule=new ActivityTestRule<TeacherActivity>(TeacherActivity.class);

    private TeacherActivity mActivity=null;

    @Before
    public void setUp() throws Exception {
        mActivity=mActivityTestRule.getActivity();
    }

    @Test
    public void TestLaunch()
    {
        View view=mActivity.findViewById(R.id.textWiewHoca);

        assertNotNull("hq58cnF3KdQVA3bLVKOkJQQHK223",view);

    }

    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }

}