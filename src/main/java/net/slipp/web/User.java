package net.slipp.web;

public class User {
	private String userId;
	private String password;
	private String name;
	private String email1;
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email1=" + email1 + "]";
	}

}
