package com.rymcu.forest.jwt.model;

/**
 * Token的Model类，可以增加字段提高安全性，例如时间戳、url签名
 * @author ScienJus
 * @date 2015/7/31.
 */
public class TokenModel {

    private String username;

    private String token;

    public TokenModel(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
