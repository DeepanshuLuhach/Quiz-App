package com.deepanshu.quiz;

class ListItem {

    private String Qbid;
    private String Qbname;

    ListItem(String Qbid, String Qbname) {
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
