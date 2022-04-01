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
public class PasswordValidationTest {


    @Before
    public void setUp() {
    }

    @Test
    public void testPasswordValidation_Correct_returnTrue() {
        assertTrue(ValidatorUtil.validatePassword("abcdef"));
    }

    @Test
    public void testPasswordValidation_LengthGreaterThan5_returnFalse() {
        assertFalse(ValidatorUtil.validatePassword("a"));
        assertFalse(ValidatorUtil.validatePassword("ab"));
        assertFalse(ValidatorUtil.validatePassword("abc"));
        assertFalse(ValidatorUtil.validatePassword("abcd"));
        assertFalse(ValidatorUtil.validatePassword("abcde"));
    }

    @Test
    public void testPasswordValidation_EmptyInput_returnFalse() {
        assertFalse(ValidatorUtil.validatePassword(""));
    }

    @Test
    public void testPasswordValidation_NullInput_returnFalse() {
        assertFalse(ValidatorUtil.validatePassword(null));
    }

    @After
    public void tearDown() {
        //
    }
}
