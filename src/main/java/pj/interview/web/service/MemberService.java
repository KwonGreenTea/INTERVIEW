package pj.interview.web.service;

import pj.interview.web.domain.MemberDTO;

public interface MemberService {
	int createMember(MemberDTO memberDTO); // 회원 정보 등록
	int checkMemberId(String memberId); // 아이디 중복 체크
	MemberDTO getMemberById(String memberId); // 회원 정보 조회
	int updateMember(MemberDTO memberDTO); // 회원 정보 수정
	int deleteMember(String memberId); // 회원 정보 삭제
}
