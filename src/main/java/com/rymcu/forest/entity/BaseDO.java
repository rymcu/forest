package com.rymcu.forest.entity;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 基础DO类，提供toString快方法
 *
 * @author liwei
 * @date 2015/6/16
 */
public class BaseDO implements Serializable {

    private static final long serialVersionUID = -1394589131426860408L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}

