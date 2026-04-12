package model_layer;

public class methodPayment {
    private int payID;
    private String paymentMethod;

    public methodPayment() {
    }

    public methodPayment(int payID, String paymentMethod) {
        this.payID = payID;
        this.paymentMethod = paymentMethod;
    }

    public int getPayID() {
        return payID;
    }

    public void setPayID(int payID) {
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