package cn.reebtech.repoms.model;

import android.util.Log;

import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.LoginBean;
import cn.reebtech.repoms.contact.MainContact;
import cn.reebtech.repoms.model.entity.AssetClsct;
import cn.reebtech.repoms.model.entity.LastLogin;
import cn.reebtech.repoms.model.entity.User;
import cn.reebtech.repoms.model.entity.WareHouse;
import cn.reebtech.repoms.model.greendao.AssetClsctDao;
import cn.reebtech.repoms.model.greendao.LastLoginDao;
import cn.reebtech.repoms.model.greendao.UserDao;
import cn.reebtech.repoms.model.greendao.WareHouseDao;
import cn.reebtech.repoms.presenter.MainPresenter;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;

public class MainModel extends BaseDBModel<User, String, IConDbListener<Integer, LoginBean, BaseResultBean>> implements MainContact.MainMdl {

    @Override
    public void logout(IConDbListener callback) {
        LastLoginDao table = getLastLoginDao();
        if(table.count() > 0){
            LastLogin record = table.queryBuilder().unique();
            record.setAutologin(false);
            record.setRememberme(true);
            table.update(record);
        }
        callback.onSuccess(MainPresenter.OP_LOGOUT, null, new BaseResultBean(0, ""));
    }
    @Override
    public void tmpInitData(int type){
        switch(type){
            case 1:
                AssetClsctDao table = getAssetClsctDao();
                table.deleteAll();
                for(int i = 0; i < 10; i++){
                    AssetClsct record = new AssetClsct();
                    record.setId("WZDL000" + i);
                    record.setName("物资大类" + i);
                    record.setLayer(1);
                    record.setParent("");
                    table.insert(record);
                    for(int j = 0; j < 5; j++){
                        AssetClsct record1 = new AssetClsct();
                        record1.setId("WZXL000" + i + ":" + j);
                        record1.setName("物资小类" + i + ":" + j);
                        record1.setLayer(2);
                        record1.setParent("WZDL000" + i);
                        table.insert(record1);
                    }
                }
                Log.i("onSuccess", "物资分类-数据初始化成功");
                break;
            case 2:
                WareHouseDao wtable = getWarehouseDao();
                wtable.deleteAll();
                for(int i = 1; i <= 10; i++){
                    WareHouse record = new WareHouse();
                    record.setId("CGK00" + (i < 10 ? "0" : "") + i);
                    record.setName(i + "#仓库");
                    record.setLayer(1);
                    record.setParent("");
                    wtable.save(record);
                }
                Log.i("onSuccess", "仓库-数据初始化成功");
                break;
            case 3:
                UserDao utable = getUserDao();
                User record = new User();

                record = new User();
                record.setId("admin");
                record.setName("仓库管理员");
                record.setPassword("1");
                record.setUsername("admin");
                utable.save(record);
                record = new User();
                record.setId("usersend");
                record.setName("送货人");
                record.setPassword("1");
                record.setUsername("usersend");
                utable.save(record);
                record = new User();
                record.setId("userrec");
                record.setName("收货人");
                record.setPassword("1");
                record.setUsername("userrec");
                utable.save(record);
                Log.i("onSuccess", "用户-数据初始化成功");
                break;
        }

    }
    /*
     * 获取上次登录信息
     * */
    private LastLoginDao getLastLoginDao(){
        return GreenDaoManager.getInstance().getSession().getLastLoginDao();
    }

    private AssetClsctDao getAssetClsctDao(){
        return GreenDaoManager.getInstance().getSession().getAssetClsctDao();
    }
    private WareHouseDao getWarehouseDao(){
        return GreenDaoManager.getInstance().getSession().getWareHouseDao();
    }
    private UserDao getUserDao(){
        return GreenDaoManager.getInstance().getSession().getUserDao();
    }

}
