package cn.reebtech.repoms.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.contact.AllocateContact;
import cn.reebtech.repoms.model.entity.AssetClsct;
import cn.reebtech.repoms.model.entity.WareHouse;
import cn.reebtech.repoms.model.greendao.AssetClsctDao;
import cn.reebtech.repoms.model.greendao.WareHouseDao;
import cn.reebtech.repoms.presenter.AllocatePresenter;
import cn.reebtech.repoms.presenter.LabelBendingPresenter;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;

public class AllocateModel extends BaseDBModel<AssetClsct, String, IConDbListener<Integer, AssetBean, BaseResultBean>>
        implements AllocateContact.AllocateMdl  {

    public AllocateModel(AllocateContact.AllocateUI view){
    }


    @Override
    public void loadBGS(IConDbListener callback) {
        WareHouseDao wareHouseDao = getWareHouseDao();
        try{
            List<WareHouse> records = wareHouseDao.queryBuilder().list();
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            for (WareHouse record: records) {
                Map<String, String> item = new HashMap<String, String>();
                item.put("id", record.getId());
                item.put("name", record.getName());
                data.add(item);
            }
            callback.onSuccess(AllocatePresenter.TYPE_INIT_DATA_BGS, data, new BaseResultBean(0, ""));
        }
        catch(Exception e){
            callback.onFailure(AllocatePresenter.TYPE_INIT_DATA_BGS, new BaseResultBean(AllocatePresenter.ERROR_CODE_LOAD_BGS_FAILED, e.getMessage()));
        }
    }

    @Override
    public void loadAssetClsScd(String parent, IConDbListener callback) {
        AssetClsctDao table = getAssetClsDao();
        try{
            List<AssetClsct> records = table.queryBuilder()
                    .where(AssetClsctDao.Properties.Parent.eq(parent))
                    .orderAsc(AssetClsctDao.Properties.Name)
                    .list();
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            for (AssetClsct record: records) {
                Map<String, String> item = new HashMap<String, String>();
                item.put("id", record.getId());
                item.put("name", record.getName());
                data.add(item);
            }
            callback.onSuccess(AllocatePresenter.TYPE_INIT_DATA_CLSSCD, data, new BaseResultBean(0, ""));
        }
        catch(Exception e){
            callback.onFailure(AllocatePresenter.TYPE_INIT_DATA_CLSSCD, new BaseResultBean(AllocatePresenter.ERROR_CODE_LOAD_CLSSCD_FAILED, e.getMessage()));
        }
    }

    @Override
    public void loadAssetClsFst(IConDbListener callback, String bgs_key) {
        AssetClsctDao table = getAssetClsDao();
        try{
            List<AssetClsct> records = table.queryBuilder()
                    .where(AssetClsctDao.Properties.Parent.eq("0"),AssetClsctDao.Properties.Location.eq(bgs_key))
                    .orderAsc(AssetClsctDao.Properties.Name)
                    .list();
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            for (AssetClsct record: records) {
                Map<String, String> item = new HashMap<String, String>();
                item.put("id", record.getId());
                item.put("name", record.getName());
                data.add(item);
            }
            callback.onSuccess(AllocatePresenter.TYPE_INIT_DATA_CLSFST, data, new BaseResultBean(0, ""));
        }
        catch(Exception e){
            callback.onFailure(AllocatePresenter.TYPE_INIT_DATA_CLSFST, new BaseResultBean(LabelBendingPresenter.ERROR_CODE_LOAD_CLSFST_FAILED, e.getMessage()));
        }
    }

    private WareHouseDao getWareHouseDao(){
        return GreenDaoManager.getInstance().getSession().getWareHouseDao();
    }
    private AssetClsctDao getAssetClsDao(){
        return GreenDaoManager.getInstance().getSession().getAssetClsctDao();
    }


}
