package cn.reebtech.repoms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.OrderRetBean;
import cn.reebtech.repoms.contact.OrderRetContact;
import cn.reebtech.repoms.model.entity.Assets;
import cn.reebtech.repoms.model.entity.Order_Ret;
import cn.reebtech.repoms.model.entity.Order_Ret_Detail;
import cn.reebtech.repoms.model.entity.User;
import cn.reebtech.repoms.model.entity.WareHouse;
import cn.reebtech.repoms.model.greendao.AssetsDao;
import cn.reebtech.repoms.model.greendao.Order_RetDao;
import cn.reebtech.repoms.model.greendao.Order_Ret_DetailDao;
import cn.reebtech.repoms.model.greendao.UserDao;
import cn.reebtech.repoms.model.greendao.WareHouseDao;
import cn.reebtech.repoms.presenter.OrderInPresenter;
import cn.reebtech.repoms.presenter.OrderOutPresenter;
import cn.reebtech.repoms.util.DBUtils;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;

public class OrderRetModel implements OrderRetContact.OrderRetMdl  {
    /*获取各Table的引用*/
    private WareHouseDao getWareHouseDao(){
        return GreenDaoManager.getInstance().getSession().getWareHouseDao();
    }
    private UserDao getUserDao(){
        return GreenDaoManager.getInstance().getSession().getUserDao();
    }
    private Order_RetDao getOrderRetDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_RetDao();
    }
    private Order_Ret_DetailDao getOrderRetDetailDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_Ret_DetailDao();
    }
    private AssetsDao getAssetsDao(){
        return GreenDaoManager.getInstance().getSession().getAssetsDao();
    }

    @Override
    public void saveOrder(OrderRetBean data, IConDbListener callback) {
        //1.校验信息
        if(data.getLocation().equals("")){
            callback.onFailure(0, new BaseResultBean(100, "仓库信息无效"));
            return;
        }
        if(data.getUserret().equals("")){
            callback.onFailure(0, new BaseResultBean(101, "归还人信息无效"));
            return;
        }
        if(data.getAssets() == null || data.getAssets().size() == 0){
            callback.onFailure(0, new BaseResultBean(102, "入库物资信息无效"));
            return;
        }
        Order_Ret record = new Order_Ret();
        //2.根据数据库中数据生成一个新的编号
        record.setId(DBUtils.generateID("GH"));
        if(record.getId().equals("")){
            callback.onFailure(0, new BaseResultBean(103, "编号生成失败，请重试"));
            return;
        }
        Order_RetDao table = getOrderRetDao();
        List<Order_Ret_Detail> records = new ArrayList<Order_Ret_Detail>();
        try{
            //2.保存入库单实体
            record.setIncount(data.getAssets().size());
            record.setRetdate(new Date());
            record.setInprice(data.getInprice());
            record.setLocation(data.getLocation());
            record.setUsermgr(data.getUsermgr());
            record.setUserret(data.getUserret());
            record.setCompleted(true);
            record.setUploaded(false);
            table.save(record);
            //3.保存入库单物资详情
            Order_Ret_DetailDao ctable = getOrderRetDetailDao();
            for (AssetBean item: data.getAssets()) {
                Order_Ret_Detail ritem = new Order_Ret_Detail();
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
                    record_assets.setLastupdate(record.getRetdate());
                    record_assets.setStatus("1");
                    table_assets.save(record_assets);
                }
            }
            //4.返回成功信息
            callback.onSuccess(OrderInPresenter.TYPE_SAVE_ORDER, null, new BaseResultBean(0, ""));
        }
        catch(Exception e){
            callback.onFailure(OrderInPresenter.TYPE_SAVE_ORDER, new BaseResultBean(104, e.getMessage()));
            //删除可能已保存的记录
            table.delete(record);
            for (Order_Ret_Detail item: records) {
                getOrderRetDetailDao().delete(item);
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
        Assets record = getAssetsDao().queryBuilder().where(AssetsDao.Properties.Rfid.eq(rfid),AssetsDao.Properties.Status.eq(3)).unique();
        if(record != null){
            result = new AssetBean();
            result.setId(record.getId());
            result.setName(record.getName());
            result.setClsct(record.getClsct());
            result.setLocation(record.getLocation());
            result.setPrice(record.getPrice());
            result.setRfid(rfid);
            result.setAsset_code(record.getAsset_code());//clc资产编码
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
