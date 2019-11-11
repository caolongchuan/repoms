package cn.reebtech.repoms.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
    @Index(value = "id DESC", unique = true)
})
public class Department {
    @Id(autoincrement = true)
    private Long pid;
    @NotNull
    @Index(unique = true)
    private String id;
    @NotNull
    private String name;
    private String parent;
    private int layer;
    private String remark;
    @Generated(hash = 2001529568)
    public Department(Long pid, @NotNull String id, @NotNull String name,
            String parent, int layer, String remark) {
        this.pid = pid;
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.layer = layer;
        this.remark = remark;
    }
    @Generated(hash = 355406289)
    public Department() {
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
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
