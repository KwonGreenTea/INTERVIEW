package pj.interview.web.service;

import java.util.List;

import pj.interview.web.domain.BoardDTO;
import pj.interview.web.util.Pagination;

public interface BoardService {
	
	int createBoard(BoardDTO boardDTO); // 게시글 등록
	List<BoardDTO> getAllBoards(); // 전체 게시글 조회
	BoardDTO getBoardById(int boardId); // 특정 게시글 조회
	int updateBoard(BoardDTO boardDTO); // 특정 게시글 수정
	int deleteBoard(int boardId); // 특정 게시글 삭제
	List<BoardDTO> getPagingBoards(Pagination pagination); // 게시글 페이징 처리 조회 
	int getTotalCount(Pagination pagination); // 게시글 수 조회
} // end BoardService
