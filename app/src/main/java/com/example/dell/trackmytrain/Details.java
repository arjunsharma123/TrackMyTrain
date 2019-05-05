package com.example.dell.trackmytrain;

/**
 * Created by dell on 16-Jan-19.
 */

public class Details {
    private String tname;
    private String tnumber;
    private String fair;
    private String src;
    private String des;
    public String getTname() {
        return tname;
    }


    public String getTnumber() {
        return tnumber;
    }


    public String getFair() {
        return fair;
    }

    public String getSrc() {
        return src;
    }

    public String getDes() {
        return des;
    }
    public Details(String tname, String tnumber, String fair, String src, String des) {
        this.tname = tname;
        this.tnumber = tnumber;
        this.fair = fair;
        this.src = src;
        this.des = des;
    }


}
