package cn.reebtech.repoms.bean;

import java.io.Serializable;
import java.util.Date;

public class AssetBean implements Serializable {
    private static final long serialVersionUID = 4359709211352400087L;
    private String id;
    private String name;
    private String clsct;
    private String specification;
    private String manut;
    private double price;
    private Date indate;
    private String status;
    private boolean inbound;
    private String location;
    private String rfid;
    private String remark;
    private String asset_code;  //clc资产编码

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClsct() {
        return clsct;
    }

    public void setClsct(String clsct) {
        this.clsct = clsct;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getManut() {
        return manut;
    }

    public void setManut(String manut) {
        this.manut = manut;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getIndate() {
        return indate;
    }

    public void setIndate(Date indate) {
        this.indate = indate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isInbound() {
        return inbound;
    }

    public void setInbound(boolean inbound) {
        this.inbound = inbound;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAsset_code() {
        return asset_code;
    }

    public void setAsset_code(String asset_code) {
        this.asset_code = asset_code;
    }
}
