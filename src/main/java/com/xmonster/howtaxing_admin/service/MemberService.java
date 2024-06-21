package com.xmonster.howtaxing_admin.service;

import com.xmonster.howtaxing_admin.domain.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    public Map<String, Object> join(Member member);
    public boolean validateDuplicateMember(Member member);
    public List<Member> findMembers();
    public Member findOne(Long memberId);
}
