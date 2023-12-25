package com.smart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Contact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="cId", unique = true)
	private int cId;
	
	@NotBlank(message="Name cannot be blank")
	private String cName;
	private String cNickName;
	private String cWork;
	@NotBlank(message = "Email can not be left blank")
	@Column(nullable = false)
	@Email
	private String cEmail;
	@Column(nullable = false)
	@Size(min = 10, max = 13)
	@Pattern(regexp="(^$|[0-9]{10,13})")
	private String cPhone;
	@Column(nullable = false, columnDefinition = "varchar(50) default 'default.png'")
	private String cImageUrl = "default.png";
	@Column(length = 5000)
	private String cDescription;
	
	@ManyToOne
	private User user;
	
	public Contact() {
		super();
	}

	public int getcId() {
		return cId;
	}


	public void setcId(int cId) {
		this.cId = cId;
	}


	public String getcName() {
		return cName;
	}


	public void setcName(String cName) {
		this.cName = cName;
	}


	public String getcNickName() {
		return cNickName;
	}


	public void setcNickName(String cNickName) {
		this.cNickName = cNickName;
	}


	public String getcWork() {
		return cWork;
	}


	public void setcWork(String cWork) {
		this.cWork = cWork;
	}


	public String getcEmail() {
		return cEmail;
	}


	public void setcEmail(String cEmail) {
		this.cEmail = cEmail;
	}


	public String getcPhone() {
		return cPhone;
	}


	public void setcPhone(String cPhone) {
		this.cPhone = cPhone;
	}


	public String getcImageUrl() {
		return cImageUrl;
	}


	public void setcImageUrl(String cImageUrl) {
		this.cImageUrl = cImageUrl;
	}


	public String getcDescription() {
		return cDescription;
	}


	public void setcDescription(String cDescription) {
		this.cDescription = cDescription;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Contact [cId=" + cId + ", cName=" + cName + ", cNickName=" + cNickName + ", cWork=" + cWork
				+ ", cEmail=" + cEmail + ", cPhone=" + cPhone + ", cImageUrl=" + cImageUrl + ", cDescription="
				+ cDescription + "]";
	}
	
}
