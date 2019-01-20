package me.p4tr7k.cookingapplication;

public class Recipes {

    private String title;
    private String imgurl;

    public Recipes(String imgurl, String title){
        this.title = title;
        this.imgurl = imgurl;
    }

    public String getTitle() {
        return title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
