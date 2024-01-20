package com.example.echat.ui.theme.Utills;


public class comment_model {
    private String single_cmtProImg, single_cmtUserName,single_cmtView;

    public comment_model() {
    }

    public comment_model(String single_cmtProImg, String single_cmtUserName, String single_cmtView) {
        this.single_cmtProImg = single_cmtProImg;
        this.single_cmtUserName = single_cmtUserName;
        this.single_cmtView = single_cmtView;
    }

    public String getSingle_cmtProImg() {
        return single_cmtProImg;
    }

    public void setSingle_cmtProImg(String single_cmtProImg) {
        this.single_cmtProImg = single_cmtProImg;
    }

    public String getSingle_cmtUserName() {
        return single_cmtUserName;
    }

    public void setSingle_cmtUserName(String single_cmtUserName) {
        this.single_cmtUserName = single_cmtUserName;
    }

    public String getSingle_cmtView() {
        return single_cmtView;
    }

    public void setSingle_cmtView(String single_cmtView) {
        this.single_cmtView = single_cmtView;
    }
}
