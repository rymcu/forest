/*
 * Copyright (c) 2020 RYMCU (https://rymcu.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package com.rymcu.forest.auth;

/**
 * @author ronger
 */
public class JwtConstants {


    /**
     * 上线需要变更
     */
    public static final String JWT_SECRET = "JYJ5Qv2WF4lA6jPl5GKuAG";

    public static final String AUTHORIZATION = "Authorization";
    public static final String UPLOAD_TOKEN = "X-Upload-Token";
    public static final String CURRENT_USER_NAME = "CURRENT_TOKEN_USER_NAME";
    public static final String CURRENT_TOKEN_CLAIMS = "CURRENT_TOKEN_CLAIMS";
    public static final String LAST_ONLINE = "last_online:";

    public static final long TOKEN_EXPIRES_HOUR = 2;
    public static final long LAST_ONLINE_EXPIRES_MINUTE = 10;
    public static final long TOKEN_EXPIRES_MINUTE = 15;
    public static final long REFRESH_TOKEN_EXPIRES_HOUR = 2;

}
