package com.rymcu.forest.enumerate;

import java.util.Arrays;

/**
 * @author ronger
 */

public enum TransactionEnum {
    ArticleSponsor("0", 20, "文章赞赏"),
    Answer("1", 30, "答题奖励"),
    CorrectAnswer("2", 50, "答题奖励");

    private String dataType;

    private Integer money;

    private String description;

    TransactionEnum(String dataType, Integer money, String description) {
        this.dataType = dataType;
        this.money = money;
        this.description = description;
    }

    public static TransactionEnum findTransactionEnum(String dataType) {
        return Arrays.stream(TransactionEnum.values()).filter(transactionEnum -> transactionEnum.getDataType().equals(dataType)).findFirst().orElse(TransactionEnum.ArticleSponsor);
    }

    public String getDataType() {
        return this.dataType;
    }

    public Integer getMoney() {
        return this.money;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isArticleSponsor() {
        return ArticleSponsor.equals(this);
    }
}
