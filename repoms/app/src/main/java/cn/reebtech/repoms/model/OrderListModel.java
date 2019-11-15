package cn.reebtech.repoms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.OrderOutBean;
import cn.reebtech.repoms.contact.OrderListContact;
import cn.reebtech.repoms.contact.OrderOutContact;
import cn.reebtech.repoms.model.entity.Assets;
import cn.reebtech.repoms.model.entity.Order_In;
import cn.reebtech.repoms.model.entity.Order_In_Detail;
import cn.reebtech.repoms.model.entity.Order_Invt;
import cn.reebtech.repoms.model.entity.Order_Invt_Detail;
import cn.reebtech.repoms.model.entity.Order_Out;
import cn.reebtech.repoms.model.entity.Order_Out_Detail;
import cn.reebtech.repoms.model.entity.Order_Req;
import cn.reebtech.repoms.model.entity.Order_Req_Detail;
import cn.reebtech.repoms.model.entity.Order_Ret;
import cn.reebtech.repoms.model.entity.Order_Ret_Detail;
import cn.reebtech.repoms.model.entity.User;
import cn.reebtech.repoms.model.entity.WaitBindAssets;
import cn.reebtech.repoms.model.entity.WareHouse;
import cn.reebtech.repoms.model.greendao.AssetsDao;
import cn.reebtech.repoms.model.greendao.Order_InDao;
import cn.reebtech.repoms.model.greendao.Order_In_DetailDao;
import cn.reebtech.repoms.model.greendao.Order_InvtDao;
import cn.reebtech.repoms.model.greendao.Order_Invt_DetailDao;
import cn.reebtech.repoms.model.greendao.Order_OutDao;
import cn.reebtech.repoms.model.greendao.Order_Out_DetailDao;
import cn.reebtech.repoms.model.greendao.Order_ReqDao;
import cn.reebtech.repoms.model.greendao.Order_Req_DetailDao;
import cn.reebtech.repoms.model.greendao.Order_RetDao;
import cn.reebtech.repoms.model.greendao.Order_Ret_DetailDao;
import cn.reebtech.repoms.model.greendao.UserDao;
import cn.reebtech.repoms.model.greendao.WaitBindAssetsDao;
import cn.reebtech.repoms.model.greendao.WareHouseDao;
import cn.reebtech.repoms.presenter.OrderInPresenter;
import cn.reebtech.repoms.presenter.OrderListPresenter;
import cn.reebtech.repoms.presenter.OrderOutPresenter;
import cn.reebtech.repoms.util.CommonUtils;
import cn.reebtech.repoms.util.DBUtils;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;

public class OrderListModel implements OrderListContact.OrderListMdl {
    @Override
    public void loadData(int type, IConDbListener callback) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        switch(type){
            case OrderListPresenter.TYPE_ORDER_IN:
                Order_InDao table_in = getOrderInDao();
                List<Order_In> records_in = table_in.queryBuilder().where(Order_InDao.Properties.Uploaded.eq(false)).orderDesc(Order_InDao.Properties.Indate).list();
                for (Order_In record: records_in) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("id", record.getId());
                    item.put("whouse", getWareHouseName(record.getLocation()));
                    item.put("acount", "" + record.getIncount());
                    item.put("odate", "" + CommonUtils.parseDateToString(record.getIndate()));
                    item.put("cont", getUserName(record.getUsersend()));
                    item.put("mgr", getUserName(record.getUsermgr()));
                    data.add(item);
                }
                break;
            case OrderListPresenter.TYPE_ORDER_OUT:
                Order_OutDao table_out = getOrderOutDao();
                List<Order_Out> records_out = table_out.queryBuilder().where(Order_OutDao.Properties.Uploaded.eq(false)).orderDesc(Order_OutDao.Properties.Outdate).list();
                for (Order_Out record: records_out) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("id", record.getId());
                    item.put("whouse", getWareHouseName(record.getLocation()));
                    item.put("acount", "" + record.getOutcount());
                    item.put("odate", "" + CommonUtils.parseDateToString(record.getOutdate()));
                    item.put("cont", getUserName(record.getUserrec()));
                    item.put("mgr", getUserName(record.getUsermgr()));
                    data.add(item);
                }
                break;
            case OrderListPresenter.TYPE_ORDER_REQ:
                Order_ReqDao table_req = getOrderReqDao();
                List<Order_Req> records_req = table_req.queryBuilder().where(Order_ReqDao.Properties.Uploaded.eq(false)).orderDesc(Order_ReqDao.Properties.Reqdate).list();
                for (Order_Req record: records_req) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("id", record.getId());
                    item.put("whouse", getWareHouseName(record.getLocation()));
                    item.put("acount", "" + record.getOutcount());
                    item.put("odate", "" + CommonUtils.parseDateToString(record.getReqdate()));
                    item.put("cont", getUserName(record.getUserreq()));
                    item.put("mgr", getUserName(record.getUsermgr()));
                    data.add(item);
                }
                break;
            case OrderListPresenter.TYPE_ORDER_RET:
                Order_RetDao table_ret = getOrderRetDao();
                List<Order_Ret> records_ret = table_ret.queryBuilder().where(Order_RetDao.Properties.Uploaded.eq(false)).orderDesc(Order_RetDao.Properties.Retdate).list();
                for (Order_Ret record: records_ret) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("id", record.getId());
                    item.put("whouse", getWareHouseName(record.getLocation()));
                    item.put("acount", "" + record.getIncount());
                    item.put("odate", "" + CommonUtils.parseDateToString(record.getRetdate()));
                    item.put("cont", getUserName(record.getUserret()));
                    item.put("mgr", getUserName(record.getUsermgr()));
                    data.add(item);
                }
                break;
            case OrderListPresenter.TYPE_ORDER_INV:
                Order_InvtDao table_inv = getOrderInvtDao();
                List<Order_Invt> records_inv = table_inv.queryBuilder().where(Order_InvtDao.Properties.Uploaded.eq(false)).orderDesc(Order_InvtDao.Properties.Invdate).list();
                for(Order_Invt record: records_inv){
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("id", record.getId());
                    item.put("whouse", getWareHouseName(record.getLocation()));
                    item.put("acount", record.getInvcount());
                    item.put("mgr", record.getInvuser());
                    item.put("odate", CommonUtils.parseDateToString(record.getInvdate()));
                    data.add(item);
                }
                break;
            case OrderListPresenter.TYPE_WAIT_BINDING:
                WaitBindAssetsDao table_waitbind = getWaitBindAssetsDao();
                List<WaitBindAssets> list = table_waitbind.queryBuilder().list();
                for(WaitBindAssets record: list){
                    Map<String, Object> item = new HashMap<String, Object>();
//                    item.put("id", record.getId());
//                    item.put("whouse", getWareHouseName(record.getLocation()));
//                    item.put("acount", record.getInvcount());
//                    item.put("mgr", record.getInvuser());
//                    item.put("odate", CommonUtils.parseDateToString(record.getInvdate()));
                    item.put("asset_code",record.getAsset_code());
                    item.put("rfid",record.getRfid());
                    data.add(item);
                }
                break;
        }
        callback.onSuccess(type, data, new BaseResultBean(0, ""));
    }

    @Override
    public void deleteData(int type, String id, IConDbListener callback) {
        if(id == null || id.equals("")){
            callback.onFailure(type, new BaseResultBean(101, "单据标示无效"));
        }
        switch(type){
            case OrderListPresenter.TYPE_ORDER_IN:
                Order_InDao table_in = getOrderInDao();
                Order_In record_in = table_in.queryBuilder().where(Order_InDao.Properties.Id.eq(id)).unique();
                if(record_in != null){
                    table_in.delete(record_in);
                    //删除子项
                    Order_In_DetailDao table_in_dt = getOrderInDetailDao();
                    List<Order_In_Detail> records_in_dt = table_in_dt.queryBuilder().where(Order_In_DetailDao.Properties.Order.eq(id)).list();
                    table_in_dt.deleteInTx(records_in_dt);
                }
                else{
                    callback.onFailure(type, new BaseResultBean(102, "未找到单据信息"));
                    return;
                }
                break;
            case OrderListPresenter.TYPE_ORDER_OUT:
                Order_OutDao table_out = getOrderOutDao();
                Order_Out record_out = table_out.queryBuilder().where(Order_OutDao.Properties.Id.eq(id)).unique();
                if(record_out != null){
                    table_out.delete(record_out);
                    //删除子项
                    Order_Out_DetailDao table_out_dt = getOrderOutDetailDao();
                    List<Order_Out_Detail> records_out_dt = table_out_dt.queryBuilder().where(Order_Out_DetailDao.Properties.Order.eq(id)).list();
                    table_out_dt.deleteInTx(records_out_dt);
                }
                else{
                    callback.onFailure(type, new BaseResultBean(102, "未找到单据信息"));
                    return;
                }
                break;
            case OrderListPresenter.TYPE_ORDER_REQ:
                Order_ReqDao table_req = getOrderReqDao();
                Order_Req record_req = table_req.queryBuilder().where(Order_ReqDao.Properties.Id.eq(id)).unique();
                if(record_req != null){
                    table_req.delete(record_req);
                    //删除子项
                    Order_Req_DetailDao table_req_dt = getOrderReqDetailDao();
                    List<Order_Req_Detail> records_req_dt = table_req_dt.queryBuilder().where(Order_Req_DetailDao.Properties.Order.eq(id)).list();
                    table_req_dt.deleteInTx(records_req_dt);
                }
                else{
                    callback.onFailure(type, new BaseResultBean(102, "未找到单据信息"));
                    return;
                }
                break;
            case OrderListPresenter.TYPE_ORDER_RET:
                Order_RetDao table_ret = getOrderRetDao();
                Order_Ret record_ret = table_ret.queryBuilder().where(Order_RetDao.Properties.Id.eq(id)).unique();
                if(record_ret != null){
                    table_ret.delete(record_ret);
                    //删除子项
                    Order_Ret_DetailDao table_ret_dt = getOrderRetDetailDao();
                    List<Order_Ret_Detail> records_ret_dt = table_ret_dt.queryBuilder().where(Order_Ret_DetailDao.Properties.Order.eq(id)).list();
                    table_ret_dt.deleteInTx(records_ret_dt);
                }
                else{
                    callback.onFailure(type, new BaseResultBean(102, "未找到单据信息"));
                    return;
                }
                break;
            case OrderListPresenter.TYPE_ORDER_INV:
                Order_InvtDao table_inv = getOrderInvtDao();
                Order_Invt record_inv = table_inv.queryBuilder().where(Order_InvtDao.Properties.Id.eq(id)).unique();
                if(record_inv != null){
                    table_inv.delete(record_inv);
                    //删除子项
                    Order_Invt_DetailDao table_inv_dt = getOrderInvtDetailDao();
                    List<Order_Invt_Detail> records_inv_dt = table_inv_dt.queryBuilder().where(Order_Invt_DetailDao.Properties.Order.eq(id)).list();
                    table_inv_dt.deleteInTx(records_inv_dt);
                }
                else{
                    callback.onFailure(type, new BaseResultBean(102, "未找到单据信息"));
                    return;
                }
                break;
        }
        callback.onSuccess(OrderListPresenter.TYPE_DELETE_ITEM, null, new BaseResultBean(0, ""));
    }

    private String getWareHouseName(String id){
        WareHouseDao table = getWareHouseDao();
        WareHouse record = table.queryBuilder().where(WareHouseDao.Properties.Id.eq(id)).unique();
        return record == null ? "" : record.getName();
    }

    private String getUserName(String id){
        UserDao table = getUserDao();
        User record = table.queryBuilder().where(UserDao.Properties.Username.eq(id)).unique();
        return record == null ? "" : record.getName();
    }

    /*获取各Table的引用*/
    private WareHouseDao getWareHouseDao(){
        return GreenDaoManager.getInstance().getSession().getWareHouseDao();
    }
    private UserDao getUserDao(){
        return GreenDaoManager.getInstance().getSession().getUserDao();
    }
    private Order_InDao getOrderInDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_InDao();
    }
    private Order_In_DetailDao getOrderInDetailDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_In_DetailDao();
    }
    private Order_OutDao getOrderOutDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_OutDao();
    }
    private Order_Out_DetailDao getOrderOutDetailDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_Out_DetailDao();
    }
    private Order_ReqDao getOrderReqDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_ReqDao();
    }
    private Order_Req_DetailDao getOrderReqDetailDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_Req_DetailDao();
    }
    private Order_RetDao getOrderRetDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_RetDao();
    }
    private Order_Ret_DetailDao getOrderRetDetailDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_Ret_DetailDao();
    }
    private Order_InvtDao getOrderInvtDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_InvtDao();
    }
    private Order_Invt_DetailDao getOrderInvtDetailDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_Invt_DetailDao();
    }

    private WaitBindAssetsDao getWaitBindAssetsDao(){
        return GreenDaoManager.getInstance().getSession().getWaitBindAssetsDao();
    }

}
