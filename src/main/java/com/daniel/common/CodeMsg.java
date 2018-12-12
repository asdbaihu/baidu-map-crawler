package com.daniel.common;

/**
 * 调用服务状态码
 *
 * @author lingengxiang
 * @date 2018/12/11 14:17
 */
public enum CodeMsg {

    /**
     * 成功处理请求
     */
    OK(0, "正常"),

    /**
     * 请求参数非法
     */
    PARAMETER_INvVALID(2, "请求参数非法"),

    /**
     * 权限校验失败
     */
    VERTIFY_FAILURE(3, "权限校验失败"),

    /**
     * 配额校验失败
     */
    QUOTA_FAILURE(4, "配额校验失败"),

    /**
     * ak不存在或者非法
     */
    AK_FAILURE(5, "ak不存在或者非法");

    /**
     * 状态码
     */
    int code;

    /**
     * 信息
     */
    String msg;

    CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
