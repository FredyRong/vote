package com.fredy.vote.api.response;

import com.fredy.vote.api.Exception.CustomizeException;
import com.fredy.vote.api.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 接口返回信息
 * @author Fredy
 * @date 2020-11-16 15:48
 */
@Data
@AllArgsConstructor
public class BaseResponse<T> {

    private Integer code;
    private String msg;
    private T data;

    public BaseResponse(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    public BaseResponse(StatusCode statusCode, T data) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.data = data;
    }

    public BaseResponse(CustomizeException customizeException) {
        this.code = customizeException.getCode();
        this.msg = customizeException.getMsg();
    }

}
