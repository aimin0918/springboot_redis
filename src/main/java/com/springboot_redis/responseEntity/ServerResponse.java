package com.springboot_redis.responseEntity;

import lombok.Data;

/**
 * @author Aimin
 * @Description:
 * @ClassName ServerResponse
 * @date 2023/4/14
 */
@Data
public class ServerResponse {
    private Boolean success;
    private Integer code;
    private String message;
    private Object data;

    private ServerResponse() {
    }

    public static ServerResponse ok(Object params) {
        ServerResponse serverResponse = new ServerResponse();
        //成功展示默认提示信息
        serverResponse.setSuccess(ResultEnum.OK.getSuccess());
        serverResponse.setCode(ResultEnum.OK.getCode());
        serverResponse.setMessage(ResultEnum.OK.getMessage());
        //返回传入的参数
        serverResponse.setData(params);
        return serverResponse;
    }

    public static ServerResponse badRequest() {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setSuccess(ResultEnum.BAD_REQUEST.getSuccess());
        serverResponse.setCode(ResultEnum.BAD_REQUEST.getCode());
        //效验失败传入指定的提示信息
        serverResponse.setMessage(ResultEnum.BAD_REQUEST.getMessage());
        //效验失败不返回参数
        serverResponse.setData(null);
        return serverResponse;
    }

    public static ServerResponse notFound() {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setSuccess(ResultEnum.NOT_FOUND.getSuccess());
        serverResponse.setCode(ResultEnum.NOT_FOUND.getCode());
        //晓燕失败传入指定的提示信息
        serverResponse.setMessage(ResultEnum.NOT_FOUND.getMessage());
        //效验失败不反悔参数
        serverResponse.setData(null);
        return serverResponse;
    }

}
