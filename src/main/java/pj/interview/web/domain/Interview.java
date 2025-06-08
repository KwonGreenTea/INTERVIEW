package pj.interview.web.domain;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter 
@Setter
@ToString 
public class Interview {
	
	private int interviewId; // INTERVIEW_ID
    private String memberId; // MEMBER_ID
    private String question; // QUESTION
    private String answer; // ANSWER
    private String suggest; // SUGGEST
    private Date createdDate; // CREATED_DATE
    private String emotion; // EMOTION
    private String intention; // INTENTION
	
} // end Interview
