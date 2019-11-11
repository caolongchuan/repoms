package cn.reebtech.repoms.model.entity;

import android.support.annotation.IntDef;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value = "id DESC", unique = true)
})
public class IDGenerator {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String type;
    @NotNull
    private Date date;
    @NotNull
    private int curnum;
@Generated(hash = 976476710)
public IDGenerator(Long id, @NotNull String type, @NotNull Date date,
        int curnum) {
    this.id = id;
    this.type = type;
    this.date = date;
    this.curnum = curnum;
}
@Generated(hash = 1957540368)
public IDGenerator() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public String getType() {
    return this.type;
}
public void setType(String type) {
    this.type = type;
}
public Date getDate() {
    return this.date;
}
public void setDate(Date date) {
    this.date = date;
}
public int getCurnum() {
    return this.curnum;
}
public void setCurnum(int curnum) {
    this.curnum = curnum;
}

}
