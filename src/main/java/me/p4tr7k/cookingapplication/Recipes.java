package me.p4tr7k.cookingapplication;

public class Recipes {

    private String title;
    private String imgurl;
    private String id;

    public Recipes(String imgurl, String title, String id){
        this.title = title;
        this.imgurl = imgurl;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getImgurl() { return imgurl; }

    public String getId() { return id; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setId(String id) { this.id = id; }

}
