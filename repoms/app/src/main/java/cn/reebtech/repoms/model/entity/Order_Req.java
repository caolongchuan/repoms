package cn.reebtech.repoms.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value="id DESC", unique = true)
})

public class Order_Req {
    @Id(autoincrement = true)
    private Long pid;
    @NotNull
    @Index(unique = true)
    private String id;
    @NotNull
    private String location;
    @NotNull
    private Date reqdate;
    @NotNull
    private String userreq;
    @NotNull
    private String usermgr;
    private int outcount;
    private double outprice;
    private boolean completed;
    private boolean uploaded;
    private String remark;
@Generated(hash = 1981448552)
public Order_Req(Long pid, @NotNull String id, @NotNull String location,
        @NotNull Date reqdate, @NotNull String userreq, @NotNull String usermgr,
        int outcount, double outprice, boolean completed, boolean uploaded,
        String remark) {
    this.pid = pid;
    this.id = id;
    this.location = location;
    this.reqdate = reqdate;
    this.userreq = userreq;
    this.usermgr = usermgr;
    this.outcount = outcount;
    this.outprice = outprice;
    this.completed = completed;
    this.uploaded = uploaded;
    this.remark = remark;
}
@Generated(hash = 324879488)
public Order_Req() {
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
public Date getReqdate() {
    return this.reqdate;
}
public void setReqdate(Date reqdate) {
    this.reqdate = reqdate;
}
public String getUserreq() {
    return this.userreq;
}
public void setUserreq(String userreq) {
    this.userreq = userreq;
}
public String getUsermgr() {
    return this.usermgr;
}
public void setUsermgr(String usermgr) {
    this.usermgr = usermgr;
}
public int getOutcount() {
    return this.outcount;
}
public void setOutcount(int outcount) {
    this.outcount = outcount;
}
public double getOutprice() {
    return this.outprice;
}
public void setOutprice(double outprice) {
    this.outprice = outprice;
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
