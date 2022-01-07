package in.co.sabailai.customer.models;

public class BookingsGetSet {

    String orderid, service, assignedto, date, time;

    public BookingsGetSet(String orderid, String service, String assignedto, String date, String time) {
        this.orderid = orderid;
        this.service = service;
        this.assignedto = assignedto;
        this.date = date;
        this.time = time;
    }

    public String getOrderid() {
        return orderid;
    }

    public BookingsGetSet setOrderid(String orderid) {
        this.orderid = orderid;
        return this;
    }

    public String getService() {
        return service;
    }

    public BookingsGetSet setService(String service) {
        this.service = service;
        return this;
    }

    public String getAssignedto() {
        return assignedto;
    }

    public BookingsGetSet setAssignedto(String assignedto) {
        this.assignedto = assignedto;
        return this;
    }

    public String getDate() {
        return date;
    }

    public BookingsGetSet setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTime() {
        return time;
    }

    public BookingsGetSet setTime(String time) {
        this.time = time;
        return this;
    }
}
