package cn.iwenddg.animation.bean;

import android.graphics.drawable.Drawable;

import androidx.annotation.Keep;

/**
 * @author iwen大大怪
 * @create 2021/11/05 15:46
 */
public class MainTabTheme {

    public MainTabTheme.TabConfig tab1;
    public MainTabTheme.TabConfig tab2;
    public MainTabTheme.TabConfig tab3;
    public MainTabTheme.TabConfig tab4;
    public MainTabTheme.TabConfig tab5;

    public static class TabConfig {
        public String label;
        public Drawable icon;
        public Drawable activeIcon;
        public String activeAnimJson;
        public String extraAnimJson;
        public int extraAnimWidth;
        public int extraAnimHeight;
        public String color;
        public String activeColor;
    }
}

