package cn.reebtech.repoms.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value="id DESC", unique = true)
})

public class AssetClsct {
    @Id(autoincrement = true)
    private Long pid;
    @NotNull
    @Index(unique = true)
    private String id;
    @NotNull
    private String name;
    private String parent;
    private int layer;
    private String location;
    private int minstock;
    private int maxstock;
@Generated(hash = 1435557023)
public AssetClsct(Long pid, @NotNull String id, @NotNull String name,
        String parent, int layer, String location, int minstock, int maxstock) {
    this.pid = pid;
    this.id = id;
    this.name = name;
    this.parent = parent;
    this.layer = layer;
    this.location = location;
    this.minstock = minstock;
    this.maxstock = maxstock;
}
@Generated(hash = 238492993)
public AssetClsct() {
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
public String getLocation() {
    return this.location;
}
public void setLocation(String location) {
    this.location = location;
}
public int getMinstock() {
    return this.minstock;
}
public void setMinstock(int minstock) {
    this.minstock = minstock;
}
public int getMaxstock() {
    return this.maxstock;
}
public void setMaxstock(int maxstock) {
    this.maxstock = maxstock;
}
}
