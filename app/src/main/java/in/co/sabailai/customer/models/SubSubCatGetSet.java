package in.co.sabailai.customer.models;

import java.util.ArrayList;

public class SubSubCatGetSet {

    String sub_sub_category_id, sub_sub_category_name;
    ArrayList<ServiceItemGetSet> itemarray;

    public SubSubCatGetSet(String sub_sub_category_id, String sub_sub_category_name, ArrayList<ServiceItemGetSet> itemarray) {
        this.sub_sub_category_id = sub_sub_category_id;
        this.sub_sub_category_name = sub_sub_category_name;
        this.itemarray = itemarray;
    }

    public String getSub_sub_category_id() {
        return sub_sub_category_id;
    }

    public SubSubCatGetSet setSub_sub_category_id(String sub_sub_category_id) {
        this.sub_sub_category_id = sub_sub_category_id;
        return this;
    }

    public String getSub_sub_category_name() {
        return sub_sub_category_name;
    }

    public SubSubCatGetSet setSub_sub_category_name(String sub_sub_category_name) {
        this.sub_sub_category_name = sub_sub_category_name;
        return this;
    }

    public ArrayList<ServiceItemGetSet> getItemarray() {
        return itemarray;
    }

    public SubSubCatGetSet setItemarray(ArrayList<ServiceItemGetSet> itemarray) {
        this.itemarray = itemarray;
        return this;
    }
}
