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
public class EmailValidationTest {


    @Before
    public void setUp() {
        //
    }

    @Test
    public void testEmailValidation_CorrectEmail_returnTrue() {
        assertTrue(ValidatorUtil.validateEmail("a@b.ca"));
    }

    @Test
    public void testEmailValidation_WithSubdomain_returnTrue() {
        //With subdomain
        assertTrue(ValidatorUtil.validateEmail("a@b.ac.ca"));
    }

    @Test
    public void testEmailValidation_WithoutDomain_returnFalse() {
        assertFalse(ValidatorUtil.validateEmail("a@.com"));
    }

    @Test
    public void testEmailValidation_WithExtraCharacters_returnFalse() {
       assertFalse(ValidatorUtil.validateEmail("a@/b.com"));
        assertFalse(ValidatorUtil.validateEmail("a@b.co/m"));
      assertFalse(ValidatorUtil.validateEmail("/a@b.com"));
    }

    @Test
    public void testEmailValidation_WithoutUsername_returnFalse() {
        assertFalse(ValidatorUtil.validateEmail("@b.com"));
    }

    @Test
    public void testEmailValidation_WithoutAtSign_returnFalse() {
        assertFalse(ValidatorUtil.validateEmail("ab.com"));
    }

    @Test
    public void testEmailValidation_WithoutTopLevelDomain_returnFalse() {
        assertFalse(ValidatorUtil.validateEmail("a@b."));
    }

    @Test
    public void testEmailValidation_WithoutDot_returnFalse() {
        assertFalse(ValidatorUtil.validateEmail("a@bcom"));
    }

    @Test
    public void testEmailValidation_EmptyInput_returnFalse() {
        assertFalse(ValidatorUtil.validateEmail(""));
    }

    @Test
    public void testEmailValidation_NullInput_returnFalse() {
        assertFalse(ValidatorUtil.validateEmail(null));
    }

    @After
    public void tearDown() {
        //
    }
}