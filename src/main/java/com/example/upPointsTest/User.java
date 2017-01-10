package com.example.upPointsTest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
	@Id
	@GeneratedValue
	private Long id;
	
	private String username;
	private String password;
	
	protected User(){}
	
	protected User(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
//	@Override
//	public String toString() {
//		return String.format("User[%s,%s]", username, password);
//	}
}
