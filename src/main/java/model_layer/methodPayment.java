package model_layer;

public class methodPayment {
    private String payID;
    private String paymentMethod;

    public methodPayment() {
    }

    public methodPayment(String payID, String paymentMethod) {
        this.payID = payID;
        this.paymentMethod = paymentMethod;
    }

    public String getPayID() {
        return payID;
    }

    public void setPayID(String payID) {
        this.payID = payID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "MethodPayment{" +
                "payID=" + payID +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}