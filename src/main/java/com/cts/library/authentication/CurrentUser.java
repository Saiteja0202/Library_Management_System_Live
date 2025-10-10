package com.cts.library.authentication;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.cts.library.model.Member;

@Component
@RequestScope
public class CurrentUser {
	
	private Member currentUser;

	public Member getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Member currentUser) {
		this.currentUser = currentUser;
	}
	
}
