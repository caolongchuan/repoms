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
public class LastLogin {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String name;
    private String password;
    private boolean rememberme;
    private boolean autologin;
    private Date date;
@Generated(hash = 791164530)
public LastLogin(Long id, @NotNull String name, String password,
        boolean rememberme, boolean autologin, Date date) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.rememberme = rememberme;
    this.autologin = autologin;
    this.date = date;
}
@Generated(hash = 1861950774)
public LastLogin() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public String getName() {
    return this.name;
}
public void setName(String name) {
    this.name = name;
}
public String getPassword() {
    return this.password;
}
public void setPassword(String password) {
    this.password = password;
}
public boolean getRememberme() {
    return this.rememberme;
}
public void setRememberme(boolean rememberme) {
    this.rememberme = rememberme;
}
public boolean getAutologin() {
    return this.autologin;
}
public void setAutologin(boolean autologin) {
    this.autologin = autologin;
}
public Date getDate() {
    return this.date;
}
public void setDate(Date date) {
    this.date = date;
}
}
