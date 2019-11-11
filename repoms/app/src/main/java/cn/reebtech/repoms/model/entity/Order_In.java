package cn.reebtech.repoms.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

@Entity(indexes = {
        @Index(value="id DESC", unique = true)
})

public class Order_In {
    @Id(autoincrement = true)
    private Long pid;
    @NotNull
    @Index(unique = true)
    private String id;
    @NotNull
    private String location;
    @NotNull
    private Date indate;
    @NotNull
    private String usersend;
    @NotNull
    private String usermgr;
    private int incount;
    private double inprice;
    private boolean completed;
    private boolean uploaded;
    private String remark;
@Generated(hash = 929948747)
public Order_In(Long pid, @NotNull String id, @NotNull String location,
        @NotNull Date indate, @NotNull String usersend, @NotNull String usermgr,
        int incount, double inprice, boolean completed, boolean uploaded,
        String remark) {
    this.pid = pid;
    this.id = id;
    this.location = location;
    this.indate = indate;
    this.usersend = usersend;
    this.usermgr = usermgr;
    this.incount = incount;
    this.inprice = inprice;
    this.completed = completed;
    this.uploaded = uploaded;
    this.remark = remark;
}
@Generated(hash = 1415310948)
public Order_In() {
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
public String getLocation() {
    return this.location;
}
public void setLocation(String location) {
    this.location = location;
}
public Date getIndate() {
    return this.indate;
}
public void setIndate(Date indate) {
    this.indate = indate;
}
public String getUsersend() {
    return this.usersend;
}
public void setUsersend(String usersend) {
    this.usersend = usersend;
}
public String getUsermgr() {
    return this.usermgr;
}
public void setUsermgr(String usermgr) {
    this.usermgr = usermgr;
}
public int getIncount() {
    return this.incount;
}
public void setIncount(int incount) {
    this.incount = incount;
}
public double getInprice() {
    return this.inprice;
}
public void setInprice(double inprice) {
    this.inprice = inprice;
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
public String getRemark() {
    return this.remark;
}
public void setRemark(String remark) {
    this.remark = remark;
}
}
