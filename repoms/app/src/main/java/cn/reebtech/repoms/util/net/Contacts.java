package cn.reebtech.repoms.util.net;

/**
 * Created by on 2017/2/15.
 * 作用： 网络地址工具类
 */

public class Contacts {

    // 公共的url
    public static final String BASE_URL = "192.168.1.168:8080";
    public static final String LOGIN_URL = "/manageplatform/login";
    public static final String USER_URL = "/manageplatform/handset/user/getAll";
    public static final String DEPT_URL = "/manageplatform/handset/department/getAll";
    public static final String WAREHOUSE_URL = "/manageplatform/handset/warehouse/getAll";
    public static final String ACLST_URL = "/manageplatform/handset/category/getAll";
    public static final String ASSEET_URL = "/manageplatform/handset/material/getAll";
    public static final String ORDER_OUT_URL = "/manageplatform/handset/ckAdd";
    public static final String ORDER_IN_URL = "/manageplatform/handset/rkAdd";
    public static final String ORDER_INVT_URL = "/manageplatform/handset/pdAdd";

    //clc添加 只获取未绑定标签的物资数据
    public static final String ON_RFID_ASSEET_URL = "/manageplatform/handset/material/getAllOrRFID";
    public static final String UPDATA_NORFID_ASSET_URL = "/manageplatform/handset/material/bdbq";
    public static final String UPDATA_ALLOCATE_ASSET_URL = "/manageplatform/handset/material/allocation";

    public static final String GLOBAL_TOKEN = "token";

}
