package com.rymcu.forest.core.result;

import lombok.Getter;

@Getter
public enum GlobalResultMessage {

    SUCCESS("操作成功！"),
    FAIL("操作失败！"),
    SEND_FAIL("发送失败，请稍后再试！"),
    SEND_SUCCESS("验证码已发送至邮箱！");

    private String message;

    GlobalResultMessage(String message) {
        this.message = message;
    }
}
