package cn.reebtech.repoms.model;

import org.greenrobot.greendao.DbUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.OrderInBean;
import cn.reebtech.repoms.contact.OrderInContact;
import cn.reebtech.repoms.model.entity.Assets;
import cn.reebtech.repoms.model.entity.Order_In;
import cn.reebtech.repoms.model.entity.Order_In_Detail;
import cn.reebtech.repoms.model.entity.User;
import cn.reebtech.repoms.model.entity.WareHouse;
import cn.reebtech.repoms.model.greendao.AssetsDao;
import cn.reebtech.repoms.model.greendao.Order_InDao;
import cn.reebtech.repoms.model.greendao.Order_In_DetailDao;
import cn.reebtech.repoms.model.greendao.UserDao;
import cn.reebtech.repoms.model.greendao.WareHouseDao;
import cn.reebtech.repoms.presenter.OrderInPresenter;
import cn.reebtech.repoms.util.CommonUtils;
import cn.reebtech.repoms.util.DBUtils;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;

public class OrderInModel implements OrderInContact.OrderInMdl {
    @Override
    public void saveOrder(OrderInBean data, IConDbListener callback) {
        //1.校验信息
        if(data.getLocation().equals("")){
            callback.onFailure(0, new BaseResultBean(100, "仓库信息无效"));
            return;
        }
        if(data.getUsersend().equals("")){
            callback.onFailure(0, new BaseResultBean(101, "送货人信息无效"));
            return;
        }
        if(data.getAssets() == null || data.getAssets().size() == 0){
            callback.onFailure(0, new BaseResultBean(102, "入库物资信息无效"));
            return;
        }
        Order_In record = new Order_In();
        //2.根据数据库中数据生成一个新的编号
        record.setId(DBUtils.generateID("RK"));
        if(record.getId().equals("")){
            callback.onFailure(0, new BaseResultBean(103, "编号生成失败，请重试"));
            return;
        }
        Order_InDao table = getOrderInDao();
        List<Order_In_Detail> records = new ArrayList<Order_In_Detail>();
        try{
            //2.保存入库单实体
            record.setIncount(data.getAssets().size());
            record.setIndate(new Date());
            record.setInprice(data.getInprice());
            record.setLocation(data.getLocation());
            record.setUsermgr(data.getUsermgr());
            record.setUsersend(data.getUsersend());
            record.setCompleted(true);
            record.setUploaded(false);
            table.save(record);
            //3.保存入库单物资详情
            Order_In_DetailDao ctable = getOrderInDetailDao();
            for (AssetBean item: data.getAssets()) {
                Order_In_Detail ritem = new Order_In_Detail();
                ritem.setOrder(record.getId());
                ritem.setAsset(generateAssetID(item.getClsct()));
                ritem.setName(item.getName() + DBUtils.generateNameFromId(ritem.getAsset()));
                ritem.setClsct(item.getClsct());
                ritem.setLocation(data.getLocation());
                ritem.setSpecification(item.getSpecification());
                ritem.setPrice(item.getPrice());
                ritem.setManut(item.getManut());
                ritem.setRfid(item.getRfid());
                ritem.setCount(1);
                ritem.setCompleted(true);
                ritem.setUploaded(false);
                ritem.setAsset_code(item.getAsset_code());//clc资产编码
                ritem.setCzl(item.getCzl());
                records.add(ritem);
                ctable.save(ritem);
                //4. 更新物资库中有相同rfid记录的rfid字段未空
                AssetsDao table_asset = getAssetsDao();
                List<Assets> assets_olds = table_asset.queryBuilder().where(AssetsDao.Properties.Rfid.eq(item.getRfid())).list();
                for(Assets assets_old : assets_olds){
                    assets_old.setRfid("");
                    table_asset.update(assets_old);
                }
                //5. 添加到新物资中
                Assets assets = new Assets();
                assets.setId(ritem.getAsset());
                assets.setName(item.getName() + DBUtils.generateNameFromId(ritem.getAsset()));
                assets.setClsct(item.getClsct());
                assets.setPrice(item.getPrice());
                assets.setLocation(data.getLocation());
                assets.setSpecification(item.getSpecification());
                assets.setManut(item.getManut());
                assets.setIndate(record.getIndate());
                assets.setLastupdate(record.getIndate());
                assets.setRfid(item.getRfid());
                assets.setStatus("1");
                assets.setAsset_code(item.getAsset_code());//clc资产编码
                table_asset.save(assets);

            }
            //5.返回成功信息
            callback.onSuccess(OrderInPresenter.TYPE_SAVE_ORDER, null, new BaseResultBean(0, ""));
        }
        catch(Exception e){
            callback.onFailure(OrderInPresenter.TYPE_SAVE_ORDER, new BaseResultBean(104, e.getMessage()));
            //删除可能已保存的记录
            table.delete(record);
            for (Order_In_Detail item: records) {
                getOrderInDetailDao().delete(item);
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
        callback.onSuccess(OrderInPresenter.TYPE_LOAD_DATA_WAREHOUSE, data, new BaseResultBean(0, ""));
    }

    @Override
    public void loadFromUsers(IConDbListener callback) {
        UserDao table = getUserDao();
        List<User> records = table.queryBuilder().orderAsc(UserDao.Properties.Name).list();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (User record:records) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("id", record.getUsername());
            item.put("name", record.getName());
            data.add(item);
        }
        callback.onSuccess(OrderInPresenter.TYPE_LOAD_DATA_FROMUSERS, data, new BaseResultBean(0, ""));
    }

    @Override
    public void loadMgrs(IConDbListener callback) {
        UserDao table = getUserDao();
        List<User> records = table.queryBuilder().orderAsc(UserDao.Properties.Name).list();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (User record:records) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("id", record.getUsername());
            item.put("name", record.getName());
            data.add(item);
        }
        callback.onSuccess(OrderInPresenter.TYPE_LOAD_DATA_MGRS, data, new BaseResultBean(0, ""));
    }

    private String generateAssetID(String type){
        String result = "";
        AssetsDao table = getAssetsDao();
        int num = 0;
        List<Assets> records = table.queryBuilder().where(AssetsDao.Properties.Clsct.eq(type)).orderDesc(AssetsDao.Properties.Id).list();
        if(records.size() == 0){
            result = type + "000001";
        }
        else{

            for(Assets record : records){
                String strnum = record.getId().substring(record.getId().length() - 6, record.getId().length());
                int tempnum = Integer.valueOf(strnum).intValue();
                if(num < tempnum){
                    num = tempnum;
                }
            }
            num++;
            result = type + CommonUtils.genNum(num + "", 6);
        }
        boolean valid = false;
        //增加容错能力，确保ID唯一
        while(!valid){
            if(table.queryBuilder().where(AssetsDao.Properties.Id.eq(result)).list().size() > 0){
                num++;
            }
            else{
                valid = true;
            }
            result = type + CommonUtils.genNum(num + "", 6);
        }
        return result;
    }

    /*获取各种Table引用*/
    private UserDao getUserDao(){
        return GreenDaoManager.getInstance().getSession().getUserDao();
    }
    private WareHouseDao getWareHouseDao(){
        return GreenDaoManager.getInstance().getSession().getWareHouseDao();
    }
    private Order_InDao getOrderInDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_InDao();
    }
    private Order_In_DetailDao getOrderInDetailDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_In_DetailDao();
    }
    private AssetsDao getAssetsDao(){
        return GreenDaoManager.getInstance().getSession().getAssetsDao();
    }
}
