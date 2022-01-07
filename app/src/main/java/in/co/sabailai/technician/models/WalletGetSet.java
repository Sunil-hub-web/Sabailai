package in.co.sabailai.technician.models;

public class WalletGetSet {

    String order_id, amount, date, contact_person;

    public WalletGetSet(String order_id, String amount, String date, String contact_person) {
        this.order_id = order_id;
        this.amount = amount;
        this.date = date;
        this.contact_person = contact_person;
    }

    public String getOrder_id() {
        return order_id;
    }

    public WalletGetSet setOrder_id(String order_id) {
        this.order_id = order_id;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public WalletGetSet setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getDate() {
        return date;
    }

    public WalletGetSet setDate(String date) {
        this.date = date;
        return this;
    }

    public String getContact_person() {
        return contact_person;
    }

    public WalletGetSet setContact_person(String contact_person) {
        this.contact_person = contact_person;
        return this;
    }
}
