package pj.interview.web.service;

import java.util.ArrayList;
import java.util.Collection;

import pj.interview.web.domain.InterviewDTO;
import pj.interview.web.domain.MemberDTO;

public interface InterviewService {

	int createInterview(InterviewDTO interviewDTO);

	int updateRslInterview(InterviewDTO interviewDTO);

	Collection<MemberDTO> getOtherInterview(String sector);

	ArrayList<MemberDTO> getInterviewInfo(String memberId);
	
} // end InterviewService
