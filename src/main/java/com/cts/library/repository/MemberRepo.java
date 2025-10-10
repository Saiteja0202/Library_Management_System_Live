package com.cts.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.library.model.Member;


@Repository
public interface MemberRepo extends JpaRepository<Member, Long>{
	
	Member findByUsername(String username);
	boolean existsByUsername(String username);
	@Query("SELECT m FROM Member m LEFT JOIN FETCH m.transactions LEFT JOIN FETCH m.fines")
	List<Member> findAllWithTransactions();
	
}
