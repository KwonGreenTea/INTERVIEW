package pj.interview.web.service;

import pj.interview.web.domain.InterviewDTO;
import pj.interview.web.domain.MemberDTO;

import java.util.Collection;
import java.util.List;

public interface InterviewService {

	int createInterview(InterviewDTO interviewDTO);

	int updateRslInterview(InterviewDTO interviewDTO);

	List<MemberDTO> getOtherInterview(String sector);
	
} // end InterviewService
