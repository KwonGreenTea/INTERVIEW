package pj.interview.web.persistence;

import org.apache.ibatis.annotations.Mapper;

import pj.interview.web.domain.Member;
import pj.interview.web.domain.MemberDTO;
import pj.interview.web.domain.MemberRole;

import java.util.Collection;
import java.util.List;

@Mapper
public interface MemberMapper {
	int insertMember(Member member);
	int insertMemberRole(String memberId);
	Member selectMemberByMemberId(String memberId);
	MemberRole selectRoleByMemberId(String memberId);
	int selectExistingMemberId(String memberId);
	int updateMember(Member member);
	int deleteMember(String memberId);
	int deleteMemberRole(String memberId);
	List<MemberDTO> selectSameSector(String sector);
}
