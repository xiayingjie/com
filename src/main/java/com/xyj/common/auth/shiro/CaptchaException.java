package com.xyj.common.auth.shiro;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:13-10-18 下午3:23
 */
public class CaptchaException  extends AuthenticationException {

    /** 描述  */
    private static final long serialVersionUID = 6146451562810994591L;

    public CaptchaException() {
        super();
    }

    public CaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptchaException(String message) {
        super(message);
    }

    public CaptchaException(Throwable cause) {
        super(cause);
    }
}
