package pj.interview.web.service;

import pj.interview.web.domain.InterviewDTO;
import pj.interview.web.domain.MemberDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface InterviewService {

	int createInterview(InterviewDTO interviewDTO);

	int updateRslInterview(InterviewDTO interviewDTO);

	Collection<MemberDTO> getOtherInterview(String sector);

	Map<String,MemberDTO> getInterviewInfo(String memberId);
	
} // end InterviewService
