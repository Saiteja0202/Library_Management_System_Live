package com.cts.library.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.library.model.Member;
import com.cts.library.model.MemberToken;

@Repository
public interface MemberTokenRepo extends JpaRepository<MemberToken, Long>{
	
	Optional<MemberToken> findByMemberToken(String token);

	Optional<MemberToken> findByMember(Member member);

	@Query("SELECT u FROM MemberToken u WHERE u.generatedAt <= :expiry")
	List<MemberToken> findExpiredTokens(@Param("expiry") LocalDateTime expiry);
	
	void deleteByMember_MemberId(Long memberId);

}
