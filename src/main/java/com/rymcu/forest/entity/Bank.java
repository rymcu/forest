package com.rymcu.forest.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 银行
 * @author ronger
 */
@Table(name = "vertical_bank")
@Data
public class Bank {

    /** 主键 */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer idBank;
    /** 银行名称 */
    private String bankName;
    /** 银行负责人 */
    private Integer bankOwner;
    /** 银行描述 */
    private String bankDescription;
    /** 创建人 */
    private Integer createdBy;
    /** 创建时间 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

}
