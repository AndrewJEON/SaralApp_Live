package com.cgp.saral.model;

/**
 * Created by karamjeetsingh on 02/09/15.
 */
public class DrawerItem {
    String title;

    public DrawerItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    int icon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
