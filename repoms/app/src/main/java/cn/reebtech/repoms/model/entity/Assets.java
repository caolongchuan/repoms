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

public class Assets {
    @Id(autoincrement = true)
    private Long pid;
    @NotNull
    @Index(unique = true)
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String clsct;
    private String specification;
    private String manut;
    private double price;
    private Date indate;
    private Date lastupdate;
    private String status;
    private boolean inbound;
    private String location;
    private String rfid;
    private String remark;
    private String asset_code;      //clc 资产编码
    private String czl;             //残值率
    private String user;
    private String warranty_period;

    @Generated(hash = 1134816599)
    public Assets(Long pid, @NotNull String id, @NotNull String name, @NotNull String clsct,
            String specification, String manut, double price, Date indate, Date lastupdate,
            String status, boolean inbound, String location, String rfid, String remark,
            String asset_code, String czl, String user, String warranty_period) {
        this.pid = pid;
        this.id = id;
        this.name = name;
        this.clsct = clsct;
        this.specification = specification;
        this.manut = manut;
        this.price = price;
        this.indate = indate;
        this.lastupdate = lastupdate;
        this.status = status;
        this.inbound = inbound;
        this.location = location;
        this.rfid = rfid;
        this.remark = remark;
        this.asset_code = asset_code;
        this.czl = czl;
        this.user = user;
        this.warranty_period = warranty_period;
    }
    @Generated(hash = 1373698660)
    public Assets() {
    }

    public Long getPid() {
    return this.pid;
}
public void setPid(Long pid) {
    this.pid = pid;
}
public String getId() {
    return this.id;
}
public void setId(String id) {
    this.id = id;
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
public Date getIndate() {
    return this.indate;
}
public void setIndate(Date indate) {
    this.indate = indate;
}
public Date getLastupdate() {
    return this.lastupdate;
}
public void setLastupdate(Date lastupdate) {
    this.lastupdate = lastupdate;
}
public String getStatus() {
    return this.status;
}
public void setStatus(String status) {
    this.status = status;
}
public boolean getInbound() {
    return this.inbound;
}
public void setInbound(boolean inbound) {
    this.inbound = inbound;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getWarranty_period() {
        return warranty_period;
    }

    public void setWarranty_period(String warranty_period) {
        this.warranty_period = warranty_period;
    }
}
