package com.fincons.utility;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public abstract class TitleOrDescriptionValidator {

    private static final String TITLE_REGEX = "^(?=.*[\\p{Alnum}])(?=.*[\\p{Punct}])(?!.*\\s)[\\p{Alnum}\\p{Punct} ]{1,255}$";



    private static final String DESCRIPTION_REGEX = "^(?=.*[\\p{Alnum}])(?=.*[\\p{Punct}])(?!.*\\s)[\\p{Alnum}\\p{Punct} ]{1,5000}$";


    public static boolean isValidTitle(String title) {
        return Pattern.matches(TITLE_REGEX, title);
    }

    public static boolean isValidDescription(String description) {
        return Pattern.matches(DESCRIPTION_REGEX, description);
    }



}
