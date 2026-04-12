package model_layer;

import java.time.LocalDate;

public class payment {
    private int paymentID;
    private double amount;
    private LocalDate paymentDate;
    private int payID;

    public payment() {
    }

    public payment(int paymentID, double amount, LocalDate paymentDate, int payID) {
        this.paymentID = paymentID;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.payID = payID;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getPayID() {
        return payID;
    }

    public void setPayID(int payID) {
        this.payID = payID;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentID=" + paymentID +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", payID=" + payID +
                '}';
    }
}