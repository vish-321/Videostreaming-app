package com.example.navigationbar;



import java.util.Date;

public class noticeclass {

    private String id;
    private String ntext;
    private String heading;

    private long nmessageTime;

    public noticeclass() {
    }

    public noticeclass(String text, String heading) {
        this.ntext = text;
        this.heading = heading;

        nmessageTime = new Date().getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setnText(String text) {
        this.ntext = text;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading =heading;
    }



    public String getnText() {
        return ntext;
    }



    public long getnMessageTime() {
        return nmessageTime;
    }

    public void setnMessageTime(long nmessageTime) {
        this.nmessageTime = nmessageTime;
    }
}


