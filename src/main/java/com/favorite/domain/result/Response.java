package com.favorite.domain.result;

/**
 * Created by cdc on 2018/6/20.
 */
public class Response {

    /**返回信息码 */
    private String resCode = "000000";

    /**
     * 返回消息内容
     */
    private String resMsg = "操作成功";

    public Response(){};

    public Response(ExceptionMsg msg) {
        this.resCode = msg.getCode();
        this.resMsg = msg.getMsg();
    }

    public Response(String rspCode) {
        this.resCode = rspCode;
        this.resMsg = "";
    }

    public Response(String rspCode, String rspMsg) {
        this.resCode = rspCode;
        this.resMsg = rspMsg;
    }
    public String getRspCode() {
        return resCode;
    }
    public void setRspCode(String rspCode) {
        this.resCode = rspCode;
    }
    public String getRspMsg() {
        return resMsg;
    }
    public void setRspMsg(String rspMsg) {
        this.resMsg = rspMsg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "rspCode='" + resCode + '\'' +
                ", rspMsg='" + resMsg + '\'' +
                '}';
    }

}
