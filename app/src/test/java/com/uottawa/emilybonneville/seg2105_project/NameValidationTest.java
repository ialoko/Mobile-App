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
public class NameValidationTest {


    @Before
    public void setUp() {
    }

    @Test
    public void testNameValidation_LongInput_returnFalse() {
        assertFalse(ValidatorUtil.validateName("asdfghjkloiuytrewqzxcvbnmlpoq123456789"));
    }

    @Test
    public void testNameValidation_InvalidCharacters_returnFalse() {
        for (int i = 0; i <= 126; i++) {
            if (i == 39 || i == 44 || i == 45 || i == 46) continue;// ' , . -
            if (i < 58 && i > 47) continue;//0-9
            if (i < 91 && i > 64) continue;//A-Z
            if (i < 123 && i > 96) continue;//a-z
            assertFalse(ValidatorUtil.validateName("abcd" + (char) i));
        }
    }

    @Test
    public void testNameValidation_Uppercase_returnTrue() {
        assertTrue(ValidatorUtil.validateName("ABCDE"));
    }

    @Test
    public void testNameValidation_Lowercase_returnTrue() {
        assertTrue(ValidatorUtil.validateName("abcde"));
    }

    @Test
    public void testNameValidation_minLength3_returnFalse() {
        assertFalse(ValidatorUtil.validateName("a"));
        assertFalse(ValidatorUtil.validateName("ab"));
    }

    @Test
    public void testNameValidation_EmptyInput_returnFalse() {
        assertFalse(ValidatorUtil.validateName(""));
    }

    @Test
    public void testNameValidation_NullInput_returnFalse() {
        assertFalse(ValidatorUtil.validateName(null));
    }

    @After
    public void tearDown() {
        //
    }
}
