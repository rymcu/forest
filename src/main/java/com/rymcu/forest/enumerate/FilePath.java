package com.rymcu.forest.enumerate;

import java.util.Arrays;

/**
 * Created on 2024/3/6 10:51.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @desc : com.rymcu.forest.enumerate
 */
public enum FilePath {
    AVATAR,
    ARTICLE,
    TAG,
    TOPIC,
    PORTFOLIO,
    PRODUCT,
    IMAGES;

    public static FilePath getFilePath(int type) {
        for (FilePath filePath : FilePath.values()) {
            if (filePath.ordinal() == type) {
                return filePath;
            }
        }
        return FilePath.IMAGES;
    }

    public static String getPath(int type) {
        for (FilePath filePath : FilePath.values()) {
            if (filePath.ordinal() == type) {
                return filePath.name().toLowerCase();
            }
        }
        return FilePath.IMAGES.name().toLowerCase();
    }
}
