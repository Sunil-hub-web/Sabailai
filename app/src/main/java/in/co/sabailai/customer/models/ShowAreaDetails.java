package in.co.sabailai.customer.models;

public class ShowAreaDetails {

    String areaNmae,areaId;

    public ShowAreaDetails(String areaNmae, String areaId) {
        this.areaNmae = areaNmae;
        this.areaId = areaId;
    }

    public String getAreaNmae() {
        return areaNmae;
    }

    public void setAreaNmae(String areaNmae) {
        this.areaNmae = areaNmae;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
