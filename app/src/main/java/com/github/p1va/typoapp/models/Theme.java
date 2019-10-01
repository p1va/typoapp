package com.github.p1va.typoapp.models;

import android.graphics.Color;

/**
 * Created by Stefano Piva on 27/07/2018.
 */
public class Theme implements java.io.Serializable {

    public String name;

    public String miniatureText;

    public String font;

    public int fontSize;

    public String fontColorHex;

    //public int fontColor;

    public String backgroundColorHex;

    //public int backgroundColor;

    public float letterSpacing = 0.15f;

    public float lineSpacing = 0;

    public String horizontalAlignment = "center";

    public String verticalAlignment = "center";

    public Theme(){

    }

    public Theme(String name, String font, int fontSize, int fontColor, int bgColor, String miniatureText) {
        this.miniatureText = miniatureText;
        this.name = name;
        this.font = font;
        this.fontSize = fontSize;
        //this.fontColor = fontColor;
        //this.backgroundColor = bgColor;
    }

    public int getFontColor(){
        return Color.parseColor(fontColorHex);
    }

    public int getBackgroundColor(){
        return Color.parseColor(backgroundColorHex);
    }
}
