package com.example.upPointsTest;

public class Authentication {
	private String username;
	private String password;
	
	public Authentication() {
		setUsername("user");
		setPassword("qwerty");
	}

	private void setUsername(String username) {
		this.username = username;
	}
	
	private String getUsername(){
		return username;
	}

	private void setPassword(String password) {
		this.password = password;
	}
	
	private String getPassword(){
		return password;
	}
	
	public Boolean authenticate(String username, String password){
		if(username.equals(getUsername()) && password.equals(getPassword())){
			return true;
		}
		return false;
	}

}
