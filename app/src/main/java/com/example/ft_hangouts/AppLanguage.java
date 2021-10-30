package com.example.ft_hangouts;

public class AppLanguage {
    public static String [] languagesMapArray = new String[] {"en", "fr"};
    public static  int currentLanguage = 0;

    public static String getCurrentLanguage(){
        return  languagesMapArray[currentLanguage];
    }
}
