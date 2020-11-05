package me.sergiomartin.tvshowmovietracker.moviesModule.model;

public class Slide {

    private int image;
    private String title;
    // Add more field depending on what you want

    public Slide(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
