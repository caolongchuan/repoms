package cn.reebtech.repoms.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.reebtech.repoms.bean.LoginBean;
import cn.reebtech.repoms.model.entity.Assets;
import cn.reebtech.repoms.model.entity.IDGenerator;
import cn.reebtech.repoms.model.entity.Server;
import cn.reebtech.repoms.model.entity.User;
import cn.reebtech.repoms.model.greendao.AssetsDao;
import cn.reebtech.repoms.model.greendao.IDGeneratorDao;
import cn.reebtech.repoms.model.greendao.ServerDao;
import cn.reebtech.repoms.model.greendao.UserDao;

public class DBUtils {
    public static String generateID(String type){
        String result = "";
        IDGeneratorDao table = getIDGenerateDao();
        SimpleDateFormat dformat=new SimpleDateFormat("yyyyMMdd");
        List<IDGenerator> records = table.queryBuilder().where(IDGeneratorDao.Properties.Type.eq(type)).orderDesc(IDGeneratorDao.Properties.Date).list();
        for(int i = 1; i < records.size(); i++){
            table.delete(records.get(i));
        }
        IDGenerator record = table.queryBuilder()
                .where(IDGeneratorDao.Properties.Type.eq(type)).unique();
        Date today = new Date();
        if(record != null){
            if(dformat.format(today).equals(dformat.format(record.getDate()))){
                record.setCurnum(record.getCurnum() + 1);
                result = CommonUtils.genNum(String.valueOf(record.getCurnum()), 4);
            }
            else{
                record.setDate(today);
                record.setCurnum(1);
                result = "0001";
            }
        }
        else{
            record = new IDGenerator();
            record.setType(type);
            record.setDate(today);
            record.setCurnum(1);
            result = "0001";
        }
        table.save(record);
        return type + dformat.format(today) + result;
    }
    public static String getSrvBaseUrl(){
        String result = "";
        ServerDao table = getServerDao();
        List<Server> records = table.queryBuilder().orderDesc(ServerDao.Properties.Update).list();
        if(records.size() > 0){
            Server record = records.get(0);
            if(record.getUrl() != null && !record.getUrl().equals("")){
                result = record.getUrl();
            }
        }
        else{
            result = "192.168.1.179:8888";
        }
        return result;
    }
    public static LoginBean getServerLogin(){
        LoginBean result = new LoginBean();
        ServerDao table = getServerDao();
        List<Server> records = table.queryBuilder().list();
        if(records.size() > 0){
            Server record = records.get(0);
            result.setUsrname(record.getUser());
            result.setPasswd(record.getPassword());
        }
        return result;
    }
    public static String genNum(String numStr, int len){
        if(numStr == null || numStr.equals("")){
            return "000001";
        }
        if(numStr.length() == len){
            return numStr;
        }
        StringBuilder result = new StringBuilder();
        result.append(numStr);
        for(int i = numStr.length(); i < len; i++){
            result.insert(0, "0");
        }
        return result.toString();
    }
    public static String generateNameFromId(String id){
        if(id!=null &&!id.equals("") && id.length() > 6){
            int num = Integer.valueOf(id.substring(id.length() - 6, id.length()));
            return "#" + num;
        }
        else{
            return "#";
        }
    }
    public static String getBindWarehouse(String user){
        UserDao table = getUserDao();
        cn.reebtech.repoms.model.entity.User record = table.queryBuilder().where(UserDao.Properties.Username.eq(user)).unique();
        if(record != null){
            return record.getWarehouse();
        }
        return "";
    }
    private static IDGeneratorDao getIDGenerateDao(){
        return GreenDaoManager.getInstance().getSession().getIDGeneratorDao();
    }
    private static AssetsDao getAssetDao(){
        return GreenDaoManager.getInstance().getSession().getAssetsDao();
    }
    private static ServerDao getServerDao(){
        return GreenDaoManager.getInstance().getSession().getServerDao();
    }

    private static UserDao getUserDao() {
        return GreenDaoManager.getInstance().getSession().getUserDao();
    }
}
