package in.co.sabailai.customer.models;

import java.util.ArrayList;

public class ServiceItemGetSet {

    String service_id, service_title, description, total_no_of_rating, price_id, regular_price, discouted_price, image, ifaddedtocart, cartquantity;

    ArrayList<CustomServiceGetSet> customservarray;

    public ServiceItemGetSet(String service_id, String service_title, String description, String total_no_of_rating, String price_id, String regular_price, String discouted_price, String image, String ifaddedtocart, String cartquantity, ArrayList<CustomServiceGetSet> customservarray) {
        this.service_id = service_id;
        this.service_title = service_title;
        this.description = description;
        this.total_no_of_rating = total_no_of_rating;
        this.price_id = price_id;
        this.regular_price = regular_price;
        this.discouted_price = discouted_price;
        this.image = image;
        this.ifaddedtocart = ifaddedtocart;
        this.cartquantity = cartquantity;
        this.customservarray = customservarray;
    }


    public String getService_id() {
        return service_id;
    }

    public ServiceItemGetSet setService_id(String service_id) {
        this.service_id = service_id;
        return this;
    }

    public String getService_title() {
        return service_title;
    }

    public ServiceItemGetSet setService_title(String service_title) {
        this.service_title = service_title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ServiceItemGetSet setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTotal_no_of_rating() {
        return total_no_of_rating;
    }

    public ServiceItemGetSet setTotal_no_of_rating(String total_no_of_rating) {
        this.total_no_of_rating = total_no_of_rating;
        return this;
    }

    public String getPrice_id() {
        return price_id;
    }

    public ServiceItemGetSet setPrice_id(String price_id) {
        this.price_id = price_id;
        return this;
    }

    public String getRegular_price() {
        return regular_price;
    }

    public ServiceItemGetSet setRegular_price(String regular_price) {
        this.regular_price = regular_price;
        return this;
    }

    public String getDiscouted_price() {
        return discouted_price;
    }

    public ServiceItemGetSet setDiscouted_price(String discouted_price) {
        this.discouted_price = discouted_price;
        return this;
    }

    public String getImage() {
        return image;
    }

    public ServiceItemGetSet setImage(String image) {
        this.image = image;
        return this;
    }


    public ArrayList<CustomServiceGetSet> getCustomservarray() {
        return customservarray;
    }

    public ServiceItemGetSet setCustomservarray(ArrayList<CustomServiceGetSet> customservarray) {
        this.customservarray = customservarray;
        return this;
    }

    public String getIfaddedtocart() {
        return ifaddedtocart;
    }

    public ServiceItemGetSet setIfaddedtocart(String ifaddedtocart) {
        this.ifaddedtocart = ifaddedtocart;
        return this;
    }

    public String getCartquantity() {
        return cartquantity;
    }

    public ServiceItemGetSet setCartquantity(String cartquantity) {
        this.cartquantity = cartquantity;
        return this;
    }
}
