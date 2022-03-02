package com.android.myappproject.vo;

public class CustomHospitalVo
{
    private String name;
    private String address;
    private String homepage;

    public CustomHospitalVo(String name, String address, String homepage)
    {
        this.name = name;
        this.address = address;
        this.homepage = homepage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
}
