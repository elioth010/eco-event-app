/**
 * 
 */
package com.ecoeventos.eis.dto;

import java.io.Serializable;

/**
 * @author developer
 *
 */
public class UserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2199893350596014187L;
	
	private Integer id;
	private String username;
	private String password;
	private String name;
	private String email;
	private Integer age;

	/**
	 * 
	 */
	public UserDTO() {
		super();
	}

	public UserDTO(Integer id, String username, String password, String name, String email, Integer age) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.age = age;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}


	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", username=" + username + ", password="
				+ password + ", name=" + name + ", email=" + email + ", age="
				+ age + "]";
	}
	
}
