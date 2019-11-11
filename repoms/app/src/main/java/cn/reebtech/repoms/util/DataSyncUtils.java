package cn.reebtech.repoms.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import cn.reebtech.repoms.model.entity.AssetClsct;
import cn.reebtech.repoms.model.entity.Assets;
import cn.reebtech.repoms.model.entity.Department;
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
import cn.reebtech.repoms.model.entity.WareHouse;
import cn.reebtech.repoms.model.greendao.AssetClsctDao;
import cn.reebtech.repoms.model.greendao.AssetsDao;
import cn.reebtech.repoms.model.greendao.DepartmentDao;
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
import cn.reebtech.repoms.model.greendao.WareHouseDao;

public class DataSyncUtils {
    public void saveDownloadData(JSONObject data){
        try{
            JSONArray items = data.getJSONArray("result");
            //Log.i("开始插入数据-", "1" + data.getString("type") + "记录条数-"+items.length());
            switch(data.getString("type")){
                case "asset":
                    AssetsDao tb_asset = getAssetDao();
                    if(items.length() > 0){
                        //清空数据
                        tb_asset.deleteAll();
                    }
                    for(int i = 0; i < items.length(); i++){
                        JSONObject item = (JSONObject) items.get(i);
                        //加入新数据
                        Assets record = new Assets();
                        record.setId(item.get("id").toString());
                        record.setName(item.get("name").toString());
                        record.setClsct(item.get("cId").toString());
                        record.setLocation(item.get("whId").toString());
                        record.setSpecification(item.get("specification").toString());
                        record.setManut(item.get("manufacturer").toString());
                        record.setRfid(item.isNull("rfid") ? "" : item.get("rfid").toString());
                        //record.setRfid("");
                        record.setStatus(item.get("state").toString());
                        record.setIndate(CommonUtils.String2Date(item.get("createTime").toString()));
                        record.setLastupdate(CommonUtils.String2Date(item.get("updateTime").toString()));
                        record.setPrice(Double.valueOf(item.get("unitPrice").toString()).doubleValue());
                        tb_asset.save(record);
                    }
                    Log.i("物资信息", "保存成功");
                    break;
                case "user":
                    UserDao tb_user = getUserDao();
                    if(items.length() > 0){
                        //清空数据
                        tb_user.deleteAll();
                    }
                    for(int i = 0; i < items.length(); i++){
                        JSONObject item = (JSONObject) items.get(i);
                        //加入新数据
                        User record = new User();
                        record.setId(item.get("id").toString());
                        record.setPassword("1");
                        record.setDept(item.get("departmentId").toString());
                        Log.i("user:", item.get("username").toString());
                        record.setUsername(item.get("username").toString());
                        record.setName(item.get("nickName").toString());
                        record.setWarehouse(item.get("wh_id").toString());
                        tb_user.save(record);
                    }
                    Log.i("用户信息", "保存成功");
                    break;
                case "dept":
                    DepartmentDao tb_dept = getDeptDao();
                    if(items.length() > 0){
                        //清空数据
                        tb_dept.deleteAll();
                    }
                    for(int i = 0; i < items.length(); i++){
                        JSONObject item = (JSONObject) items.get(i);
                        //加入新数据
                        Department record = new Department();
                        record.setId(item.get("id").toString());
                        record.setName(item.get("title").toString());
                        record.setLayer(0);
                        record.setParent(item.get("parentId").toString());
                        tb_dept.save(record);
                    }
                    Log.i("部门信息", "保存成功");
                    break;
                case "aclst":
                    AssetClsctDao tb_aclsct = getAssetClsctDao();
                    if(items.length() > 0){
                        //清空数据
                        tb_aclsct.deleteAll();
                    }
                    for(int i = 0; i < items.length(); i++){
                        JSONObject item = (JSONObject) items.get(i);
                        //加入新数据
                        AssetClsct record = new AssetClsct();
                        record.setId(item.get("id").toString());
                        record.setName(item.get("title").toString());
                        record.setLayer(0);
                        record.setParent(item.get("parentId").toString());
                        record.setLocation(item.get("whId").toString());
                        tb_aclsct.save(record);

                    }
                    Log.i("物资分类信息", "保存成功");
                    break;
                case "whouse":
                    WareHouseDao tb_whouse = getWareHouseDao();
                    if(items.length() > 0){
                        //清空数据
                        tb_whouse.deleteAll();
                    }
                    for(int i = 0; i < items.length(); i++){
                        JSONObject item = (JSONObject) items.get(i);
                        //加入新数据
                        WareHouse record = new WareHouse();
                        record.setId(item.get("id").toString());
                        record.setName(item.get("title").toString());
                        record.setLayer(0);
                        record.setParent(item.get("parentId").toString());
                        tb_whouse.save(record);
                    }
                    Log.i("仓库信息", "保存成功");
                    break;
            }
        }
        catch(Exception e){
            Log.i("错误信息", "保存下载信息出错：" + e.getMessage());
        }
    }
    public JSONArray readUploadData(String type){
        JSONArray data = new JSONArray();
        switch(type){
            case "order_in": //remark字段0表示新物资入库，1表示物资归还入库
                //新物资入库单
                Order_InDao table_order_in = getOrderInDao();
                Order_In_DetailDao table_order_in_detail = getOrderInDetailDao();
                List<Order_In> orders = table_order_in.queryBuilder().where(Order_InDao.Properties.Uploaded.eq(false)).list();
                for (Order_In order : orders) {
                    JSONObject item = new JSONObject();
                    try {
                        item.put("whId", order.getLocation());
                        item.put("inDate", CommonUtils.parseDate2String(order.getIndate()));
                        item.put("inUser", order.getUsersend());
                        item.put("whManage", order.getUsermgr());
                        item.put("inNum", order.getIncount());
                        item.put("money", order.getInprice());
                        item.put("remark", 0);
                        JSONArray subitems = new JSONArray();
                        List<Order_In_Detail> order_details = table_order_in_detail.queryBuilder().where(Order_In_DetailDao.Properties.Order.eq(order.getId())).list();
                        for (Order_In_Detail order_item : order_details) {
                            JSONObject subitem = new JSONObject();
                            Assets assets = getAssets(order_item.getAsset());
                            subitem.put("name", order_item.getName());
                            subitem.put("specification", order_item.getSpecification());
                            subitem.put("manufacturer", order_item.getManut());
                            subitem.put("unitPrice", order_item.getPrice());
                            subitem.put("state", assets.getStatus());
                            subitem.put("updateTime", CommonUtils.parseDate2String(assets.getLastupdate()));
                            subitem.put("cId", order_item.getClsct());
                            subitem.put("rfid", order_item.getRfid());
                            subitem.put("num", order_item.getCount());
                            subitem.put("remark", "");

                            subitem.put("asset_code",order_item.getAsset_code());//clc 资产编码

                            subitems.put(subitem);
                        }
                        item.put("materials", subitems);
                        data.put(item);
                    }
                    catch (Exception e) {

                    }
                }
                //物资归还单
                Order_RetDao table_order_ret = getOrderRetDao();
                Order_Ret_DetailDao table_order_ret_detail = getOrderRetDetailDao();
                List<Order_Ret> orders_ret = table_order_ret.queryBuilder().where(Order_RetDao.Properties.Uploaded.eq(false)).list();
                for (Order_Ret order : orders_ret) {
                    JSONObject item = new JSONObject();
                    try {
                        item.put("whId", order.getLocation());
                        item.put("inDate", CommonUtils.parseDate2String(order.getRetdate()));
                        item.put("inUser", order.getUserret());
                        item.put("whManage", order.getUsermgr());
                        item.put("inNum", order.getIncount());
                        item.put("money", order.getInprice());
                        item.put("remark", 1);
                        JSONArray subitems = new JSONArray();
                        List<Order_Ret_Detail> order_details = table_order_ret_detail.queryBuilder().where(Order_Ret_DetailDao.Properties.Order.eq(order.getId())).list();
                        for (Order_Ret_Detail order_item : order_details) {
                            JSONObject subitem = new JSONObject();
                            subitem.put("name", order_item.getAsset());
                            subitem.put("specification", "");
                            subitem.put("manufacturer", "");
                            subitem.put("unitPrice", order_item.getPrice());
                            subitem.put("state", "1");
                            subitem.put("cId", "");
                            subitem.put("num", order_item.getCount());
                            subitem.put("rfid", order_item.getAsset());
                            subitem.put("updateTime", CommonUtils.parseDate2String(order.getRetdate()));
                            subitem.put("remark", "");
                            subitems.put(subitem);
                        }
                        item.put("materials", subitems);
                        data.put(item);
                    }
                    //物资归还入库单
                    catch (Exception e) {

                    }
                }
                break;
            case "order_out":
                //物资出库单
                Order_OutDao table_order_out = getOrderOutDao();
                Order_Out_DetailDao table_order_out_detail = getOrderOutDetailDao();
                List<Order_Out> orders_out = table_order_out.queryBuilder().where(Order_OutDao.Properties.Uploaded.eq(false)).list();
                for (Order_Out order : orders_out) {
                    JSONObject item = new JSONObject();
                    try {
                        item.put("whId", order.getLocation());
                        item.put("outDate", CommonUtils.parseDate2String(order.getOutdate()));
                        item.put("outUser", order.getUserrec());
                        item.put("whManage", order.getUsermgr());
                        item.put("outNum", order.getOutcount());
                        item.put("money", order.getOutprice());
                        item.put("remark", 0);
                        JSONArray subitems = new JSONArray();
                        List<Order_Out_Detail> order_out_details = table_order_out_detail.queryBuilder().where(Order_Out_DetailDao.Properties.Order.eq(order.getId())).list();
                        for (Order_Out_Detail order_item : order_out_details) {
                            JSONObject subitem = new JSONObject();
                            subitem.put("rfid", order_item.getAsset());
                            subitem.put("num", order_item.getCount());
                            subitem.put("money", order_item.getPrice());
                            subitem.put("updateTime", CommonUtils.parseDate2String(order.getOutdate()));
                            subitem.put("remark", "");
                            subitems.put(subitem);
                        }
                        item.put("materials", subitems);
                        data.put(item);
                    }
                    catch (Exception e) {
                        Log.i("read order_out error", e.getMessage());
                    }
                }
                Order_ReqDao table_order_req = getOrderReqDao();
                Order_Req_DetailDao table_order_req_detail = getOrderReqDetailDao();
                List<Order_Req> orders_req = table_order_req.queryBuilder().where(Order_ReqDao.Properties.Uploaded.eq(false)).list();
                for (Order_Req order : orders_req) {
                    JSONObject item = new JSONObject();
                    try {
                        item.put("whId", order.getId());
                        item.put("outDate", CommonUtils.parseDate2String(order.getReqdate()));
                        item.put("outUser", order.getUserreq());
                        item.put("whManage", order.getUsermgr());
                        item.put("outNum", order.getOutcount());
                        item.put("money", order.getOutprice());
                        item.put("remark", 0);
                        JSONArray subitems = new JSONArray();
                        List<Order_Req_Detail> order_req_details = table_order_req_detail.queryBuilder().where(Order_Req_DetailDao.Properties.Order.eq(order.getId())).list();
                        for (Order_Req_Detail order_item : order_req_details) {
                            JSONObject subitem = new JSONObject();
                            subitem.put("rfid", order_item.getAsset());
                            subitem.put("num", order_item.getCount());
                            subitem.put("money", order_item.getPrice());
                            subitem.put("updateTime", CommonUtils.parseDate2String(order.getReqdate()));
                            subitem.put("remark", "");
                            subitems.put(subitem);
                        }
                        item.put("materials", subitems);
                        data.put(item);
                    }
                    catch (Exception e) {

                    }
                }
                break;
            case "order_invt":
                Order_InvtDao table_order_invt = getOrderInvtDao();
                Order_Invt_DetailDao table_order_invt_detail = getOrderInvtDetailDao();
                List<Order_Invt> orders_invt = table_order_invt.queryBuilder().where(Order_InvtDao.Properties.Uploaded.eq(false)).list();
                for (Order_Invt order : orders_invt) {
                    JSONObject item = new JSONObject();
                    try {
                        item.put("whId", order.getLocation());
                        item.put("checkDate", CommonUtils.parseDate2String(order.getInvdate()));
                        item.put("checkUser", order.getInvuser());
                        item.put("isFinish", 1);
                        item.put("isUpload", 1);
                        item.put("remark", 0);
                        JSONArray subitems = new JSONArray();
                        List<Order_Invt_Detail> order_invt_details = table_order_invt_detail.queryBuilder().where(Order_Invt_DetailDao.Properties.Order.eq(order.getId())).list();
                        for (Order_Invt_Detail order_item : order_invt_details) {
                            JSONObject subitem = new JSONObject();
                            subitem.put("rfid", order_item.getAsset());
                            subitem.put("remark", "");
                            subitems.put(subitem);
                        }
                        item.put("materials", subitems);
                        data.put(item);
                    }
                    catch (Exception e) {
                        Log.i("read order_out error", e.getMessage());
                    }
                }
                break;
        }
        return data;
    }
    public boolean chkDlvalid(){
        Order_InvtDao tb_order_invt = getOrderInvtDao();
        if(tb_order_invt.queryBuilder().where(Order_InvtDao.Properties.Uploaded.eq(false)).list().size() > 0){
            return false;
        }
        Order_InDao tb_order_in = getOrderInDao();
        if(tb_order_in.queryBuilder().where(Order_InDao.Properties.Uploaded.eq(false)).list().size() > 0){
            return false;
        }
        Order_OutDao tb_order_out = getOrderOutDao();
        if(tb_order_out.queryBuilder().where(Order_OutDao.Properties.Uploaded.eq(false)).list().size() > 0){
            return false;
        }
        Order_ReqDao tb_order_req = getOrderReqDao();
        if(tb_order_req.queryBuilder().where(Order_ReqDao.Properties.Uploaded.eq(false)).list().size() > 0){
            return false;
        }
        Order_RetDao tb_order_ret = getOrderRetDao();
        if(tb_order_ret.queryBuilder().where(Order_RetDao.Properties.Uploaded.eq(false)).list().size() > 0){
            return false;
        }
        return true;
    }
    public void updateOrderList(String type){
        switch(type){
            case "order_in":
                //新物资
                Order_InDao table_order_in = getOrderInDao();
                Order_In_DetailDao table_order_in_detail = getOrderInDetailDao();
                List<Order_In> records_in = table_order_in.queryBuilder().list();
                for(Order_In record : records_in){
                    record.setUploaded(true);
                    record.setCompleted(true);
                    table_order_in.save(record);
                }
                List<Order_In_Detail> records_in_detail = table_order_in_detail.queryBuilder().list();
                for(Order_In_Detail record : records_in_detail){
                    record.setUploaded(true);
                    record.setCompleted(true);
                    table_order_in_detail.save(record);
                }
                //归还物资
                Order_RetDao table_order_ret = getOrderRetDao();
                Order_Ret_DetailDao table_order_ret_detail = getOrderRetDetailDao();
                List<Order_Ret> records_ret = table_order_ret.queryBuilder().list();
                for(Order_Ret record : records_ret){
                    record.setUploaded(true);
                    record.setCompleted(true);
                    table_order_ret.save(record);
                }
                List<Order_Ret_Detail> records_ret_detail = table_order_ret_detail.queryBuilder().list();
                for(Order_Ret_Detail record:records_ret_detail){
                    record.setUploaded(true);
                    record.setCompleted(true);
                    table_order_ret_detail.save(record);
                }
                Log.i("data update_order_in", "success");
                break;
            case "order_out":
                //新物资
                Order_OutDao table_order_out = getOrderOutDao();
                Order_Out_DetailDao table_order_out_detail = getOrderOutDetailDao();
                List<Order_Out> records_out = table_order_out.queryBuilder().list();
                for(Order_Out record : records_out){
                    record.setUploaded(true);
                    record.setCompleted(true);
                    table_order_out.save(record);
                }
                List<Order_Out_Detail> records_out_detail = table_order_out_detail.queryBuilder().list();
                for(Order_Out_Detail record : records_out_detail){
                    record.setUploaded(true);
                    record.setCompleted(true);
                    table_order_out_detail.save(record);
                }
                //借用物资
                Order_ReqDao table_order_req = getOrderReqDao();
                Order_Req_DetailDao table_order_req_detail = getOrderReqDetailDao();
                List<Order_Req> records_req = table_order_req.queryBuilder().list();
                for(Order_Req record : records_req){
                    record.setUploaded(true);
                    record.setCompleted(true);
                    table_order_req.save(record);
                }
                List<Order_Req_Detail> records_req_detail = table_order_req_detail.queryBuilder().list();
                for(Order_Req_Detail record:records_req_detail){
                    record.setUploaded(true);
                    record.setCompleted(true);
                    table_order_req_detail.save(record);
                }
                Log.i("data update_order_out", "success");
                break;
            case "order_invt":
                Order_InvtDao table_order_invt = getOrderInvtDao();
                Order_Invt_DetailDao table_order_invt_detail = getOrderInvtDetailDao();
                List<Order_Invt> records_invt = table_order_invt.queryBuilder().list();
                for(Order_Invt record : records_invt){
                    record.setUploaded(true);
                    record.setCompleted(true);
                    table_order_invt.save(record);
                }
                List<Order_Invt_Detail> records_invt_detail = table_order_invt_detail.queryBuilder().list();
                for(Order_Invt_Detail record:records_invt_detail){
                    record.setUploaded(true);
                    record.setCompleted(true);
                    table_order_invt_detail.save(record);
                }
                Log.i("data update_order_invt", "success");
                break;
        }
    }
    private Assets getAssets(String id){
        Assets result = null;
        if(id != null && !id.equals("")){
            AssetsDao table = getAssetDao();
            result = table.queryBuilder().where(AssetsDao.Properties.Id.eq(id)).unique();
        }
        return result;
    }
    /*获取数据库数据表对象*/
    private UserDao getUserDao(){
        return GreenDaoManager.getInstance().getSession().getUserDao();
    }
    private DepartmentDao getDeptDao(){
        return GreenDaoManager.getInstance().getSession().getDepartmentDao();
    }
    private AssetClsctDao getAssetClsctDao(){
        return GreenDaoManager.getInstance().getSession().getAssetClsctDao();
    }
    private AssetsDao getAssetDao(){
        return GreenDaoManager.getInstance().getSession().getAssetsDao();
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
}
