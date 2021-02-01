package com.rymcu.forest.enumerate;

/**
 * @author ronger
 */

public enum TransactionEnum {
    ArticleSponsor("0", 20),
    Answer("1", 30),
    CorrectAnswer("2", 50);

    private String dataType;

    private Integer money;

    TransactionEnum(String dataType, Integer money) {
        this.dataType = dataType;
        this.money = money;
    }

    public String getDataType() {
        return this.dataType;
    }

    public Integer getMoney() {
        return this.money;
    }

    public boolean isArticleSponsor() {
        return ArticleSponsor.equals(this);
    }
}
