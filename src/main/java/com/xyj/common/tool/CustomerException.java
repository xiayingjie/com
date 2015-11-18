package com.xyj.common.tool;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/11/17
 */

public class CustomerException extends Exception {
    private String retCd ;  //异常对应的返回码
    private String msgDes;  //异常对应的描述信息

    public CustomerException() {
        super();
    }

    public CustomerException(String message) {
        super(message);
        msgDes = message;
    }

    public CustomerException(String retCd, String msgDes) {
        super();
        this.retCd = retCd;
        this.msgDes = msgDes;
    }

    public String getRetCd() {
        return retCd;
    }

    public String getMsgDes() {
        return msgDes;
    }
}
