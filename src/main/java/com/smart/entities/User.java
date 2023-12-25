package com.smart.entities;

import java.util.ArrayList; 
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	@Size(min = 4, max = 20, message="Name must be between 4 - 15 characters")
	private String name;
	
	@Email
	@NotBlank(message = "Enter a valid email")
	@Column(unique = true, nullable = false)
	private String email;
	
	@Size(min=6, message="Password must be greater than 5 characters")
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false, columnDefinition = "varchar(20) default 'ROLE_USER'")
	private String role = "ROLE_USER";
	@Column(nullable = false, columnDefinition = "boolean default true")
	private boolean enabled = true;
	@Column(nullable = false, columnDefinition = "varchar(50) default 'default.png'")
	private String imageURL = "default.png";
	
	
	@Column(length = 500)
	private String about;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Contact> contacts = new ArrayList<Contact>();
	

	public User() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", enabled=" + enabled + ", imageURL=" + imageURL + ", about=" + about + ", contacts=" + contacts
				+ "]";
	}
	
}
/*
echo "# SmartContact-Project" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/Souravs72/SmartContact-Project.git
git push -u origin main

*/