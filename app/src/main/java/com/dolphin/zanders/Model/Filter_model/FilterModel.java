package com.dolphin.zanders.Model.Filter_model;

import java.util.ArrayList;

public class FilterModel {

    String label,name;
    private ArrayList<FilterInnerModel> Items;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<FilterInnerModel> getItems() {
        return Items;
    }

    public void setItems(ArrayList<FilterInnerModel> items) {
        Items = items;
    }
}
