package pj.interview.web.domain;

import java.util.ArrayList;
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
public class BoardDTO {

	private int boardId; 
	private String boardTitle; 
	private String boardContent;
	private String memberId; 
	private Date boardDateCreated; 
	private int replyCount;
	
	private List<AttachDTO> attachList;
	
	public List<AttachDTO> getAttachList() {
		if(attachList == null) {
			attachList = new ArrayList<AttachDTO>();
		}
		return attachList;
	}
	
} // end BoardDTO
