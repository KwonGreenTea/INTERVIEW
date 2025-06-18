package pj.interview.web.service;

import pj.interview.web.domain.MemberDTO;

import java.util.Collection;

public interface MemberService {
	int createMember(MemberDTO memberDTO); // 회원 정보 등록
	int checkMemberId(String memberId); // 아이디 중복 체크
	MemberDTO getMemberById(String memberId); // 회원 정보 조회
	MemberDTO selectKorInfo(String memberId);
	int updateMember(MemberDTO memberDTO); // 회원 정보 수정
	int deleteMember(String memberId); // 회원 정보 삭제
	Collection<MemberDTO> selectSameSector(String sector); //같은 직군의 사용자 조회
}
