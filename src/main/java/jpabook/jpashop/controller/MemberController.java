package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult result) { // javax.validation 적용, valid 오류가 있다면 BindingResult에 담겨서 실행. 에러를 화면에 뿌릴 수 있다.


        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping(value = "members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        // 여기도 Member Entity를 바로 쓰기보다는 화면에 뿌리기 위한 object를 새로 정의해서 보내는 것이 좋다.
        return "members/memberList";
    }


}
