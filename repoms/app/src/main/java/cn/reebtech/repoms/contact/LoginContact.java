package cn.reebtech.repoms.contact;

import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.LoginBean;
import cn.reebtech.repoms.presenter.IBasePresenter;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.view.IBaseView;

public final class LoginContact {
    /**
     * view 层接口
     */
    public interface LoginUI extends IBaseView {
        /**
         * 登录成功
         */
        void loginSuccess(LoginBean login);

        /**
         * 登录失败
         */
        void loginFailure(BaseResultBean result);
        /*
        * 加载上次登录信息
        * */
        void loadLastLogin(LoginBean data);
    }
    /**
     * presenter 层接口
     */
    public interface LoginPtr extends IBasePresenter {
        void login(LoginBean login);
        void readLastLogin();
        void checkUserDb();
    }
    /**
     * model 层接口
     */
    public interface LoginMdl {
        void login(LoginBean data, IConDbListener callbak);
        void readLastLogin(IConDbListener callback);
        void saveLastLogin(LoginBean data, IConDbListener callback);
        void checkUserDb(IConDbListener callback);
    }
}
