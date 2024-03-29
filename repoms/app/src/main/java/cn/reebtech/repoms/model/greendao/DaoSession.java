package cn.reebtech.repoms.model.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import cn.reebtech.repoms.model.entity.AssetClsct;
import cn.reebtech.repoms.model.entity.Assets;
import cn.reebtech.repoms.model.entity.Department;
import cn.reebtech.repoms.model.entity.IDGenerator;
import cn.reebtech.repoms.model.entity.LastLogin;
import cn.reebtech.repoms.model.entity.Location;
import cn.reebtech.repoms.model.entity.Order_In;
import cn.reebtech.repoms.model.entity.Order_Invt;
import cn.reebtech.repoms.model.entity.Order_Invt_Detail;
import cn.reebtech.repoms.model.entity.Order_In_Detail;
import cn.reebtech.repoms.model.entity.Order_Out;
import cn.reebtech.repoms.model.entity.Order_Out_Detail;
import cn.reebtech.repoms.model.entity.Order_Req;
import cn.reebtech.repoms.model.entity.Order_Req_Detail;
import cn.reebtech.repoms.model.entity.Order_Ret;
import cn.reebtech.repoms.model.entity.Order_Ret_Detail;
import cn.reebtech.repoms.model.entity.Server;
import cn.reebtech.repoms.model.entity.Stacode;
import cn.reebtech.repoms.model.entity.User;
import cn.reebtech.repoms.model.entity.WaitBindAssets;
import cn.reebtech.repoms.model.entity.WareHouse;

import cn.reebtech.repoms.model.greendao.AssetClsctDao;
import cn.reebtech.repoms.model.greendao.AssetsDao;
import cn.reebtech.repoms.model.greendao.DepartmentDao;
import cn.reebtech.repoms.model.greendao.IDGeneratorDao;
import cn.reebtech.repoms.model.greendao.LastLoginDao;
import cn.reebtech.repoms.model.greendao.LocationDao;
import cn.reebtech.repoms.model.greendao.Order_InDao;
import cn.reebtech.repoms.model.greendao.Order_InvtDao;
import cn.reebtech.repoms.model.greendao.Order_Invt_DetailDao;
import cn.reebtech.repoms.model.greendao.Order_In_DetailDao;
import cn.reebtech.repoms.model.greendao.Order_OutDao;
import cn.reebtech.repoms.model.greendao.Order_Out_DetailDao;
import cn.reebtech.repoms.model.greendao.Order_ReqDao;
import cn.reebtech.repoms.model.greendao.Order_Req_DetailDao;
import cn.reebtech.repoms.model.greendao.Order_RetDao;
import cn.reebtech.repoms.model.greendao.Order_Ret_DetailDao;
import cn.reebtech.repoms.model.greendao.ServerDao;
import cn.reebtech.repoms.model.greendao.StacodeDao;
import cn.reebtech.repoms.model.greendao.UserDao;
import cn.reebtech.repoms.model.greendao.WaitBindAssetsDao;
import cn.reebtech.repoms.model.greendao.WareHouseDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig assetClsctDaoConfig;
    private final DaoConfig assetsDaoConfig;
    private final DaoConfig departmentDaoConfig;
    private final DaoConfig iDGeneratorDaoConfig;
    private final DaoConfig lastLoginDaoConfig;
    private final DaoConfig locationDaoConfig;
    private final DaoConfig order_InDaoConfig;
    private final DaoConfig order_InvtDaoConfig;
    private final DaoConfig order_Invt_DetailDaoConfig;
    private final DaoConfig order_In_DetailDaoConfig;
    private final DaoConfig order_OutDaoConfig;
    private final DaoConfig order_Out_DetailDaoConfig;
    private final DaoConfig order_ReqDaoConfig;
    private final DaoConfig order_Req_DetailDaoConfig;
    private final DaoConfig order_RetDaoConfig;
    private final DaoConfig order_Ret_DetailDaoConfig;
    private final DaoConfig serverDaoConfig;
    private final DaoConfig stacodeDaoConfig;
    private final DaoConfig userDaoConfig;
    private final DaoConfig waitBindAssetsDaoConfig;
    private final DaoConfig wareHouseDaoConfig;

    private final AssetClsctDao assetClsctDao;
    private final AssetsDao assetsDao;
    private final DepartmentDao departmentDao;
    private final IDGeneratorDao iDGeneratorDao;
    private final LastLoginDao lastLoginDao;
    private final LocationDao locationDao;
    private final Order_InDao order_InDao;
    private final Order_InvtDao order_InvtDao;
    private final Order_Invt_DetailDao order_Invt_DetailDao;
    private final Order_In_DetailDao order_In_DetailDao;
    private final Order_OutDao order_OutDao;
    private final Order_Out_DetailDao order_Out_DetailDao;
    private final Order_ReqDao order_ReqDao;
    private final Order_Req_DetailDao order_Req_DetailDao;
    private final Order_RetDao order_RetDao;
    private final Order_Ret_DetailDao order_Ret_DetailDao;
    private final ServerDao serverDao;
    private final StacodeDao stacodeDao;
    private final UserDao userDao;
    private final WaitBindAssetsDao waitBindAssetsDao;
    private final WareHouseDao wareHouseDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        assetClsctDaoConfig = daoConfigMap.get(AssetClsctDao.class).clone();
        assetClsctDaoConfig.initIdentityScope(type);

        assetsDaoConfig = daoConfigMap.get(AssetsDao.class).clone();
        assetsDaoConfig.initIdentityScope(type);

        departmentDaoConfig = daoConfigMap.get(DepartmentDao.class).clone();
        departmentDaoConfig.initIdentityScope(type);

        iDGeneratorDaoConfig = daoConfigMap.get(IDGeneratorDao.class).clone();
        iDGeneratorDaoConfig.initIdentityScope(type);

        lastLoginDaoConfig = daoConfigMap.get(LastLoginDao.class).clone();
        lastLoginDaoConfig.initIdentityScope(type);

        locationDaoConfig = daoConfigMap.get(LocationDao.class).clone();
        locationDaoConfig.initIdentityScope(type);

        order_InDaoConfig = daoConfigMap.get(Order_InDao.class).clone();
        order_InDaoConfig.initIdentityScope(type);

        order_InvtDaoConfig = daoConfigMap.get(Order_InvtDao.class).clone();
        order_InvtDaoConfig.initIdentityScope(type);

        order_Invt_DetailDaoConfig = daoConfigMap.get(Order_Invt_DetailDao.class).clone();
        order_Invt_DetailDaoConfig.initIdentityScope(type);

        order_In_DetailDaoConfig = daoConfigMap.get(Order_In_DetailDao.class).clone();
        order_In_DetailDaoConfig.initIdentityScope(type);

        order_OutDaoConfig = daoConfigMap.get(Order_OutDao.class).clone();
        order_OutDaoConfig.initIdentityScope(type);

        order_Out_DetailDaoConfig = daoConfigMap.get(Order_Out_DetailDao.class).clone();
        order_Out_DetailDaoConfig.initIdentityScope(type);

        order_ReqDaoConfig = daoConfigMap.get(Order_ReqDao.class).clone();
        order_ReqDaoConfig.initIdentityScope(type);

        order_Req_DetailDaoConfig = daoConfigMap.get(Order_Req_DetailDao.class).clone();
        order_Req_DetailDaoConfig.initIdentityScope(type);

        order_RetDaoConfig = daoConfigMap.get(Order_RetDao.class).clone();
        order_RetDaoConfig.initIdentityScope(type);

        order_Ret_DetailDaoConfig = daoConfigMap.get(Order_Ret_DetailDao.class).clone();
        order_Ret_DetailDaoConfig.initIdentityScope(type);

        serverDaoConfig = daoConfigMap.get(ServerDao.class).clone();
        serverDaoConfig.initIdentityScope(type);

        stacodeDaoConfig = daoConfigMap.get(StacodeDao.class).clone();
        stacodeDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        waitBindAssetsDaoConfig = daoConfigMap.get(WaitBindAssetsDao.class).clone();
        waitBindAssetsDaoConfig.initIdentityScope(type);

        wareHouseDaoConfig = daoConfigMap.get(WareHouseDao.class).clone();
        wareHouseDaoConfig.initIdentityScope(type);

        assetClsctDao = new AssetClsctDao(assetClsctDaoConfig, this);
        assetsDao = new AssetsDao(assetsDaoConfig, this);
        departmentDao = new DepartmentDao(departmentDaoConfig, this);
        iDGeneratorDao = new IDGeneratorDao(iDGeneratorDaoConfig, this);
        lastLoginDao = new LastLoginDao(lastLoginDaoConfig, this);
        locationDao = new LocationDao(locationDaoConfig, this);
        order_InDao = new Order_InDao(order_InDaoConfig, this);
        order_InvtDao = new Order_InvtDao(order_InvtDaoConfig, this);
        order_Invt_DetailDao = new Order_Invt_DetailDao(order_Invt_DetailDaoConfig, this);
        order_In_DetailDao = new Order_In_DetailDao(order_In_DetailDaoConfig, this);
        order_OutDao = new Order_OutDao(order_OutDaoConfig, this);
        order_Out_DetailDao = new Order_Out_DetailDao(order_Out_DetailDaoConfig, this);
        order_ReqDao = new Order_ReqDao(order_ReqDaoConfig, this);
        order_Req_DetailDao = new Order_Req_DetailDao(order_Req_DetailDaoConfig, this);
        order_RetDao = new Order_RetDao(order_RetDaoConfig, this);
        order_Ret_DetailDao = new Order_Ret_DetailDao(order_Ret_DetailDaoConfig, this);
        serverDao = new ServerDao(serverDaoConfig, this);
        stacodeDao = new StacodeDao(stacodeDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);
        waitBindAssetsDao = new WaitBindAssetsDao(waitBindAssetsDaoConfig, this);
        wareHouseDao = new WareHouseDao(wareHouseDaoConfig, this);

        registerDao(AssetClsct.class, assetClsctDao);
        registerDao(Assets.class, assetsDao);
        registerDao(Department.class, departmentDao);
        registerDao(IDGenerator.class, iDGeneratorDao);
        registerDao(LastLogin.class, lastLoginDao);
        registerDao(Location.class, locationDao);
        registerDao(Order_In.class, order_InDao);
        registerDao(Order_Invt.class, order_InvtDao);
        registerDao(Order_Invt_Detail.class, order_Invt_DetailDao);
        registerDao(Order_In_Detail.class, order_In_DetailDao);
        registerDao(Order_Out.class, order_OutDao);
        registerDao(Order_Out_Detail.class, order_Out_DetailDao);
        registerDao(Order_Req.class, order_ReqDao);
        registerDao(Order_Req_Detail.class, order_Req_DetailDao);
        registerDao(Order_Ret.class, order_RetDao);
        registerDao(Order_Ret_Detail.class, order_Ret_DetailDao);
        registerDao(Server.class, serverDao);
        registerDao(Stacode.class, stacodeDao);
        registerDao(User.class, userDao);
        registerDao(WaitBindAssets.class, waitBindAssetsDao);
        registerDao(WareHouse.class, wareHouseDao);
    }
    
    public void clear() {
        assetClsctDaoConfig.clearIdentityScope();
        assetsDaoConfig.clearIdentityScope();
        departmentDaoConfig.clearIdentityScope();
        iDGeneratorDaoConfig.clearIdentityScope();
        lastLoginDaoConfig.clearIdentityScope();
        locationDaoConfig.clearIdentityScope();
        order_InDaoConfig.clearIdentityScope();
        order_InvtDaoConfig.clearIdentityScope();
        order_Invt_DetailDaoConfig.clearIdentityScope();
        order_In_DetailDaoConfig.clearIdentityScope();
        order_OutDaoConfig.clearIdentityScope();
        order_Out_DetailDaoConfig.clearIdentityScope();
        order_ReqDaoConfig.clearIdentityScope();
        order_Req_DetailDaoConfig.clearIdentityScope();
        order_RetDaoConfig.clearIdentityScope();
        order_Ret_DetailDaoConfig.clearIdentityScope();
        serverDaoConfig.clearIdentityScope();
        stacodeDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
        waitBindAssetsDaoConfig.clearIdentityScope();
        wareHouseDaoConfig.clearIdentityScope();
    }

    public AssetClsctDao getAssetClsctDao() {
        return assetClsctDao;
    }

    public AssetsDao getAssetsDao() {
        return assetsDao;
    }

    public DepartmentDao getDepartmentDao() {
        return departmentDao;
    }

    public IDGeneratorDao getIDGeneratorDao() {
        return iDGeneratorDao;
    }

    public LastLoginDao getLastLoginDao() {
        return lastLoginDao;
    }

    public LocationDao getLocationDao() {
        return locationDao;
    }

    public Order_InDao getOrder_InDao() {
        return order_InDao;
    }

    public Order_InvtDao getOrder_InvtDao() {
        return order_InvtDao;
    }

    public Order_Invt_DetailDao getOrder_Invt_DetailDao() {
        return order_Invt_DetailDao;
    }

    public Order_In_DetailDao getOrder_In_DetailDao() {
        return order_In_DetailDao;
    }

    public Order_OutDao getOrder_OutDao() {
        return order_OutDao;
    }

    public Order_Out_DetailDao getOrder_Out_DetailDao() {
        return order_Out_DetailDao;
    }

    public Order_ReqDao getOrder_ReqDao() {
        return order_ReqDao;
    }

    public Order_Req_DetailDao getOrder_Req_DetailDao() {
        return order_Req_DetailDao;
    }

    public Order_RetDao getOrder_RetDao() {
        return order_RetDao;
    }

    public Order_Ret_DetailDao getOrder_Ret_DetailDao() {
        return order_Ret_DetailDao;
    }

    public ServerDao getServerDao() {
        return serverDao;
    }

    public StacodeDao getStacodeDao() {
        return stacodeDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public WaitBindAssetsDao getWaitBindAssetsDao() {
        return waitBindAssetsDao;
    }

    public WareHouseDao getWareHouseDao() {
        return wareHouseDao;
    }

}
