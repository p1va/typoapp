package com.github.p1va.quickbrownfox;

/**
 * Created by Stefano Piva on 27/07/2018.
 */
public class Theme {
    String font;
    int fontColor;
    int fontSize;
    int backgroundColor;
    String name;

    Theme(String name, String font, int fontSize, int fontColor, int bgColor){
        this.name = name;
        this.font = font;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        this.backgroundColor = bgColor;
    }
}
