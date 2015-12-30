package com.example.CollegeApp.others; // 24 Dec, 04:01 PM

public class Notice {

    public int ID;// unique ID notice
    public int short_desc_length;
    public String header;
    public String short_desc;// a set of predefined noof letters from the body
    public String body;

    public Notice(String header, String body){
        this.header = header;
        this.body = body;
        short_desc_length = 50;// default
        this.short_desc = this.body.substring(0, short_desc_length);
    }

    public Notice(String header, String body, int short_desc_length){
        this.header = header;
        this.body = body;
        this.short_desc_length = short_desc_length;// default
        this.short_desc = this.body.substring(0, this.short_desc_length);
    }

}
