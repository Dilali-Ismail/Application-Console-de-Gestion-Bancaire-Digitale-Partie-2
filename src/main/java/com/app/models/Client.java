package main.java.com.app.models;

import java.math.BigDecimal;
import java.time.Instant;

public class Client {
    private Long id;
    private String Name;
    private String email;
    private String phone;
    private String address;
    private BigDecimal monthlyIncome;
    private Instant createdAt;

    public Client(){

    }
    public Client(String Name, String email, String phone, String address, BigDecimal monthlyIncome) {
        this.Name = Name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.monthlyIncome = monthlyIncome;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", email='" + email + '\'' +
                ", monthlyIncome=" + monthlyIncome +
                '}';
    }
}
