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
public class InterviewDTO {
	
	private int interviewId;
    private String memberId;
    private String question;
    private String answer;
    private String suggest;
    private String emotion;
    private String intention;
    private int emotionScore;
    private int intentionScore;
    private int totalScore;
    private int lengthScore;
    private int qualityScore;
    private int wordCount;
    private String grade;
    private Date createdDate;
	
} // end InterviewDTO
