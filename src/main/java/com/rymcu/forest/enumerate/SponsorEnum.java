package com.rymcu.forest.enumerate;

/**
 * @author ronger
 */

public enum SponsorEnum {
    Article("0", 20);

    private String dataType;

    private Integer money;

    SponsorEnum(String dataType, Integer money) {
        this.dataType = dataType;
        this.money = money;
    }

    public String getDataType() {
        return this.dataType;
    }

    public Integer getMoney() {
        return this.money;
    }

    public boolean isArticle() {
        return Article.equals(this);
    }
}
