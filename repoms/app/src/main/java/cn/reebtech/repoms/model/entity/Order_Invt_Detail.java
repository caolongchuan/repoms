package cn.reebtech.repoms.model.entity;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value="id DESC", unique = true)
})


public class Order_Invt_Detail {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String order;
    @NotNull
    private String asset;
    private boolean completed;
    private boolean uploaded;
@Generated(hash = 1192558827)
public Order_Invt_Detail(Long id, @NotNull String order, @NotNull String asset,
        boolean completed, boolean uploaded) {
    this.id = id;
    this.order = order;
    this.asset = asset;
    this.completed = completed;
    this.uploaded = uploaded;
}
@Generated(hash = 1236657857)
public Order_Invt_Detail() {
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

}
