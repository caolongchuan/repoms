package cn.reebtech.repoms.view;

import android.app.Activity;

/*
* View层的接口基类
* */
public interface IBaseXView {
    /**
     * 获取 Activity 对象
     *
     * @return activity
     */
    <T extends Activity> T getSelfActivity();
}
