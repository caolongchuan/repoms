package cn.reebtech.repoms.bean;

import java.util.Date;
import java.util.List;

public class OrderRetBean {
    private String id;
    private String location;
    private Date outdate;
    private String userret;
    private String usermgr;
    private int incount;
    private double inprice;
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

    public Date getOutdate() {
        return outdate;
    }

    public void setOutdate(Date outdate) {
        this.outdate = outdate;
    }

    public String getUserret() {
        return userret;
    }

    public void setUserret(String userret) {
        this.userret = userret;
    }

    public String getUsermgr() {
        return usermgr;
    }

    public void setUsermgr(String usermgr) {
        this.usermgr = usermgr;
    }

    public int getIncount() {
        return incount;
    }

    public void setIncount(int incount) {
        this.incount = incount;
    }

    public double getInprice() {
        if(assets == null || assets.size() == 0){
            return 0;
        }
        else{
            double price = 0;
            for(AssetBean record : assets){
                price += record.getPrice();
            }
            return price;
        }
    }

    public void setInprice(double inprice) {
        this.inprice = inprice;
    }

    public List<AssetBean> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetBean> assets) {
        this.assets = assets;
    }
}
