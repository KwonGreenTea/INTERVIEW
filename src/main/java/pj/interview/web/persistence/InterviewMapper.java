package pj.interview.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import pj.interview.web.domain.Board;
import pj.interview.web.domain.InterviewDTO;
import pj.interview.web.util.Pagination;

@Mapper
public interface InterviewMapper {
	
	// 메서드 이름은 mapper xml에서 SQL 쿼리 정의 태그의 id와 동일
	// 매개변수는 mapper xml에서 #{변수명}과 동일(클래스 타입은 각 멤버변수명과 매칭) 
	// @Param : 자바 객체의 속성을 mapper에 매핑
	int createInterview(InterviewDTO interviewDTO);
	int updateRslInterview(InterviewDTO interviewDTO);
} // end BoardMapper
