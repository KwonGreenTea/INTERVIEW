package pj.interview.web.service;

import pj.interview.web.domain.InterviewDTO;

public interface InterviewService {

	int createInterview(InterviewDTO interviewDTO);

	int updateRslInterview(InterviewDTO interviewDTO);
	
	
} // end InterviewService
