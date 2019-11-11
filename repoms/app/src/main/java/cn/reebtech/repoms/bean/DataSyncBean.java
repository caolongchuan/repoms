package cn.reebtech.repoms.bean;

import java.util.ArrayList;
import java.util.List;

public class DataSyncBean {
    private List<Integer> downlItems;
    private List<Integer> uplItems;
    public DataSyncBean(){
        downlItems = new ArrayList<Integer>();
        uplItems = new ArrayList<Integer>();
    }
    public List<Integer> getDownlItems() {
        return downlItems;
    }

    public void setDownlItems(List<Integer> downlItems) {
        this.downlItems = downlItems;
    }

    public List<Integer> getUplItems() {
        return uplItems;
    }

    public void setUplItems(List<Integer> uplItems) {
        this.uplItems = uplItems;
    }
}
