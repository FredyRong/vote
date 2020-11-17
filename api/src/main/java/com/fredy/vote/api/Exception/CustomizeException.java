package com.fredy.vote.api.Exception;

import com.fredy.vote.api.enums.StatusCode;
import lombok.Getter;

/**
 * @author Fredy
 * @date 2020-11-17 10:35
 */
public class CustomizeException extends RuntimeException{

    @Getter
    private Integer code;
    @Getter
    private String msg;

    public CustomizeException(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }
}
