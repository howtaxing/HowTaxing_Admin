package com.xmonster.howtaxing_admin.controller;

import com.xmonster.howtaxing_admin.domain.Member;
import com.xmonster.howtaxing_admin.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm(){
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    @ResponseBody
    //public ModelAndView create(MemberForm form){
    public Map<String, Object> create(Model model, @RequestParam Map<String, Object> paramMap){

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Member member = new Member();
        //member.setName(form.getName());
        member.setName((String)paramMap.get("name"));
        System.out.println("member : " + member.getName());

        resultMap = memberService.join(member);
        //String data = Integer.toString(result);

        //resultMap.put("result", result);
        //model.addAttribute("resultMap", resultMap);
        //model.addAttribute("result", (String)resultMap.get("result"));
        //model.addAttribute("rtnMsg", (String)resultMap.get("rtnMsg"));

        /*if(result == 0){
            modelAndView.setViewName("home");
        }else{
            modelAndView.setViewName("members/createMemberForm");
            modelAndView.addObject("data", data);
        }*/

        //return modelAndView;
        //return "members/createMemberForm";
        return resultMap;

        //return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
