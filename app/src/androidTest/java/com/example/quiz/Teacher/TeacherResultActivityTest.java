package com.example.quiz.Teacher;

import androidx.test.rule.ActivityTestRule;

import com.example.quiz.R;
import com.jjoe64.graphview.GraphView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TeacherResultActivityTest {

    @Rule
    public ActivityTestRule<TeacherResultActivity> mActivityTestRule=new ActivityTestRule<TeacherResultActivity>(TeacherResultActivity.class);

    private TeacherResultActivity mActivity=null;

    @Before
    public void setUp() throws Exception {
        mActivity=mActivityTestRule.getActivity();
    }

    @Test
    public void TestLaunch()
    {
        GraphView view=mActivity.findViewById(R.id.graph1_1);
        GraphView view2=mActivity.findViewById(R.id.graph1_2);

        assertNotNull(view);
        assertNotNull(view2);
    }

    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }
}