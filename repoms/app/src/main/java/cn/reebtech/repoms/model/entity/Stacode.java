package cn.reebtech.repoms.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value = "id DESC", unique = true)
})

public class Stacode {
    @NotNull
    private String id;
    @NotNull
    private String name;
    private String content;
    private String parent;
    private int layer;
    private String remark;
@Generated(hash = 1398805814)
public Stacode(@NotNull String id, @NotNull String name, String content,
        String parent, int layer, String remark) {
    this.id = id;
    this.name = name;
    this.content = content;
    this.parent = parent;
    this.layer = layer;
    this.remark = remark;
}
@Generated(hash = 1315801363)
public Stacode() {
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
public String getContent() {
    return this.content;
}
public void setContent(String content) {
    this.content = content;
}
public String getParent() {
    return this.parent;
}
public void setParent(String parent) {
    this.parent = parent;
}
public int getLayer() {
    return this.layer;
}
public void setLayer(int layer) {
    this.layer = layer;
}
public String getRemark() {
    return this.remark;
}
public void setRemark(String remark) {
    this.remark = remark;
}
}
