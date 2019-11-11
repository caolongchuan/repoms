package cn.reebtech.repoms.view;

public interface IBaseView extends IBaseXView {
    /**
     * 显示正在加载 view
     */
    void showLoading();
    /**
     * 关闭正在加载 view
     */
    void hideLoading();
    /**
     * 显示提示
     * @param msg
     */
    void showToast(String msg);
}
