package com.ibm.techathon.elven.smartpool.model.ibmdata;

/**
 * Created by meshriva on 9/3/2014.
 */
public class NavDrawerItem {

    private String title;
    private int icon;

    /**
     * default constructor
     */
    public NavDrawerItem(){
        super();
    }

    /**
     * parameterized constructor
     * @param title
     * @param icon
     */
    public NavDrawerItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

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
