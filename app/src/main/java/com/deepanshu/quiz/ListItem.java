package com.deepanshu.quiz;

/**
 * Created by deepanshu on 27/7/17.
 */

public class ListItem {

    private String Qbid;
    private String Qbname;

    public ListItem(String Qbid, String Qbname) {
        this.Qbid = Qbid;
        this.Qbname = Qbname;
    }

    public String getQbid() {
        return Qbid;
    }

    public String getQbname() {
        return Qbname;
    }
}
