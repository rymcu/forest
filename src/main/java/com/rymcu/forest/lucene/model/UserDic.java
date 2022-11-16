package com.rymcu.forest.lucene.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UserDic 用户个性化字典
 *
 * @author suwen
 * @date 2021/2/4 09:09
 */
@Data
@Table(name = "forest_lucene_user_dic")
public class UserDic {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 字典
     */
    private String dic;
}
