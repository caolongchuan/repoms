package cn.reebtech.repoms.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value="id DESC", unique = true)
})

public class Order_Req_Detail {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String order;
    @NotNull
    private String asset;
    private int count;
    private double price;
    private boolean completed;
    private boolean uploaded;
    private String remark;
@Generated(hash = 1485888315)
public Order_Req_Detail(Long id, @NotNull String order, @NotNull String asset,
        int count, double price, boolean completed, boolean uploaded,
        String remark) {
    this.id = id;
    this.order = order;
    this.asset = asset;
    this.count = count;
    this.price = price;
    this.completed = completed;
    this.uploaded = uploaded;
    this.remark = remark;
}
@Generated(hash = 1250768302)
public Order_Req_Detail() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public String getOrder() {
    return this.order;
}
public void setOrder(String order) {
    this.order = order;
}
public String getAsset() {
    return this.asset;
}
public void setAsset(String asset) {
    this.asset = asset;
}
public int getCount() {
    return this.count;
}
public void setCount(int count) {
    this.count = count;
}
public double getPrice() {
    return this.price;
}
public void setPrice(double price) {
    this.price = price;
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
