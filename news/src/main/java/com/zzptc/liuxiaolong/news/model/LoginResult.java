package com.zzptc.liuxiaolong.news.model;

public class LoginResult {
	private String status;
	private String token;
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
