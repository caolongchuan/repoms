package cn.reebtech.repoms.util;

import cn.reebtech.repoms.bean.BaseResultBean;

public interface IConDbListener<S, T, R extends BaseResultBean> {
    /**
     * 数据库操作成功
     * @param source 请求的标记
     * @param data 请求的数据
     * @param result 返回的数据
     */
    void onSuccess(S source, T data, R result);

    /**
     * 数据库操作失败
     * @param source 请求失败时，返回的信息类
     * @param result 请求失败时，返回的信息类
     */
    void onFailure(S source, R result);
}
