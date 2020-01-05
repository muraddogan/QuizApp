package com.example.quiz.Teacher;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import com.example.quiz.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TeacherTestActivityTest {

    @Rule
    public ActivityTestRule<TeacherTestActivity> mActivityTestRule=new ActivityTestRule<TeacherTestActivity>(TeacherTestActivity.class);

    private TeacherTestActivity mActivity=null;

    @Before
    public void setUp() throws Exception {
        mActivity=mActivityTestRule.getActivity();
    }

    @Test
    public void TestLaunch()
    {
        View view=mActivity.findViewById(R.id.textViewKonuSayi);
        View view2=mActivity.findViewById(R.id.textViewKonuSayi);

        assertNotNull(view);
        assertNotNull(view2);
    }

    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }
}