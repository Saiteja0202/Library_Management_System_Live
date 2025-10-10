package com.cts.library.test;

import com.cts.library.authentication.CurrentUser;
import com.cts.library.exception.ResourceNotFoundException;
import com.cts.library.model.Member;
import com.cts.library.model.Role;
import com.cts.library.repository.*;
import com.cts.library.service.MemberServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepo memberRepo;

    @Mock
    private BorrowingTransactionRepo transactionRepo;

    @Mock
    private FineRepo fineRepo;

    @Mock
    private NotificationRepo notificationRepo;

    @Mock
    private MemberTokenRepo memberTokenRepo;

    @Mock
    private CurrentUser currentUser;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    public void testRegisterMember_ShouldSucceed() {
        Member member = new Member();
        member.setUsername("sai");
        member.setPassword("password");

        when(memberRepo.existsByUsername("sai")).thenReturn(false);
        when(memberRepo.save(any(Member.class))).thenReturn(member);

        String result = memberService.registerMember(member);
        assertEquals("Member registered successfully.", result);
        System.out.println("Member registered.");
    }

    @Test
    public void testGetMemberById_Found() {
        Member admin = new Member();
        admin.setRole(Role.ADMIN);
        admin.setMemberId(1L);

        Member target = new Member();
        target.setMemberId(2L);

        when(currentUser.getCurrentUser()).thenReturn(admin);
        when(memberRepo.findById(2L)).thenReturn(Optional.of(target));

        Member result = memberService.getMemberById(2L);
        assertEquals(2L, result.getMemberId());
        System.out.println("Member retrieved.");
    }

    @Test
    public void testGetMemberById_NotFound() {
        Member admin = new Member();
        admin.setRole(Role.ADMIN);
        admin.setMemberId(1L);

        when(currentUser.getCurrentUser()).thenReturn(admin);
        when(memberRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.getMemberById(1L));
        System.out.println("Exception thrown as expected.");
    }
}
