package com.rymcu.forest.enumerate;

import java.util.Arrays;

/**
 * @author ronger
 */
public enum TransactionEnum {
    ArticleSponsor(20, "文章赞赏"),
    Answer(30, "答题奖励"),
    CorrectAnswer(50, "答题奖励"),
    NewbieRewards(200, "新手奖励");

    private Integer money;

    private String description;

    TransactionEnum(Integer money, String description) {
        this.money = money;
        this.description = description;
    }

    public static TransactionEnum findTransactionEnum(int dataType) {
        return Arrays.stream(TransactionEnum.values()).filter(transactionEnum -> transactionEnum.ordinal() == dataType).findFirst().orElse(TransactionEnum.ArticleSponsor);
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
