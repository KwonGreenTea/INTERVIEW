package pj.interview.web.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter 
@Setter
@ToString 
public class MemberRole {
	private int roleId;
	private String memberId;
	private String roleName;
}
