package com.uottawa.emilybonneville.seg2105_project;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import com.uottawa.emilybonneville.seg2105_project.utils.ValidatorUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DateValidationTest {


    @Before
    public void setUp() {
    }

    @Test
    public void testDateValidation_Correct_returnTrue() {
        assertTrue(ValidatorUtil.validateDateFormat("1997/03/31"));
    }

    @Test
    public void testDateValidation_InvalidYear_returnFalse() {
        assertFalse(ValidatorUtil.validateDateFormat("19979/03/31"));
    }

    @Test
    public void testDateValidation_InvalidMonth_returnFalse() {
        assertFalse(ValidatorUtil.validateDateFormat("19979/13/31"));
    }


    @Test
    public void testDateValidation_DashFormat_returnFalse() {
        assertFalse(ValidatorUtil.validateDateFormat("1997-03-31"));
    }


    @Test
    public void testDateValidation_EmptyInput_returnFalse() {
        assertFalse(ValidatorUtil.validateDateFormat(""));
    }

    @Test
    public void testDateValidation_NullInput_returnFalse() {
        assertFalse(ValidatorUtil.validateDateFormat(null));
    }

    @After
    public void tearDown() {
        //
    }
}
