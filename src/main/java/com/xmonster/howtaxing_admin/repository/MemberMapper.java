package com.xmonster.howtaxing_admin.repository;

import com.xmonster.howtaxing_admin.domain.Member;

import java.util.List;

//@Mapper
public interface MemberMapper {
    int save(Member member);
    Member findById(Long id);
    Member findByName(String name);
    List<Member> findAll();
}
