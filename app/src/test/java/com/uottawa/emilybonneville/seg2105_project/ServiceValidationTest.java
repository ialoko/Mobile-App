package com.uottawa.emilybonneville.seg2105_project;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ServiceValidationTest {

    private SearchBranch activity;
    private String[] arrServices;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(SearchBranch.class).create().resume().get();
        arrServices = new String[]{"Car Rental", "Truck Rental", "Moving Assistance"};
    }

    @Test
    public void testSearchBranch_SpecialInputs_returnTrue() {
        for (String service : arrServices) {
            assertTrue(activity.validService(service));
        }
    }

    @Test
    public void testSearchBranch_EmptyInput_returnFalse() {
        assertFalse(activity.validService(""));
    }

    @Test
    public void testSearchBranch_NullInput_returnFalse() {
        assertFalse(activity.validService(null));
    }

    @After
    public void tearDown() {
        //
    }
}
