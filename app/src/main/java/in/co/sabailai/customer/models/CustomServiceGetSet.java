package in.co.sabailai.customer.models;

public class CustomServiceGetSet {

    String service_id, service_title, image, price, iftaken;

    public CustomServiceGetSet(String service_id, String service_title, String image, String price, String iftaken) {
        this.service_id = service_id;
        this.service_title = service_title;
        this.image = image;
        this.price = price;
        this.iftaken = iftaken;
    }


    public String getService_id() {
        return service_id;
    }

    public CustomServiceGetSet setService_id(String service_id) {
        this.service_id = service_id;
        return this;
    }

    public String getService_title() {
        return service_title;
    }

    public CustomServiceGetSet setService_title(String service_title) {
        this.service_title = service_title;
        return this;
    }

    public String getImage() {
        return image;
    }

    public CustomServiceGetSet setImage(String image) {
        this.image = image;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public CustomServiceGetSet setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getIftaken() {
        return iftaken;
    }

    public CustomServiceGetSet setIftaken(String iftaken) {
        this.iftaken = iftaken;
        return this;
    }
}
