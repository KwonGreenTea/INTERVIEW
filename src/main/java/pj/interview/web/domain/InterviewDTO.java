package pj.interview.web.domain;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter 
@Setter
@ToString 
public class InterviewDTO {
	
	private int interviewId;
    private String memberId;
    private String question;
    private String answer;
    private String suggest;
    private String emotion;
    private String intention;
    private String sector;
    private String gender;
    private String career;
    private String grade;
    private Date createdDate;
    
    private List<String> feedbackList;
    
    
	
} // end InterviewDTO
