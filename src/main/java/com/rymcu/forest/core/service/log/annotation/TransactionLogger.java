package com.rymcu.forest.core.service.log.annotation;

import com.rymcu.forest.enumerate.TransactionEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author ronger
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionLogger {

    TransactionEnum transactionType();

}
