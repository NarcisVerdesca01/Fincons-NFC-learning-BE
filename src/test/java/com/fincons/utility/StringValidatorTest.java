package com.fincons.utility;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class StringValidatorTest {

    @Test
    public void testValidTitle() {
        String validTitle = "Java Programming";
        assertTrue(TitleOrDescriptionValidator.isValidTitle(validTitle));
    }

    @Test
    public void testInvalidTitle() {
        StringBuilder invalidTitleBuilder = new StringBuilder();
        for (int i = 0; i < 258; i++) {
            invalidTitleBuilder.append("a");
        }
        String invalidTitle = invalidTitleBuilder.toString();
        assertFalse(TitleOrDescriptionValidator.isValidTitle(invalidTitle));
    }

    @Test
    public void testValidDescription() {
        String validDescription = "This is a valid description!";
        assertTrue(TitleOrDescriptionValidator.isValidDescription(validDescription));
    }

    @Test
    public void testInvalidDescription() {
        StringBuilder invalidDescriptionBuilder = new StringBuilder();
        for (int i = 0; i < 5100; i++) {
            invalidDescriptionBuilder.append("a");
        }
        String invalidDescription = invalidDescriptionBuilder.toString();
        assertFalse(TitleOrDescriptionValidator.isValidDescription(invalidDescription));
    }
}
