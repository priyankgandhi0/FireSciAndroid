package com.firesafetysci.FireSci.Main;

public class Location {
    private int id;
    private String installerFiresciPin;
    private String customerFiresciPin;
    private String companyName;
    private String city;
    private String stateProvince;
    private String country;
    private String address;
    private String zipcode;
    private String locationDescription;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstallerFiresciPin() {
        return installerFiresciPin;
    }

    public void setInstallerFiresciPin(String installerFiresciPin) {
        this.installerFiresciPin = installerFiresciPin;
    }

    public String getCustomerFiresciPin() {
        return customerFiresciPin;
    }

    public void setCustomerFiresciPin(String customerFiresciPin) {
        this.customerFiresciPin = customerFiresciPin;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }
}
