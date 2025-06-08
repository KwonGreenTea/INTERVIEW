package pj.interview.web.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter 
@Setter
@ToString 
public class Member implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String memberId;
	private String memberPw;
	private String memberName;
	private Date regDate;
	private int enabled;
}
