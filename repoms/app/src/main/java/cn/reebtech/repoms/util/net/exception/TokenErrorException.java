package cn.reebtech.repoms.util.net.exception;

/**
 * Created by on 2017/6/13.
 * 作用：
 */

public class TokenErrorException extends BaseException {
    public TokenErrorException(String code, String displayMessage) {
        super(code, displayMessage);
    }
}
