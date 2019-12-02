package cn.reebtech.repoms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.OrderInvtBean;
import cn.reebtech.repoms.contact.OrderInvtContact;
import cn.reebtech.repoms.model.entity.Assets;
import cn.reebtech.repoms.model.entity.Order_Invt;
import cn.reebtech.repoms.model.entity.Order_Invt_Detail;
import cn.reebtech.repoms.model.entity.Order_Ret_Detail;
import cn.reebtech.repoms.model.entity.User;
import cn.reebtech.repoms.model.entity.WareHouse;
import cn.reebtech.repoms.model.greendao.AssetsDao;
import cn.reebtech.repoms.model.greendao.Order_InvtDao;
import cn.reebtech.repoms.model.greendao.Order_Invt_DetailDao;
import cn.reebtech.repoms.model.greendao.Order_Ret_DetailDao;
import cn.reebtech.repoms.model.greendao.UserDao;
import cn.reebtech.repoms.model.greendao.WareHouseDao;
import cn.reebtech.repoms.presenter.OrderInPresenter;
import cn.reebtech.repoms.presenter.OrderOutPresenter;
import cn.reebtech.repoms.util.DBUtils;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;

public class OrderInvtModel implements OrderInvtContact.OrderInvtMdl{
    /*获取各Table的引用*/
    private WareHouseDao getWareHouseDao(){
        return GreenDaoManager.getInstance().getSession().getWareHouseDao();
    }
    private UserDao getUserDao(){
        return GreenDaoManager.getInstance().getSession().getUserDao();
    }
    private Order_InvtDao getOrderInvtDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_InvtDao();
    }
    private Order_Invt_DetailDao getOrderInvtDetailDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_Invt_DetailDao();
    }
    private AssetsDao getAssetsDao(){
        return GreenDaoManager.getInstance().getSession().getAssetsDao();
    }

    @Override
    public void saveOrder(OrderInvtBean data, IConDbListener callback) {
        //1.校验信息
        if(data.getLocation().equals("")){
            callback.onFailure(0, new BaseResultBean(100, "办公室信息无效"));
            return;
        }
        if(data.getUsermgr().equals("")){
            callback.onFailure(0, new BaseResultBean(101, "盘点人信息无效"));
            return;
        }
        if(data.getAssets() == null || data.getAssets().size() == 0){
            callback.onFailure(0, new BaseResultBean(102, "入账物资信息无效"));
            return;
        }
        Order_Invt record = new Order_Invt();
        //2.根据数据库中数据生成一个新的编号
        record.setId(DBUtils.generateID("PD"));
        if(record.getId().equals("")){
            callback.onFailure(0, new BaseResultBean(103, "编号生成失败，请重试"));
            return;
        }
        Order_InvtDao table = getOrderInvtDao();
        List<Order_Invt_Detail> records = new ArrayList<Order_Invt_Detail>();
        try{
            //2.保存入库单实体
            record.setInvdate(new Date());
            record.setLocation(data.getLocation());
            record.setInvuser(data.getUsermgr());
            record.setInvcount(data.getAssets().size());
            record.setCompleted(true);
            record.setUploaded(false);
            table.save(record);
            //3.保存入库单物资详情
            Order_Invt_DetailDao ctable = getOrderInvtDetailDao();
            for (AssetBean item: data.getAssets()) {
                Order_Invt_Detail ritem = new Order_Invt_Detail();
                ritem.setOrder(record.getId());
                ritem.setAsset(item.getRfid());
                ritem.setCompleted(true);
                ritem.setUploaded(false);
                records.add(ritem);
                ctable.save(ritem);
            }
            //4.返回成功信息
            callback.onSuccess(OrderInPresenter.TYPE_SAVE_ORDER, null, new BaseResultBean(0, ""));
        }
        catch(Exception e){
            callback.onFailure(OrderInPresenter.TYPE_SAVE_ORDER, new BaseResultBean(104, e.getMessage()));
            //删除可能已保存的记录
            table.delete(record);
            for (Order_Invt_Detail item: records) {
                getOrderInvtDetailDao().delete(item);
            }
        }
    }

    @Override
    public void loadWareHouse(IConDbListener callback) {
        WareHouseDao table = getWareHouseDao();
        List<WareHouse> records = table.queryBuilder().orderAsc(WareHouseDao.Properties.Name).list();
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
        Assets record = getAssetsDao().queryBuilder()
                .where(AssetsDao.Properties.Rfid.eq(rfid),AssetsDao.Properties.Status.eq(1))
                .unique();
        if(record != null){
            result = new AssetBean();
            result.setId(record.getId());
            result.setName(record.getName());
            result.setClsct(record.getClsct());
            result.setLocation(record.getLocation());
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
