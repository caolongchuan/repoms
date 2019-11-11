package cn.reebtech.repoms.contact;

import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.LoginBean;
import cn.reebtech.repoms.presenter.IBasePresenter;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.view.IBaseView;

public final class MainContact {
    /**
     * view 层接口
     */
    public interface MainUI extends IBaseView {
        void logoutSuccess();
        void logoutFailuer(BaseResultBean result);
    }
    /**
     * presenter 层接口
     */
    public interface MainPtr extends IBasePresenter {
        void logout();
        void tmpInitData(int type);
    }
    /**
     * model 层接口
     */
    public interface MainMdl {
        void logout(IConDbListener callback);
        void tmpInitData(int type);
    }
}
