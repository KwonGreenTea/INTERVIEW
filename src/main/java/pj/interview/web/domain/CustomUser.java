package pj.interview.web.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;


@Getter
public class CustomUser extends User { // User 클래스 상속

	private static final long serialVersionUID = 1L;
	
	private Member member;
	public CustomUser(Member member, 
					 Collection<? extends GrantedAuthority> authorities) {
		// Collection<? extends GrantedAuthority> authorities : 
		//  권한 정보를 저장하는 Collection
		
		// User 클래스 생성자에 username, password, authorities를 적용
		// 인증 및 권한 확인에 필요한 정보
		super(member.getMemberId(), member.getMemberPw(), authorities);
		
		// 전송된 member 객체 적용
		this.member = member;
	}

}
