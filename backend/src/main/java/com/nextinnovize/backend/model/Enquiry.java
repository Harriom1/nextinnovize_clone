package com.nextinnovize.backend.model;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;

@Entity(name = "enquiries")
public class Enquiry {

    @Id
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String company;
    private String service;
    private String message;

    public Enquiry() {
    }

    public Enquiry(String name, String email, String phone,
                   String company, String service, String message) {

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.company = company;
        this.service = service;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}