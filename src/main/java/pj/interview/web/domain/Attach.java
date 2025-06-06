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
public class Attach {
	private int attachId;
	private int boardId;
	private String attachPath;
	private String attachRealName;
	private String attachChgName;
	private String attachExtension;
	private Date attachDateCreated;
}
