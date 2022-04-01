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
public class NumberValidationTest {


    @Before
    public void setUp() {
    }

    @Test
    public void testNumberValidation_InvalidCharacters_returnFalse() {
        for (int i = 0; i <= 126; i++) {
            if (i < 58 && i > 47) continue;//0-9
            assertFalse(ValidatorUtil.validateNumber("0123456789" + (char) i));
        }
    }

    @Test
    public void testNumberValidation_Correct_returnTrue() {
        assertTrue(ValidatorUtil.validateNumber("0123456789"));
    }
    

    @Test
    public void testNumberValidation_minLength9_returnFalse() {
        assertFalse(ValidatorUtil.validateNumber("1"));
        assertFalse(ValidatorUtil.validateNumber("12"));
        assertFalse(ValidatorUtil.validateNumber("123"));
        assertFalse(ValidatorUtil.validateNumber("1234"));
        assertFalse(ValidatorUtil.validateNumber("12345"));
        assertFalse(ValidatorUtil.validateNumber("123456"));
        assertFalse(ValidatorUtil.validateNumber("1234567"));
        assertFalse(ValidatorUtil.validateNumber("12345678"));
        assertFalse(ValidatorUtil.validateNumber("12345678"));
    }

    @Test
    public void testNumberValidation_maxLength12_returnFalse() {
        assertFalse(ValidatorUtil.validateNumber("1234567891234"));
        assertFalse(ValidatorUtil.validateNumber("12345678912345"));
    }

    @Test
    public void testNumberValidation_EmptyInput_returnFalse() {
        assertFalse(ValidatorUtil.validateNumber(""));
    }

    @Test
    public void testNumberValidation_NullInput_returnFalse() {
        assertFalse(ValidatorUtil.validateNumber(null));
    }

    @After
    public void tearDown() {
        //
    }
}