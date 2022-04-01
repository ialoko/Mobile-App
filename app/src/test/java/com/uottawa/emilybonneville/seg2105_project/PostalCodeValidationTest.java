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
public class PostalCodeValidationTest {


    @Before
    public void setUp() {
    }

    @Test
    public void testPostalCodeValidation_InvalidCharacters_returnFalse() {
        for (int i = 0; i <= 126; i++) {
            if (i == 32 || i == 35 || i == 44 || i == 45 || i == 46) continue;//space  ' , . -
            if (i < 58 && i > 47) continue;//0-9
            if (i < 91 && i > 64) continue;//A-Z
            if (i < 123 && i > 96) continue;//a-z
            assertFalse(ValidatorUtil.validatePostalCode("abc" + (char) i));
        }
    }

    @Test
    public void testPostalCodeValidation_Correct_returnTrue() {
        assertTrue(ValidatorUtil.validatePostalCode("52.0744.306"));
    }

    @Test
    public void testPostalCodeValidation_Uppercase_returnTrue() {
        assertTrue(ValidatorUtil.validatePostalCode("ABCDE"));
    }

    @Test
    public void testPostalCodeValidation_Lowercase_returnTrue() {
        assertTrue(ValidatorUtil.validatePostalCode("abcde"));
    }

    @Test
    public void testPostalCodeValidation_minLength3_returnFalse() {
        assertFalse(ValidatorUtil.validatePostalCode("1"));
        assertFalse(ValidatorUtil.validatePostalCode("12"));
    }

    @Test
    public void testPostalCodeValidation_EmptyInput_returnFalse() {
        assertFalse(ValidatorUtil.validatePostalCode(""));
    }

    @Test
    public void testPostalCodeValidation_NullInput_returnFalse() {
        assertFalse(ValidatorUtil.validatePostalCode(null));
    }

    @After
    public void tearDown() {
        //
    }
}