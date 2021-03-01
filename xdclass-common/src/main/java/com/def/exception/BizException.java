package com.def.exception;

import com.def.enums.BizCodeEnum;
import lombok.Data;

@Data
public class BizException extends RuntimeException{

    private int code;

    private String msg;

    public BizException(BizCodeEnum biz) {
        super(biz.getMessage());
        this.code = biz.getCode();
        this.msg = biz.getMessage();
    }

}
