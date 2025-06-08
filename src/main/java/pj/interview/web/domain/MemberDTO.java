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
public class MemberDTO {
	private String memberId;
	private String memberPw;
	private String memberName;
	private Date regDate;
	private int enabled;
}