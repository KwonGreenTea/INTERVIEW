package pj.interview.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import pj.interview.web.domain.Board;
import pj.interview.web.util.Pagination;

@Mapper
public interface BoardMapper {
	
	// 메서드 이름은 mapper xml에서 SQL 쿼리 정의 태그의 id와 동일
	// 매개변수는 mapper xml에서 #{변수명}과 동일(클래스 타입은 각 멤버변수명과 매칭) 
	int insert(Board board); // 게시글 등록
	List<Board> selectList(); // 전체 게시글 조회
	Board selectOne(int boardId); // 특정 게시글 조회
	int update(Board board); // 특정 게시글 수정
	int delete(int boardId); // 특정 게시글 삭제
	List<Board> selectListByPagination(Pagination pagination); // 전체 게시글 페이징 처리
	int selectTotalCount(Pagination pagination); // 게시글 수 조회
	int updateReplyCount(@Param("boardId") int boardId, @Param("amount") int amount);
	// @Param : 자바 객체의 속성을 mapper에 매핑
} // end BoardMapper
