package com.rymcu.forest.core.result;

import com.rymcu.forest.util.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalResultGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalResultGenerator.class);

    /**
     * normal
     *
     * @param success
     * @param data
     * @param message
     * @param <T>
     * @return
     */
    public static <T> GlobalResult<T> genResult(boolean success, T data, String message) {
        GlobalResult<T> result = GlobalResult.newInstance();
        result.setSuccess(success);
        result.setData(data);
        result.setMessage(message);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("generate rest result:{}", result);
        }
        return result;
    }

    /**
     * success
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> GlobalResult<T> genSuccessResult(T data) {

        return genResult(true, data, null);
    }

    /**
     * error message
     *
     * @param message error message
     * @param <T>
     * @return
     */
    public static <T> GlobalResult<T> genErrorResult(String message) {

        return genResult(false, null, message);
    }

    /**
     * error
     *
     * @param error error enum
     * @param <T>
     * @return
     */
    public static <T> GlobalResult<T> genErrorResult(ErrorCode error) {

        return genErrorResult(error.getMessage());
    }

    /**
     * success no message
     *
     * @return
     */
    public static GlobalResult genSuccessResult() {
        return genSuccessResult(null);
    }

    /**
     * success
     *
     * @param <T>
     * @return
     */
    public static <T> GlobalResult<T> genSuccessResult(String message) {

        return genResult(true, null, message);
    }

}
