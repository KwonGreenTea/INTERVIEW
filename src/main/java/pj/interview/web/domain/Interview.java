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
	
} // end Interview
