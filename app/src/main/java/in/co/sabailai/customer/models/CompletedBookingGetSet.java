package in.co.sabailai.customer.models;

public class CompletedBookingGetSet {

    String vendor_id, order_id, booking_id, contact_person, contact_phon, area, address, aprts, Service_date, time_slot, payment, total_amount,
            discount, cupon, status, Service_title, service_qty, service_price;

    public CompletedBookingGetSet(String vendor_id, String order_id, String booking_id, String contact_person, String contact_phon, String area, String address, String aprts, String service_date, String time_slot, String payment, String total_amount, String discount, String cupon, String status, String service_title, String service_qty, String service_price) {
        this.vendor_id = vendor_id;
        this.order_id = order_id;
        this.booking_id = booking_id;
        this.contact_person = contact_person;
        this.contact_phon = contact_phon;
        this.area = area;
        this.address = address;
        this.aprts = aprts;
        Service_date = service_date;
        this.time_slot = time_slot;
        this.payment = payment;
        this.total_amount = total_amount;
        this.discount = discount;
        this.cupon = cupon;
        this.status = status;
        Service_title = service_title;
        this.service_qty = service_qty;
        this.service_price = service_price;
    }


    public String getService_title() {
        return Service_title;
    }

    public CompletedBookingGetSet setService_title(String service_title) {
        Service_title = service_title;
        return this;
    }

    public String getService_qty() {
        return service_qty;
    }

    public CompletedBookingGetSet setService_qty(String service_qty) {
        this.service_qty = service_qty;
        return this;
    }

    public String getService_price() {
        return service_price;
    }

    public CompletedBookingGetSet setService_price(String service_price) {
        this.service_price = service_price;
        return this;
    }

    public String getOrder_id() {
        return order_id;
    }

    public CompletedBookingGetSet setOrder_id(String order_id) {
        this.order_id = order_id;
        return this;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public CompletedBookingGetSet setBooking_id(String booking_id) {
        this.booking_id = booking_id;
        return this;
    }

    public String getContact_person() {
        return contact_person;
    }

    public CompletedBookingGetSet setContact_person(String contact_person) {
        this.contact_person = contact_person;
        return this;
    }

    public String getContact_phon() {
        return contact_phon;
    }

    public CompletedBookingGetSet setContact_phon(String contact_phon) {
        this.contact_phon = contact_phon;
        return this;
    }

    public String getArea() {
        return area;
    }

    public CompletedBookingGetSet setArea(String area) {
        this.area = area;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public CompletedBookingGetSet setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getAprts() {
        return aprts;
    }

    public CompletedBookingGetSet setAprts(String aprts) {
        this.aprts = aprts;
        return this;
    }

    public String getService_date() {
        return Service_date;
    }

    public CompletedBookingGetSet setService_date(String service_date) {
        Service_date = service_date;
        return this;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public CompletedBookingGetSet setTime_slot(String time_slot) {
        this.time_slot = time_slot;
        return this;
    }

    public String getPayment() {
        return payment;
    }

    public CompletedBookingGetSet setPayment(String payment) {
        this.payment = payment;
        return this;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public CompletedBookingGetSet setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
        return this;
    }

    public String getDiscount() {
        return discount;
    }

    public CompletedBookingGetSet setDiscount(String discount) {
        this.discount = discount;
        return this;
    }

    public String getCupon() {
        return cupon;
    }

    public CompletedBookingGetSet setCupon(String cupon) {
        this.cupon = cupon;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public CompletedBookingGetSet setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public CompletedBookingGetSet setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
        return this;
    }
}
