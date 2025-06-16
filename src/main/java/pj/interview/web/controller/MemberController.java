package pj.interview.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.log4j.Log4j;
import pj.interview.web.domain.MemberDTO;
import pj.interview.web.service.MemberService;

@Controller
@RequestMapping(value = "/member") 
@Log4j
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// join.jsp 페이지 호출
	@GetMapping("/join")
	public void joinGET() {
		log.info("joinGET()");
	}
	
	// join.jsp에서 전송된 memberDTO 데이터를 DB에 저장
	@PostMapping("/join")
	public String joinPOST(MemberDTO memberDTO) {
		log.info("joinPOST()");
		// 비밀번호 암호화
		String encPw = passwordEncoder.encode(memberDTO.getMemberPw());
		memberDTO.setMemberPw(encPw); // 암호화된 데이터 적용
		int result = memberService.createMember(memberDTO);
		log.info(result + "행 등록");
		return "redirect:/auth/login";
	}

	// Ajax로 전송된 memberId 데이터로 아이디 중복 체크
	@GetMapping("/check/{memberId}")
	@ResponseBody
	public ResponseEntity<Integer> checkId(@PathVariable("memberId") String memberId) {
		log.info("checkId()");
		
		int result = memberService.checkMemberId(memberId);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// 로그인한 사용자의 정보를 info.jsp 페이지에 출력
	@GetMapping("/info")
	public void info(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		// @AuthenticationPrincipal : 인증된 사용자의 Principal을 주입
		log.info("info()");
		String memberId = userDetails.getUsername(); // 로그인된 사용자 아이디 저장
		MemberDTO memberDTO = memberService.selectKorInfo(memberId);
		model.addAttribute("memberDTO", memberDTO);
	}
	
	// modify.jsp 페이지 호출
	@GetMapping("/modify")
	public void modifyGET(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		log.info("modifyGET()");
		String memberId = userDetails.getUsername();
		MemberDTO memberDTO = memberService.selectKorInfo(memberId);
		model.addAttribute("memberDTO", memberDTO);
	}
	
	// modify.jsp에서 전송된 회원 정보로 데이터 수정
	@PreAuthorize("principal.username == #memberDTO.memberId")
	@PostMapping("/modify")
	public String modifyPOST(MemberDTO memberDTO) {
		log.info("modifyPOST()");
		String encPw = passwordEncoder.encode(memberDTO.getMemberPw());
		memberDTO.setMemberPw(encPw); 
		memberService.updateMember(memberDTO);
		
		return "redirect:/member/info";
	}
	
	// 로그인된 회원 아이디로 회원 정보 삭제
	@PostMapping("/delete")
	public String delete(@AuthenticationPrincipal UserDetails userDetails) {
		log.info("delete()");
		String memberId = userDetails.getUsername();
		memberService.deleteMember(memberId);
		SecurityContextHolder.clearContext(); // 인증 정보 초기화
		return "redirect:/board/list";
	}

}





