package com.rymcu.vertical.core.result;

public enum GlobalResultMessage {

    SUCCESS("操作成功！"),
    FAIL("操作失败！"),
    SEND_FAIL("发送失败，请稍后再试！"),
    SEND_SUCCESS("发送成功！");

    private String message;

    GlobalResultMessage(String message){
        this.message = message;
    }
}
