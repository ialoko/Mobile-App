package com.uottawa.emilybonneville.seg2105_project;

import static junit.framework.TestCase.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class BranchHoursTest {

    private BranchHours activity;


    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(BranchHours.class).create().resume().get();
    }

    @Test
    public void testBranchHours_Correct_returnTrue() {
        activity.monday.setChecked(false);
        activity.tuesday.setChecked(false);
        activity.wednesday.setChecked(false);
        activity.thursday.setChecked(false);
        activity.friday.setChecked(false);
        assertTrue(activity.validHours());

        activity.monday.setChecked(true);
        activity.tuesday.setChecked(true);
        activity.wednesday.setChecked(true);
        activity.thursday.setChecked(true);
        activity.friday.setChecked(true);
        assertTrue(activity.validHours());
    }


    @After
    public void tearDown() {
        //
    }
}
