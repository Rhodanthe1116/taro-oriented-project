package com.genomu.starttravel.ui.home;

public class GalleryPhoto {

    private String title;
    private int image;

    public GalleryPhoto(String title, int image){

        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
