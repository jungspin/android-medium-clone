package com.cos.mediumclone.controller.dto;

import lombok.Data;

@Data
public class CMRespDTO<T> {
    private int code;
    private String msg;
    private T data;
}
