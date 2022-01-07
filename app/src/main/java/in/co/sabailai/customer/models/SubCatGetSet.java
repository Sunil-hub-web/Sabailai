package in.co.sabailai.customer.models;

public class SubCatGetSet {

    String id, name;


    public SubCatGetSet(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public SubCatGetSet setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SubCatGetSet setName(String name) {
        this.name = name;
        return this;
    }
}
