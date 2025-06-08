package pj.interview.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;
import pj.interview.web.service.MemberService;

@Controller // @Component
// * 표현 계층(Presentation Layer)
// - 클라이언트(JSP 페이지 등)와 service를 연결하는 역할
@RequestMapping(value = "/interview") 
@Log4j	
public class InterviewController {
	
	@Autowired
	private MemberService memberService;

	@GetMapping("interview")
	public void interviewGET() {
		log.info("interviewGET()");
		
		
	} // end interviewGET()

	@GetMapping("userList")
	public void userListGET() {
		log.info("userListGET()");
		
		
	}
	
	
} // end InterviewController