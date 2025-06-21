package pj.interview.web.service;

import java.util.ArrayList;

import pj.interview.web.domain.InterviewDTO;
import pj.interview.web.domain.MemberDTO;

public interface InterviewService {

	int createInterview(InterviewDTO interviewDTO);

	int updateRslInterview(InterviewDTO interviewDTO);

	ArrayList<MemberDTO> getInterviewInfo(String memberId);

	ArrayList<InterviewDTO> getOtherInterview();
	
} // end InterviewService
