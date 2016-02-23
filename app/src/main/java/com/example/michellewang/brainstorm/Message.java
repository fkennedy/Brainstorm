package com.example.michellewang.brainstorm;
import java.util.Date;


/**
 * Created by Victor on 2/21/2016.
 */



public class Message {
    private String mText;
    private String mSender;
    private Date mDate;

    public Date getDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmSender() {
        return mSender;
    }

    public void setmSender(String mSender) {
        this.mSender = mSender;
    }
}