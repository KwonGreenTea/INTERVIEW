package pj.interview.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;
import pj.interview.web.domain.MemberDTO;
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
	public ResponseEntity<Integer> interviewGET(@AuthenticationPrincipal UserDetails userDetails) {
		log.info("interviewGET()");
		
		// User 정보를 불러움
		String memberId = userDetails.getUsername();
		MemberDTO memberDTO = memberService.getMemberById(memberId);
		
		// User 정보 중 면접 데이터에 필요한 파일이 없을 시 예외처리
		if ("".equals(memberDTO.getCareer())) {
			return new ResponseEntity<Integer>(10, HttpStatus.OK);
		} else if ("".equals(memberDTO.getGender())) {
			return new ResponseEntity<Integer>(11, HttpStatus.OK);
		} else if ("".equals(memberDTO.getSector())) {
			return new ResponseEntity<Integer>(12, HttpStatus.OK);
		}
		
		// User 정보를 Json 파일로 만들어 디렉토리에 저장

		
		// 질문 파일이 생성될 때까지 대기 후 질문 Json 파일이 생성되면 화면에 뿌림
		
		return new ResponseEntity<Integer>(1, HttpStatus.OK);
		
	} // end interviewGET()

	@GetMapping("userList")
	public void userListGET() {
		log.info("userListGET()");
		
		
	}
	
	
} // end InterviewController