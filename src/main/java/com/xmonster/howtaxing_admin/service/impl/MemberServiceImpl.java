package com.xmonster.howtaxing_admin.service.impl;

import com.xmonster.howtaxing_admin.domain.Member;
import com.xmonster.howtaxing_admin.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class MemberServiceImpl implements MemberService {

    //private final MemberMapper memberMapper;

   /* @Autowired
    public MemberServiceImpl(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }*/

    /**
     * 회원 가입
     */
    public Map<String, Object> join(Member member){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String rtnMsg = "";
        String result = "success";

        // 회원 정보 중복 여부 체크
        if(validateDuplicateMember(member)){
            result = "fail";
            rtnMsg = "이미 존재하는 회원입니다.";
            System.out.println("회원명 중복");
        }else{
            // 회원가입 DB 저장
           /* if(memberMapper.save(member) != 1){
                result = "fail";
                rtnMsg = "데이터를 저장하는 중 오류가 발생했습니다.";
                System.out.println("DB insert 오류");
            }*/
        }

        resultMap.put("result", result);
        resultMap.put("rtnMsg", rtnMsg);

        return resultMap;
    }

    /**
     * 중복 회원 검증
     */
    public boolean validateDuplicateMember(Member member) {
        //memberMapper.findByName(member.getName());
        Member m =null;
        boolean result = false;
        if(m != null){
            result = true;
            //throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        return result;
                /*.ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });*/
        /*Member m = memberMapper.findByName(member.getName());
        System.out.println("GGMANYAR : " + m.getName());*/
    }

    @Override
    public List<Member> findMembers() {
        return null;
    }

    @Override
    public Member findOne(Long memberId) {
        return null;
    }

    /**
     * 전체 회원 조회
     */
    /*public List<Member> findMembers() {
        //return memberMapper.findAll();
    }*/

    /**
     * 입력 회원 조회
     */
    /*public Member findOne(Long memberId){
        return memberMapper.findById(memberId);
    }*/
}
