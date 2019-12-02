package cn.reebtech.repoms.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

@Entity(indexes = {
        @Index(value = "id DESC", unique = true)
})

public class Order_In_Detail {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String order;
    @NotNull
    private String asset;
    @NotNull
    private String name;
    @NotNull
    private String clsct;
    private String specification;
    private String manut;
    private double price;
    private String location;
    private String rfid;
    private boolean completed;
    private boolean uploaded;
    private int count;
    private String remark;
    private String asset_code;  //资产编码
    private String czl;//残值率

    @Generated(hash = 1194645511)
    public Order_In_Detail(Long id, @NotNull String order, @NotNull String asset, @NotNull String name,
            @NotNull String clsct, String specification, String manut, double price, String location, String rfid,
            boolean completed, boolean uploaded, int count, String remark, String asset_code, String czl) {
        this.id = id;
        this.order = order;
        this.asset = asset;
        this.name = name;
        this.clsct = clsct;
        this.specification = specification;
        this.manut = manut;
        this.price = price;
        this.location = location;
        this.rfid = rfid;
        this.completed = completed;
        this.uploaded = uploaded;
        this.count = count;
        this.remark = remark;
        this.asset_code = asset_code;
        this.czl = czl;
    }

    @Generated(hash = 1683712341)
    public Order_In_Detail() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getAsset() {
        return this.asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClsct() {
        return this.clsct;
    }

    public void setClsct(String clsct) {
        this.clsct = clsct;
    }

    public String getSpecification() {
        return this.specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getManut() {
        return this.manut;
    }

    public void setManut(String manut) {
        this.manut = manut;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRfid() {
        return this.rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public boolean getCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean getUploaded() {
        return this.uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRemark() {
        return this.remark;
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

    public String getCzl() {
        return czl;
    }

    public void setCzl(String czl) {
        this.czl = czl;
    }
}
