package cn.reebtech.repoms.bean;

public class BaseResultBean {
    private int errCode;
    private String errMsg;

    public BaseResultBean(){
        
    }
    public BaseResultBean(int errCode, String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
