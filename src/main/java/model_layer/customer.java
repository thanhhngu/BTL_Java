package model_layer;

import java.time.LocalDate;

public class customer {
    private int customerID;
    private String name;
    private String phone;
    private String gender;
    private LocalDate birthdate;

    public customer() {
    }

    public customer(int customerID, String name, String phone, String gender, LocalDate birthdate) {
        this.customerID = customerID;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.birthdate = birthdate;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID=" + customerID +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
}