package com.ezen.spboard.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ezen.spboard.dto.SpMember;
import com.ezen.spboard.service.MemberService;

@Controller
public class MemberController {
	
	@Autowired
	MemberService ms;
	//멤버변수로 만들어 아래의 모든 객체에서 사용이 가능하도록 함
	
	//@RequestMapping(value="/", method=RequestMethod.GET)
	@RequestMapping("/")
	public String firstRequest(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("loginUser")==null)
			return "loginform";
		else		
			return "main";
	}
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(Model model, HttpServletRequest request) {
		
		String url = "loginform";
		String id=request.getParameter("id");
		String pw = request.getParameter("pw");
		
		SpMember sdto = ms.getMember(id);
		
		if(sdto == null) {
			model.addAttribute("message","id를 확인하세요");
		}else if(sdto.getPw()==null) {
			model.addAttribute("message","암호가 null 입니다. 관리자에게 문의하세요.");
		}else if(!sdto.getPw().equals(pw)) {
			model.addAttribute("message","비밀번호가 맞지 않습니다.");
		}else if(sdto.getPw().equals(pw)) {
			HttpSession session = request.getSession();
			session.setAttribute("loginUser", sdto);
			url ="main";
		}else {
			model.addAttribute("message","원인 미상의 오류로 로그인 실패");
		}
		return url;
	}
}
