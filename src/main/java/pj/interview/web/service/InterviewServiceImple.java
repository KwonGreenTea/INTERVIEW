package pj.interview.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;
import pj.interview.web.domain.InterviewDTO;
import pj.interview.web.domain.MemberDTO;
import pj.interview.web.persistence.InterviewMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Log4j
public class InterviewServiceImple implements InterviewService{
	
	@Autowired
	private InterviewMapper interviewMapper;

	@Override
	public int createInterview(InterviewDTO interviewDTO) {
		return interviewMapper.createInterview(interviewDTO);
	}

	@Override
	public int updateRslInterview(InterviewDTO interviewDTO) {
		return interviewMapper.updateRslInterview(interviewDTO);
	}
	
	@Override
	public Collection<MemberDTO> getOtherInterview(String sector) {return interviewMapper.getOtherInterview(sector);}

	@Override
	public Map<String,MemberDTO> getInterviewInfo(String memberId) {return interviewMapper.getInterviewInfo(memberId);}


} // end InterviewServiceImple
