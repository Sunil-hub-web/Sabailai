package in.co.sabailai.customer.models;

public class PaymentGetSet {

    String assignedto, service, orderid, amount, date;

    public PaymentGetSet(String assignedto, String service, String orderid, String amount, String date) {
        this.assignedto = assignedto;
        this.service = service;
        this.orderid = orderid;
        this.amount = amount;
        this.date = date;
    }

    public String getAssignedto() {
        return assignedto;
    }

    public PaymentGetSet setAssignedto(String assignedto) {
        this.assignedto = assignedto;
        return this;
    }

    public String getService() {
        return service;
    }

    public PaymentGetSet setService(String service) {
        this.service = service;
        return this;
    }

    public String getOrderid() {
        return orderid;
    }

    public PaymentGetSet setOrderid(String orderid) {
        this.orderid = orderid;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public PaymentGetSet setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getDate() {
        return date;
    }

    public PaymentGetSet setDate(String date) {
        this.date = date;
        return this;
    }
}
