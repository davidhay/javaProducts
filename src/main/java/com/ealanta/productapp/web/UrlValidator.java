package com.ealanta.productapp.web;

import java.net.URL;

/**
 * This simple validator makes sure the URL for the remote web service is valid BEFORE we attempt to use the RestTemplate
 * with an invalid URL and get a strange error message.s
 */
public class UrlValidator {

    public static boolean isValid(String url) {
        try {
            new URL(url).toURI();
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
