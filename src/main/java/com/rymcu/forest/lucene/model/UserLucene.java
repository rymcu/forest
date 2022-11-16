package com.rymcu.forest.lucene.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserLucene
 *
 * @author suwen
 * @date 2021/3/6 09:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLucene {

    /**
     * 用户编号
     */
    private Long idUser;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 签名
     */
    private String signature;

    /**
     * 相关度评分
     */
    private String score;
}
