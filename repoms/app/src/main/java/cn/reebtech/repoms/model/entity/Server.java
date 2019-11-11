package cn.reebtech.repoms.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value = "id DESC", unique = true)
})
public class Server {
    @Id(autoincrement = true)
    private Long id;
    private String url;
    private String user;
    private String password;
    private Date update;
@Generated(hash = 1270356252)
public Server(Long id, String url, String user, String password, Date update) {
    this.id = id;
    this.url = url;
    this.user = user;
    this.password = password;
    this.update = update;
}
@Generated(hash = 1151933134)
public Server() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public String getUrl() {
    return this.url;
}
public void setUrl(String url) {
    this.url = url;
}
public String getUser() {
    return this.user;
}
public void setUser(String user) {
    this.user = user;
}
public String getPassword() {
    return this.password;
}
public void setPassword(String password) {
    this.password = password;
}
public Date getUpdate() {
    return this.update;
}
public void setUpdate(Date update) {
    this.update = update;
}
}
