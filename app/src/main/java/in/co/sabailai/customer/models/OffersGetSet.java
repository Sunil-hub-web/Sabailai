package in.co.sabailai.customer.models;

public class OffersGetSet {

    int img;
    String name;

    public OffersGetSet(int img, String name) {
        this.img = img;
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public OffersGetSet setImg(int img) {
        this.img = img;
        return this;
    }

    public String getName() {
        return name;
    }

    public OffersGetSet setName(String name) {
        this.name = name;
        return this;
    }
}
