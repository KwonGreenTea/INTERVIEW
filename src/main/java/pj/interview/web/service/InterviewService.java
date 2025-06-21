package pj.interview.web.service;

import java.util.ArrayList;

import pj.interview.web.domain.InterviewDTO;

public interface InterviewService {

	int createInterview(InterviewDTO interviewDTO);

	int updateRslInterview(InterviewDTO interviewDTO);

	ArrayList<InterviewDTO> getInterviewInfo(String memberId);

	ArrayList<InterviewDTO> getOtherInterview();

	ArrayList<InterviewDTO> getInterviewInfoForInterviewId(int interviewId);
	
} // end InterviewService
