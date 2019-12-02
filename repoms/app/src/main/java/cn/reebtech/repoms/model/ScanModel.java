package cn.reebtech.repoms.model;

import java.util.List;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.contact.ScanContact;
import cn.reebtech.repoms.model.entity.Assets;
import cn.reebtech.repoms.model.entity.WareHouse;
import cn.reebtech.repoms.model.greendao.AssetsDao;
import cn.reebtech.repoms.model.greendao.WareHouseDao;
import cn.reebtech.repoms.presenter.OrderOutPresenter;
import cn.reebtech.repoms.presenter.ScanPresenter;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;

public class ScanModel implements ScanContact.ScanMdl {

    @Override
    public void loadAsset(String rfid, IConDbListener callback) {
        AssetBean result = null;
        Assets record = getAssetsDao().queryBuilder().where(AssetsDao.Properties.Rfid.eq(rfid)).unique();
        if(record != null){
            result = new AssetBean();
            result.setId(record.getId());
            result.setName(record.getName());
            result.setClsct(record.getClsct());
            result.setLocation(getLocation(record.getLocation()));
            result.setRfid(rfid);
            result.setPrice(record.getPrice());
            result.setManut(record.getManut());
            callback.onSuccess(ScanPresenter.TYPE_LOAD_ASSET, result, new BaseResultBean(0, ""));
        }
        else{
            result = new AssetBean();
            result.setRfid(rfid);
            callback.onSuccess(ScanPresenter.TYPE_LOAD_ASSET, result, new BaseResultBean(0, ""));
        }
    }
    private String getLocation(String id){
        StringBuilder result = new StringBuilder();
        if(id != null && !id.equals("")){
            WareHouseDao table = getWareHouseDao();
            WareHouse record = table.queryBuilder().where(WareHouseDao.Properties.Id.eq(id)).unique();
            if(record != null){
                result.insert(0, record.getName() + (result.length() == 0 ? "" : "/"));
                if(!record.getParent().equals("0")){
                    result.insert(0, getLocation(record.getParent()) + (result.length() == 0 ? "" : "/"));
                }
            }
        }
        return result.toString();
    }
    private AssetsDao getAssetsDao(){
        return GreenDaoManager.getInstance().getSession().getAssetsDao();
    }
    private WareHouseDao getWareHouseDao(){
        return GreenDaoManager.getInstance().getSession().getWareHouseDao();
    }
}
