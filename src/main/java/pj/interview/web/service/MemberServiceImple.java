package pj.interview.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j;
import pj.interview.web.domain.Member;
import pj.interview.web.domain.MemberDTO;
import pj.interview.web.persistence.MemberMapper;

@Service
@Log4j
public class MemberServiceImple implements MemberService {
	
	@Autowired
	private MemberMapper memberMapper;

	@Transactional(value = "transactionManager")
	@Override
	public int createMember(MemberDTO memberDTO) {
		log.info("createMember()");
		int insertMemberResult = memberMapper.insertMember(toEntity(memberDTO));
		log.info(insertMemberResult + "행 회원 정보 등록");
	
		int insertRoleResult = memberMapper.insertMemberRole(memberDTO.getMemberId());
		log.info(insertRoleResult + "행 권한 정보 등록");
		return 1;
	}

	@Override
	public int checkMemberId(String memberId) {
		log.info("checkMemberId()");
		
		return memberMapper.selectExistingMemberId(memberId);
	}
	
	@Override
	public MemberDTO getMemberById(String memberId) {
		log.info("getMemberById()");
		MemberDTO member = toDTO(memberMapper.selectMemberByMemberId(memberId));

		if (member != null) {
			String sector = member.getSector();
			switch (sector) {
				case "BM":
					member.setSector("비즈니스 매니저");
					break;
				case "SM":
					member.setSector("영업 매니저");
					break;
				case "PS":
					member.setSector("제품 전문가");
					break;
				case "RND":
					member.setSector("연구 개발 부서");
					break;
				case "ICT":
					member.setSector("정보통신기술");
					break;
				case "ARD":
					member.setSector("응용 연구 개발");
					break;
				case "MM":
					member.setSector("마케팅 매니저");
					break;
				default:
					member.setSector("알 수 없는 직군");
					break;
			}

			if("female".equals(member.getGender())){
				member.setGender("여성");
			}else if("male".equals(member.getGender())){
				member.setGender("남성");
			}
		}
		return member;
	}


	@Override
	public int updateMember(MemberDTO memberDTO) {
		log.info("updateMember()");
		return memberMapper.updateMember(toEntity(memberDTO));
	}

	@Transactional(value = "transactionManager")
	@Override
	public int deleteMember(String memberId) {
		log.info("deleteMember()");
		int deleteMemberResult = memberMapper.deleteMember(memberId);
		log.info(deleteMemberResult + "행 회원 정보 삭제");
		int deleteRoleResult = memberMapper.deleteMemberRole(memberId);
		log.info(deleteRoleResult + "행 권한 정보 삭제");
		return 1;
	}
	
	
    public MemberDTO toDTO(Member member) {
    	MemberDTO memberDTO = new MemberDTO();
    	memberDTO.setMemberId(member.getMemberId());
    	memberDTO.setMemberPw(member.getMemberPw());
    	memberDTO.setMemberName(member.getMemberName());
    	memberDTO.setSector(member.getSector());
    	memberDTO.setGender(member.getGender());
    	memberDTO.setCareer(member.getCareer());
    	memberDTO.setCreatedDate(member.getCreatedDate());
    	memberDTO.setUpdatedDate(member.getUpdatedDate());
    	memberDTO.setEnabled(member.getEnabled());

        return memberDTO;
    }

    public Member toEntity(MemberDTO memberDTO) {
        Member entity = new Member();
        entity.setMemberId(memberDTO.getMemberId());
        entity.setMemberPw(memberDTO.getMemberPw());
        entity.setMemberName(memberDTO.getMemberName());
        entity.setSector(memberDTO.getSector());
        entity.setGender(memberDTO.getGender());
        entity.setCareer(memberDTO.getCareer());
        entity.setCreatedDate(memberDTO.getCreatedDate());
        entity.setUpdatedDate(memberDTO.getUpdatedDate());
        entity.setEnabled(memberDTO.getEnabled());

        return entity;
    }



}