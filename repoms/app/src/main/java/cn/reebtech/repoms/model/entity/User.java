package cn.reebtech.repoms.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value = "id DESC", unique = true)
})
public class User {
    @NotNull
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String username;
    @NotNull
    private String password;
    private String warehouse;
    private String sex;
    private String dept;
    private String pos;
    private String phone;
    private String remark;
@Generated(hash = 1747327326)
public User(@NotNull String id, @NotNull String name, @NotNull String username,
        @NotNull String password, String warehouse, String sex, String dept,
        String pos, String phone, String remark) {
    this.id = id;
    this.name = name;
    this.username = username;
    this.password = password;
    this.warehouse = warehouse;
    this.sex = sex;
    this.dept = dept;
    this.pos = pos;
    this.phone = phone;
    this.remark = remark;
}
@Generated(hash = 586692638)
public User() {
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
public String getUsername() {
    return this.username;
}
public void setUsername(String username) {
    this.username = username;
}
public String getPassword() {
    return this.password;
}
public void setPassword(String password) {
    this.password = password;
}
public String getWarehouse() {
    return this.warehouse;
}
public void setWarehouse(String warehouse) {
    this.warehouse = warehouse;
}
public String getSex() {
    return this.sex;
}
public void setSex(String sex) {
    this.sex = sex;
}
public String getDept() {
    return this.dept;
}
public void setDept(String dept) {
    this.dept = dept;
}
public String getPos() {
    return this.pos;
}
public void setPos(String pos) {
    this.pos = pos;
}
public String getPhone() {
    return this.phone;
}
public void setPhone(String phone) {
    this.phone = phone;
}
public String getRemark() {
    return this.remark;
}
public void setRemark(String remark) {
    this.remark = remark;
}
}
