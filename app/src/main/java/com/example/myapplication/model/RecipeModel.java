package com.example.myapplication.model;

public class RecipeModel {

    private int rec_id;
    private String rec_name;
    private String rec_img;
    private String rec_ingredients;
    private String rec_steps;
    private String rec_cat;
    private String rec_subcat;
    private int bgColor;

    public RecipeModel(int rec_id, String rec_name, String rec_img, String rec_ingredients, String rec_steps, String rec_cat, String rec_subcat, int bgColor) {
        this.rec_id = rec_id;
        this.rec_name = rec_name;
        this.rec_img = rec_img;
        this.rec_ingredients = rec_ingredients;
        this.rec_steps = rec_steps;
        this.rec_cat = rec_cat;
        this.rec_subcat = rec_subcat;
        this.bgColor = bgColor;
    }

    public int getRec_id() {
        return rec_id;
    }

    public void setRec_id(int rec_id) {
        this.rec_id = rec_id;
    }

    public String getRec_name() {
        return rec_name;
    }

    public void setRec_name(String rec_name) {
        this.rec_name = rec_name;
    }

    public String getRec_img() {
        return rec_img;
    }

    public void setRec_img(String rec_img) {
        this.rec_img = rec_img;
    }

    public String getRec_ingredients() {
        return rec_ingredients;
    }

    public void setRec_ingredients(String rec_ingredients) {
        this.rec_ingredients = rec_ingredients;
    }

    public String getRec_steps() {
        return rec_steps;
    }

    public void setRec_steps(String rec_steps) {
        this.rec_steps = rec_steps;
    }

    public String getRec_cat() {
        return rec_cat;
    }

    public void setRec_cat(String rec_cat) {
        this.rec_cat = rec_cat;
    }

    public String getRec_subcat() {
        return rec_subcat;
    }

    public void setRec_subcat(String rec_subcat) {
        this.rec_subcat = rec_subcat;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

}
