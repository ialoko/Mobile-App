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
public class PickupBeforeReturnDateTest {

    private TruckRentalForm activity;


    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(TruckRentalForm.class).create().resume().get();
    }

    @Test
    public void testPickup_Correct_returnTrue() {
        assertTrue(activity.isPickupBeforeReturn("1997/03/31" , "00:00" , "1997/03/31" , "23:59"));
        assertTrue(activity.isPickupBeforeReturn("1997/03/31" , "00:00" , "1997/03/31" , "00:01"));
        assertTrue(activity.isPickupBeforeReturn("1997/03/31" , "23:59" , "1997/04/01" , "00:00"));
        assertTrue(activity.isPickupBeforeReturn("2021/06/13" , "00:00" , "2021/12/01" , "23:59"));
    }

    @Test
    public void testPickup_Incorrect_returnFalse() {
        assertFalse(activity.isPickupBeforeReturn("1997/03/31" , "23:59" , "1997/03/31" , "23:58"));
        assertFalse(activity.isPickupBeforeReturn("1997/03/31" , "23:59" , "1997/03/31" , "00:00"));
        assertFalse(activity.isPickupBeforeReturn("1998/03/31" , "23:59" , "1997/04/01" , "00:00"));
        assertFalse(activity.isPickupBeforeReturn("2021/06/13" , "00:00" , "2021/02/01" , "23:59"));
    }


    @After
    public void tearDown() {
        //
    }
}
