package in.co.sabailai.customer.models;

public class ServiceGetSet {

    String id, name, image;

    public ServiceGetSet(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public ServiceGetSet setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ServiceGetSet setName(String name) {
        this.name = name;
        return this;
    }

    public String getImage() {
        return image;
    }

    public ServiceGetSet setImage(String image) {
        this.image = image;
        return this;
    }
}
