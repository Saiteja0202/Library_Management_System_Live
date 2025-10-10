package com.cts.library.filter;

import java.io.IOException;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cts.library.authentication.CurrentUser;
import com.cts.library.exception.UnauthorizedAccessException;
import com.cts.library.model.MemberToken;
import com.cts.library.repository.MemberTokenRepo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @Component
public class AuthenticationFilter extends OncePerRequestFilter{
	
	private MemberTokenRepo memberTokenRepo;
    private CurrentUser currentUser;
    
    public AuthenticationFilter(MemberTokenRepo memberTokenRepo, CurrentUser currentUser) {
    	this.memberTokenRepo = memberTokenRepo;
    	this.currentUser = currentUser;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
    	

                                        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                                            response.setStatus(HttpServletResponse.SC_OK);
                                            filterChain.doFilter(request, response);  // Continue filter chain to add CORS headers
                                            return;
                                        }
    	String path = request.getServletPath();

        
        

    	 if (path.startsWith("/login") ||
    			 path.startsWith("/member/register") ||
    			 path.startsWith("/books/get-books") ||
    			 path.startsWith("/admin/admin-register") 
    			 )
    		       {
    		        filterChain.doFilter(request, response);
    		        return;
    		    }
 
        String token = request.getHeader("Authorization");
        
        if (token != null && token.startsWith("Basic ")) {
            token = token.substring(6);
        }
 
        System.out.println("Token received: " + token);
        
 
        if (token != null && !token.isBlank()) {
        	MemberToken memberToken =  memberTokenRepo.findByMemberToken(token)
                .orElseThrow(() -> new UnauthorizedAccessException("Invalid or expired token"));
 
            currentUser.setCurrentUser(memberToken.getMember());
 
        }else {
           
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization token");
            return;
        }
 
        filterChain.doFilter(request, response);
    }
    	

}



