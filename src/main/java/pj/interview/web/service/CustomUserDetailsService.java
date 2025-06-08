package pj.interview.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.extern.log4j.Log4j;
import pj.interview.web.domain.CustomUser;
import pj.interview.web.domain.Member;
import pj.interview.web.domain.MemberRole;
import pj.interview.web.persistence.MemberMapper;

@Log4j
public class CustomUserDetailsService implements UserDetailsService { 
	
    @Autowired
    private MemberMapper memberMapper;
   
    // 전송된 username으로 사용자 정보를 조회하고, UserDetails에 저장하여 리턴하는 메서드 
    @Override
    public UserDetails loadUserByUsername(String username) {
    	log.info("loadUserByUsername()");
    	log.info(username);
        // 사용자 ID를 이용하여 회원 정보와 권한 정보를 조회
        Member member = memberMapper.selectMemberByMemberId(username);
        MemberRole role = memberMapper.selectRoleByMemberId(username);
        
        // 조회된 회원 정보가 없을 경우 예외 처리
        if (member == null) {
            throw new UsernameNotFoundException("UsernameNotFound");
        }
        
        // 회원의 역할을 Spring Security의 GrantedAuthority 타입으로 변환하여 리스트에 추가
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        
        // UserDetails 객체를 생성하여 회원 정보와 역할 정보를 담아 반환
        UserDetails userDetails = new CustomUser(member, 
								        		authorities);
        return userDetails;
    }

}
