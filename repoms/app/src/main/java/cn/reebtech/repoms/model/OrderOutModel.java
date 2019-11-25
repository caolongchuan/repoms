package cn.reebtech.repoms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.OrderOutBean;
import cn.reebtech.repoms.contact.OrderOutContact;
import cn.reebtech.repoms.model.entity.Assets;
import cn.reebtech.repoms.model.entity.Order_In_Detail;
import cn.reebtech.repoms.model.entity.Order_Out;
import cn.reebtech.repoms.model.entity.Order_Out_Detail;
import cn.reebtech.repoms.model.entity.User;
import cn.reebtech.repoms.model.entity.WareHouse;
import cn.reebtech.repoms.model.greendao.AssetsDao;
import cn.reebtech.repoms.model.greendao.Order_In_DetailDao;
import cn.reebtech.repoms.model.greendao.Order_OutDao;
import cn.reebtech.repoms.model.greendao.Order_Out_DetailDao;
import cn.reebtech.repoms.model.greendao.UserDao;
import cn.reebtech.repoms.model.greendao.WareHouseDao;
import cn.reebtech.repoms.presenter.OrderInPresenter;
import cn.reebtech.repoms.presenter.OrderOutPresenter;
import cn.reebtech.repoms.util.DBUtils;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;

public class OrderOutModel implements OrderOutContact.OrderOutMdl {
    /*获取各Table的引用*/
    private WareHouseDao getWareHouseDao(){
        return GreenDaoManager.getInstance().getSession().getWareHouseDao();
    }

    private UserDao getUserDao(){
        return GreenDaoManager.getInstance().getSession().getUserDao();
    }

    private Order_OutDao getOrderOutDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_OutDao();
    }

    private Order_Out_DetailDao getOrderOutDetailDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_Out_DetailDao();
    }

    private AssetsDao getAssetsDao(){
        return GreenDaoManager.getInstance().getSession().getAssetsDao();
    }

    @Override
    public void saveOrder(OrderOutBean data, IConDbListener callback) {
        //1.校验信息
        if(data.getLocation().equals("")){
            callback.onFailure(0, new BaseResultBean(100, "仓库信息无效"));
            return;
        }
        if(data.getUserrec().equals("")){
            callback.onFailure(0, new BaseResultBean(101, "接货人信息无效"));
            return;
        }
        if(data.getAssets() == null || data.getAssets().size() == 0){
            callback.onFailure(0, new BaseResultBean(102, "入库物资信息无效"));
            return;
        }
        Order_Out record = new Order_Out();
        //2.根据数据库中数据生成一个新的编号
        record.setId(DBUtils.generateID("CK"));
        if(record.getId().equals("")){
            callback.onFailure(0, new BaseResultBean(103, "编号生成失败，请重试"));
            return;
        }
        Order_OutDao table = getOrderOutDao();
        List<Order_Out_Detail> records = new ArrayList<Order_Out_Detail>();
        List<Order_Out> tmprecords = table.queryBuilder().list();
        try{
            //2.保存出库单实体
            record.setOutcount(data.getAssets().size());
            record.setOutdate(new Date());
            record.setOutprice(data.getInprice());
            record.setLocation(data.getLocation());
            record.setUsermgr(data.getUsermgr());
            record.setUserrec(data.getUserrec());
            record.setCompleted(true);
            record.setUploaded(false);
            table.save(record);
            //3.保存出库单物资详情
            Order_Out_DetailDao ctable = getOrderOutDetailDao();
            for (AssetBean item: data.getAssets()) {
                Order_Out_Detail ritem = new Order_Out_Detail();
                ritem.setOrder(record.getId());
                ritem.setAsset(item.getRfid());
                ritem.setCount(1);
                ritem.setPrice(item.getPrice());
                ritem.setCompleted(true);
                ritem.setUploaded(false);
                records.add(ritem);
                ctable.save(ritem);
                //更新本地物资状态
                AssetsDao table_assets = getAssetsDao();
                Assets record_assets = table_assets.queryBuilder().where(AssetsDao.Properties.Rfid.eq(item.getRfid())).unique();
                if(record_assets != null){
                    record_assets.setLastupdate(record.getOutdate());
                    record_assets.setStatus("3");
                    table_assets.save(record_assets);
                }
            }
            //4.返回成功信息
            callback.onSuccess(OrderInPresenter.TYPE_SAVE_ORDER, null, new BaseResultBean(0, ""));
        }
        catch(Exception e){
            callback.onFailure(OrderInPresenter.TYPE_SAVE_ORDER, new BaseResultBean(104, e.getMessage()));
        }
    }

    @Override
    public void loadWareHouse(IConDbListener callback) {
        WareHouseDao table = getWareHouseDao();
        List<WareHouse> records = table.queryBuilder().where(WareHouseDao.Properties.Parent.eq("0")).orderAsc(WareHouseDao.Properties.Name).list();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (WareHouse record: records) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("id", record.getId());
            item.put("name", record.getName());
            data.add(item);
        }
        callback.onSuccess(OrderOutPresenter.TYPE_LOAD_DATA_WAREHOUSE, data, new BaseResultBean(0, ""));
    }

    @Override
    public void loadFromUsers(IConDbListener callback) {
        List<User> records = loadUsers();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (User record: records) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("id", record.getUsername());
            item.put("name", record.getName());
            data.add(item);
        }
        callback.onSuccess(OrderOutPresenter.TYPE_LOAD_DATA_FROMUSERS, data, new BaseResultBean(0, ""));
    }

    @Override
    public void loadMgrs(IConDbListener callback) {
        List<User> records = loadUsers();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (User record: records) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("id", record.getUsername());
            item.put("name", record.getName());
            data.add(item);
        }
        callback.onSuccess(OrderOutPresenter.TYPE_LOAD_DATA_MGRS, data, new BaseResultBean(0, ""));
    }

    @Override
    public void loadAsset(String rfid, IConDbListener callback) {
        AssetBean result = null;
        List<Assets> records = getAssetsDao().queryBuilder().list();
        Assets record = getAssetsDao().queryBuilder().where(AssetsDao.Properties.Rfid.eq(rfid), AssetsDao.Properties.Status.notEq(3)).unique();
        if(record != null){
            result = new AssetBean();
            result.setId(record.getId());
            result.setName(record.getName());
            result.setClsct(record.getClsct());
            result.setLocation(record.getLocation());
            result.setPrice(record.getPrice());
            result.setRfid(rfid);
            result.setAsset_code(record.getAsset_code());//clc 资产编码
            callback.onSuccess(OrderOutPresenter.TYPE_LOAD_ASSET, result, new BaseResultBean(0, ""));
        }
        else{
            result = new AssetBean();
            callback.onSuccess(OrderOutPresenter.TYPE_LOAD_ASSET, result, new BaseResultBean(0, ""));
        }
    }

    private List<User> loadUsers(){
        UserDao table = getUserDao();
        return table.queryBuilder().orderAsc(UserDao.Properties.Name).list();
    }

}
