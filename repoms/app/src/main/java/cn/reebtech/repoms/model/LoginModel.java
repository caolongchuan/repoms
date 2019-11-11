package cn.reebtech.repoms.model;

import android.app.Application;
import android.util.Log;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import cn.reebtech.repoms.R;
import cn.reebtech.repoms.RepomsAPP;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.LoginBean;
import cn.reebtech.repoms.contact.LoginContact;
import cn.reebtech.repoms.model.entity.LastLogin;
import cn.reebtech.repoms.model.entity.User;
import cn.reebtech.repoms.model.greendao.LastLoginDao;
import cn.reebtech.repoms.model.greendao.UserDao;
import cn.reebtech.repoms.presenter.LoginPresenter;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.model.greendao.UserDao.Properties;

public class LoginModel extends BaseDBModel<User, String, IConDbListener<Integer, LoginBean, BaseResultBean>> implements LoginContact.LoginMdl {

    @Override
    public void login(LoginBean data, IConDbListener callback) {
        UserDao table = getUserDao();
        List<User> users = table.queryBuilder().list();
        Query query = table.queryBuilder().where(UserDao.Properties.Username.eq(data.getUsrname()), Properties.Password.eq(data.getPasswd())).build();
        List<User> list = query.list();
        if(query.list().size() > 0){
            callback.onSuccess(LoginPresenter.OP_LOGIN, data, new BaseResultBean(0, ""));
        }
        else{
            callback.onFailure(LoginPresenter.OP_LOGIN, new BaseResultBean(1, RepomsAPP.getContext().getString(R.string.str_tip_login_failed_up_invalid)));
        }
    }

    @Override
    public void readLastLogin(IConDbListener callback) {
        LastLoginDao table = getLastLoginDao();
        LoginBean loginBean = new LoginBean("", "", false, false);
        if(table.count() > 0){
            LastLogin record = table.queryBuilder().orderDesc(LastLoginDao.Properties.Id).unique();
            loginBean.setUsrname(record.getName());
            loginBean.setPasswd(record.getPassword());
            loginBean.setRememberme(record.getRememberme());
            loginBean.setAutologin(record.getAutologin());
        }
        callback.onSuccess(LoginPresenter.OP_READ_LASTLOGIN, loginBean, new BaseResultBean(0, ""));
    }

    @Override
    public void saveLastLogin(LoginBean data, IConDbListener callback) {
        LastLoginDao table = getLastLoginDao();
        table.deleteAll();
        LastLogin record = new LastLogin();
        if(data.isRememberme()){
            record.setName(data.getUsrname());
            record.setRememberme(true);
        }
        if(data.isAutologin()){
            record.setPassword(data.getPasswd());
            record.setRememberme(true);
            record.setAutologin(true);
        }
        table.insert(record);
        callback.onSuccess(LoginPresenter.OP_SAVE_LASTLOGIN, data, new BaseResultBean(0, ""));
    }

    @Override
    public void checkUserDb(IConDbListener callback) {
        UserDao table = getUserDao();
        if(table.count() == 0){
            User record = new User();
            record.setId("test");
            record.setName("测试人员");
            record.setPassword("1");
            record.setUsername("test");
            table.insert(record);
            record = new User();
            record.setId("admin");
            record.setName("仓库管理员");
            record.setPassword("1");
            record.setUsername("admin");
            table.insert(record);
            record = new User();
            record.setId("usersend");
            record.setName("送货人");
            record.setPassword("1");
            record.setUsername("usersend");
            table.insert(record);
            record = new User();
            record.setId("userrec");
            record.setName("收货人");
            record.setPassword("1");
            record.setUsername("userrec");
            table.insert(record);
        }
        callback.onSuccess(LoginPresenter.OP_CHECK_USERDB, null, new BaseResultBean(0, ""));
    }

    /*
    * 获取上次登录信息
    * */
    private LastLoginDao getLastLoginDao(){
        return GreenDaoManager.getInstance().getSession().getLastLoginDao();
    }
    /*
    * 获取用户信息
    * */
    private UserDao getUserDao(){
        return GreenDaoManager.getInstance().getSession().getUserDao();
    }
}
