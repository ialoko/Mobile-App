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
public class ReviewRatingTest {

    private ReviewBranch activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(ReviewBranch.class).create().resume().get();
    }

    @Test
    public void testReviewRating_SpecialInputs_returnTrue() {
        assertTrue(activity.validRating("0"));
        assertTrue(activity.validRating("1"));
        assertTrue(activity.validRating("2"));
        assertTrue(activity.validRating("3"));
        assertTrue(activity.validRating("4"));
        assertTrue(activity.validRating("5"));
    }

    @Test
    public void testReviewRating_SpecialInputs_returnFalse() {
        assertFalse(activity.validRating("-6"));
        assertFalse(activity.validRating("-5"));
        assertFalse(activity.validRating("-1"));
        assertFalse(activity.validRating("6"));
        assertFalse(activity.validRating("6"));
    }

    @Test
    public void testReviewRating_TwoCharacter_returnFalse() {
        assertFalse(activity.validRating("00"));
        assertFalse(activity.validRating("01"));
        assertFalse(activity.validRating("10"));
        assertFalse(activity.validRating("05"));
    }

    @Test
    public void testReviewRating_EmptyInput_returnFalse() {
        assertFalse(activity.validRating(""));
    }

    @Test
    public void testReviewRating_NullInput_returnFalse() {
        assertFalse(activity.validRating(null));
    }

    @After
    public void tearDown() {
        //
    }
}
