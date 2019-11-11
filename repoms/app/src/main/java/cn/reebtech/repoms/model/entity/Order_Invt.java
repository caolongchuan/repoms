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

public class Order_Invt {
    @Id(autoincrement = true)
    private Long pid;
    @NotNull
    @Index(unique = true)
    private String id;
    @NotNull
    private String location;
    @NotNull
    private Date invdate;
    @NotNull
    private String invuser;
    private int invcount;
    private boolean completed;
    private boolean uploaded;
    private String remark;
@Generated(hash = 117881560)
public Order_Invt(Long pid, @NotNull String id, @NotNull String location,
        @NotNull Date invdate, @NotNull String invuser, int invcount,
        boolean completed, boolean uploaded, String remark) {
    this.pid = pid;
    this.id = id;
    this.location = location;
    this.invdate = invdate;
    this.invuser = invuser;
    this.invcount = invcount;
    this.completed = completed;
    this.uploaded = uploaded;
    this.remark = remark;
}
@Generated(hash = 330204760)
public Order_Invt() {
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
public Date getInvdate() {
    return this.invdate;
}
public void setInvdate(Date invdate) {
    this.invdate = invdate;
}
public String getInvuser() {
    return this.invuser;
}
public void setInvuser(String invuser) {
    this.invuser = invuser;
}
public int getInvcount() {
    return this.invcount;
}
public void setInvcount(int invcount) {
    this.invcount = invcount;
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
