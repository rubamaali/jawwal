package com.lite.pits_jawwal.pitstracklite.Categories;

public class CategoryData {

    private String catName;
    private String catId;
    private PlaceData[] catPlaces;

    public CategoryData(String catName, String catId, PlaceData[] catPlaces) {
        this.catName = catName;
        this.catId = catId;
        this.catPlaces = catPlaces;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public PlaceData[] getCatPlaces() {
        return catPlaces;
    }

    public void setCatPlaces(PlaceData[] catPlaces) {
        this.catPlaces = catPlaces;
    }
}

