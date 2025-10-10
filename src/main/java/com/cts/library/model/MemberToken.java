package com.cts.library.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class MemberToken {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
 
    @Column(unique = true)
    private String memberToken;
 
    private LocalDateTime generatedAt;
 
    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;
    
    public MemberToken() {
    	
    }
 
	public Long getTokenId() {
		return tokenId;
	}
 
	public void setTokenId(Long tokenId) {
		this.tokenId = tokenId;
	}
 
	public String getMemberToken() {
		return memberToken;
	}
 
	public void setMemberToken(String memberToken) {
		this.memberToken = memberToken;
	}
 
	public LocalDateTime getGeneratedAt() {
		return generatedAt;
	}
 
	public void setGeneratedAt(LocalDateTime generatedAt) {
		this.generatedAt = generatedAt;
	}
 
	public Member getMember() {
		return member;
	}
 
	public void setMember(Member member) {
		this.member = member;
	}

}
