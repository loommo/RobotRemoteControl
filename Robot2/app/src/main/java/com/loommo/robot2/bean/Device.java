package com.loommo.robot2.bean;

/**
 * Created by loommo on 16/3/27.
 */
public class Device {

    private String name;
    private String address;
    private boolean isBonded;

    public Device(String name, String address) {
        this.name = name;
        this.address = address;
        isBonded = false;
    }

    public Device(String name, String address, boolean isBonded) {
        this.name = name;
        this.address = address;
        this.isBonded = isBonded;
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

    public boolean isBonded() {
        return isBonded;
    }

    public void setBonded(boolean bonded) {
        isBonded = bonded;
    }
}
