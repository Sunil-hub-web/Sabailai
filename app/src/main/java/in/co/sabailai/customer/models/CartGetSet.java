package in.co.sabailai.customer.models;

public class CartGetSet {

    String service_id, service_name, service_qty, service_price;

    public CartGetSet(String service_id, String service_name, String service_qty, String service_price) {
        this.service_id = service_id;
        this.service_name = service_name;
        this.service_qty = service_qty;
        this.service_price = service_price;
    }

    public String getService_id() {
        return service_id;
    }

    public CartGetSet setService_id(String service_id) {
        this.service_id = service_id;
        return this;
    }

    public String getService_name() {
        return service_name;
    }

    public CartGetSet setService_name(String service_name) {
        this.service_name = service_name;
        return this;
    }

    public String getService_qty() {
        return service_qty;
    }

    public CartGetSet setService_qty(String service_qty) {
        this.service_qty = service_qty;
        return this;
    }

    public String getService_price() {
        return service_price;
    }

    public CartGetSet setService_price(String service_price) {
        this.service_price = service_price;
        return this;
    }
}
