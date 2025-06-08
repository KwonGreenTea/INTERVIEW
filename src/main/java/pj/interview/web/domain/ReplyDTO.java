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
public class ReplyDTO {
	private int replyId;
	private int boardId;
	private String memberId;
	private String replyContent;
	private Date replyDateCreated;
	
} // end ReplyDTO