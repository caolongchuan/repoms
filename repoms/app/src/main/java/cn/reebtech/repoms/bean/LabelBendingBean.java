package cn.reebtech.repoms.bean;

import java.util.Date;
import java.util.List;

public class LabelBendingBean {
    private String id;
    private String location;
    private Date invdate;
    private String usermgr;
    private List<AssetBean> assets;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getInvdate() {
        return invdate;
    }

    public void setInvdate(Date invdate) {
        this.invdate = invdate;
    }

    public String getUsermgr() {
        return usermgr;
    }

    public void setUsermgr(String usermgr) {
        this.usermgr = usermgr;
    }

    public List<AssetBean> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetBean> assets) {
        this.assets = assets;
    }
}
