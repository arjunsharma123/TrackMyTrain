package com.example.dell.trackmytrain;

public class MyData {
    private String tname;
    private String tnumber;

    private String src;
    private String des;
    public String getTname() {
        return tname;
    }


    public String getTnumber() {
        return tnumber;
    }



    public String getSrc() {
        return src;
    }

    public String getDes() {
        return des;
    }
    public MyData(String tname, String tnumber, String src, String des) {
        this.tname = tname;
        this.tnumber = tnumber;

        this.src = src;
        this.des = des;
    }
}
