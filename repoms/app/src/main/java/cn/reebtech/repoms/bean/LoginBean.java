package cn.reebtech.repoms.bean;

public class LoginBean {
    private String usrname;
    private String passwd;
    private boolean rememberme;
    private boolean autologin;
    public LoginBean(){

    }
    public LoginBean(String usrname, String passwd, boolean rememberme, boolean autologin){
        this.usrname = usrname;
        this.passwd = passwd;
        this.rememberme = rememberme;
        this.autologin = autologin;
    }
    public String getUsrname(){
        return usrname;
    }
    public void setUsrname(String value){
        usrname = value;
    }
    public String getPasswd(){
        return passwd;
    }
    public void setPasswd(String value){
        passwd = value;
    }
    public boolean isRememberme(){
        return rememberme;
    }
    public void setRememberme(boolean value){
        rememberme = value;
    }
    public boolean isAutologin(){
        return autologin;
    }
    public void setAutologin(boolean value){
        autologin = value;
    }
}
