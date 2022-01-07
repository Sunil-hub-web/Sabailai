package in.co.sabailai.customer.models;

public class CartOffersGetSet {

    String cupon_id, cupon_name, discount_value, minimum_cart_val, Cupon_status;


    public CartOffersGetSet(String cupon_id, String cupon_name, String discount_value, String minimum_cart_val, String cupon_status) {
        this.cupon_id = cupon_id;
        this.cupon_name = cupon_name;
        this.discount_value = discount_value;
        this.minimum_cart_val = minimum_cart_val;
        Cupon_status = cupon_status;
    }

    public String getCupon_id() {
        return cupon_id;
    }

    public CartOffersGetSet setCupon_id(String cupon_id) {
        this.cupon_id = cupon_id;
        return this;
    }

    public String getCupon_name() {
        return cupon_name;
    }

    public CartOffersGetSet setCupon_name(String cupon_name) {
        this.cupon_name = cupon_name;
        return this;
    }

    public String getDiscount_value() {
        return discount_value;
    }

    public CartOffersGetSet setDiscount_value(String discount_value) {
        this.discount_value = discount_value;
        return this;
    }

    public String getMinimum_cart_val() {
        return minimum_cart_val;
    }

    public CartOffersGetSet setMinimum_cart_val(String minimum_cart_val) {
        this.minimum_cart_val = minimum_cart_val;
        return this;
    }

    public String getCupon_status() {
        return Cupon_status;
    }

    public CartOffersGetSet setCupon_status(String cupon_status) {
        Cupon_status = cupon_status;
        return this;
    }
}
