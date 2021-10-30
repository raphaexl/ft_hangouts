package com.example.ft_hangouts;

public class AppThemeAndHeader {
    public static int [] colorsMapArray = new int[] {R.color.purple_500, R.color.red, R.color.green, R.color.blue, R.color.black, R.color.cyan};
    public static  int currentHeaderColor = 0;

    public static int getCurrentHeaderColor(){
        return colorsMapArray[currentHeaderColor];
    }
}
