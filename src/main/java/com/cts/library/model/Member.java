package com.cts.library.model;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long memberId;

	@NotBlank(message = "Name cannot be Blank")
	private String name;

	@NotBlank(message = "Email cannot be Blank")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Phone Number cannot be Blank")
	@Size(min = 10, max = 10, message = "Phone Number has to be 10 digits only")
	private String phone;

	@NotBlank(message = "Address cannot be Blank")
	private String address;

	@NotBlank(message = "UserName cannot be Blank")
	@Size(min = 4, max = 20, message = "UserName must be between 4 to 20 characters")
	private String username;

	@NotBlank(message = "Password cannot be Blank")
	@Size(min = 8, message = "Password must be between 8 to 50 characters")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	private LocalDate membershipExpiryDate;
	private int borrowingLimit = 2;

	@Enumerated(EnumType.STRING)
	private MembershipStatus membershipStatus = MembershipStatus.BASIC;

	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<BorrowingTransaction> transactions;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Fine> fines;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Notification> notifications;

	List<BorrowingTransaction> getTransactions(Long memberId)
	{
		return transactions;
	}
	
	List<BorrowingTransaction> getAllTransactions()
	{
		return transactions;
	}
	

	public void setTransactions(List<BorrowingTransaction> transactions) {
		this.transactions = transactions;
	}

	public List<Fine> getFines() {
		return fines;
	}

	public void setFines(List<Fine> fines) {
		this.fines = fines;
	}

	public boolean isMembershipActive() {
		return membershipExpiryDate != null && LocalDate.now().isBefore(membershipExpiryDate);
	}

	public Member() {
		// by default we are setting the role to member
		this.role = Role.MEMBER;
	}

	public Member(long memberId, String name, String email, String phone, String address,
			MembershipStatus membershipStatus, String username, String password, Role role) {
		this(memberId, name, email, phone, address, membershipStatus, 2, username, password, role);
	}

	public Member(long memberId, String name, String email, String phone, String address,
			MembershipStatus membershipStatus, int borrowingLimit, String username, String password, Role role) {
		super();
		this.memberId = memberId;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.membershipStatus = membershipStatus;
		this.username = username;
		this.password = password;
		this.role = role;
		this.borrowingLimit = borrowingLimit;
	}

	public long getMemberId() {
		return memberId;
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	
	public MembershipStatus getMembershipStatus() {
		
		return membershipStatus;
	}

	public void setMembershipStatus(MembershipStatus status) {
		if (this.membershipStatus != MembershipStatus.PRIME || status == MembershipStatus.EXPIRED) {
			this.membershipStatus = status;
		}
	}

	public LocalDate getMembershipExpiryDate() {
		return membershipExpiryDate;
	}

	public void setMembershipExpiryDate(LocalDate membershipExpiryDate) {
		this.membershipExpiryDate = membershipExpiryDate;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {

		// we are making sure member is not changing into admin
		if (this.role != Role.ADMIN) {
			this.role = role;
		}
	}

	public int getBorrowingLimit() {
		return borrowingLimit;
	}

	public void setBorrowingLimit(int borrowingLimit) {
		this.borrowingLimit = borrowingLimit;
	}

}
