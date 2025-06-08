package pj.interview.web.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j;
import pj.interview.web.domain.Attach;
import pj.interview.web.domain.AttachDTO;
import pj.interview.web.domain.Board;
import pj.interview.web.domain.BoardDTO;
import pj.interview.web.persistence.AttachMapper;
import pj.interview.web.persistence.BoardMapper;
import pj.interview.web.util.Pagination;

@Service
@Log4j
public class BoardServiceImple implements BoardService{
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private AttachMapper attachMapper;

	@Transactional(value = "transactionManager") 
	@Override
	public int createBoard(BoardDTO boardDTO) {
		log.info("createBoard()");
		log.info("boardDTO = " + boardDTO);
		int insertBoardResult = boardMapper.insert(toEntity(boardDTO));
		log.info(insertBoardResult + "행 게시글 등록");
	
		List<AttachDTO> attachList = boardDTO.getAttachList();

		int insertAttachResult = 0;
		for(AttachDTO attachDTO : attachList) {
			insertAttachResult += attachMapper.insert(toEntity(attachDTO));
		}
		log.info(insertAttachResult + "행 파일 정보 등록");
		
		return 1;
	} // end createBoard()



	@Override
	public List<BoardDTO> getAllBoards() {
		log.info("getAllBoards()");
		List<Board> list = boardMapper.selectList();
		
		return list.stream().map(this::toDTO).collect(Collectors.toList());
	} // end getAllBoards()
  

	@Override
	public BoardDTO getBoardById(int boardId) {
		log.info("getBoardById()");
		log.info("boardId = " + boardId);
		Board board = boardMapper.selectOne(boardId);
		List<Attach> list = attachMapper.selectByBoardId(boardId);
		BoardDTO boardDTO = toDTO(board);
		
		List<AttachDTO> attachList = list.stream().map(this::toDTO).collect(Collectors.toList());
		
		boardDTO.setAttachList(attachList);
		return boardDTO;
	} // end getBoardById()


	@Transactional(value = "transactionManager") 
	@Override
	public int updateBoard(BoardDTO boardDTO) {
		log.info("updateBoard()");
		log.info("boardDTO = " + boardDTO);
		int updateBoardResult = boardMapper.update(toEntity(boardDTO));
		log.info(updateBoardResult + "행 게시글 정보 수정");
		
		int deleteAttachResult = attachMapper.delete(boardDTO.getBoardId());
		log.info(deleteAttachResult + "행 파일 정보 삭제");
		
		List<AttachDTO> attachList = boardDTO.getAttachList();
		
		int insertAttachResult = 0;
		for(AttachDTO attachDTO : attachList) {
			attachDTO.setBoardId(boardDTO.getBoardId()); // 게시글 번호 적용
			insertAttachResult += attachMapper.insertModify(toEntity(attachDTO));
		}
		log.info(insertAttachResult + "행 파일 정보 등록");
		return 1;
	} // end updateBoard()


	@Transactional(value = "transactionManager") 
	@Override
	public int deleteBoard(int boardId) {
		log.info("deleteBoard()");
		log.info("boardId = " + boardId);
		int deleteBoardResult = boardMapper.delete(boardId);
		log.info(deleteBoardResult + "행 게시글 정보 삭제");
		int deleteAttachResult = attachMapper.delete(boardId);
		log.info(deleteAttachResult + "행 파일 정보 삭제");
		return 1;
	} // end deleteBoard()
	
	
	@Override
	public List<BoardDTO> getPagingBoards(Pagination pagination) {
		log.info("getPagingBoards()");
		List<Board> list = boardMapper.selectListByPagination(pagination);
		
		return list.stream().map(this::toDTO).collect(Collectors.toList());
	} // end getPagingBoards()
	

	@Override
	public int getTotalCount(Pagination pagination) {
		log.info("getTotalCount");
		return boardMapper.selectTotalCount(pagination);
	} // end getTotalCount()
	
	// Board 데이터를 BoardDTO에 적용하는 메서드
	public BoardDTO toDTO(Board board) {
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setBoardId(board.getBoardId());
		boardDTO.setBoardTitle(board.getBoardTitle());
		boardDTO.setBoardContent(board.getBoardContent());
		boardDTO.setMemberId(board.getMemberId());
		boardDTO.setBoardDateCreated(board.getBoardDateCreated());
		boardDTO.setReplyCount(board.getReplyCount());
		return boardDTO;
	} // end toDTO()
	
	// BoardDTO 데이터를 Board에 적용하는 메서드
	public Board toEntity(BoardDTO boardDTO) {
		Board board = new Board();
		board.setBoardId(boardDTO.getBoardId());
		board.setBoardTitle(boardDTO.getBoardTitle());
		board.setBoardContent(boardDTO.getBoardContent());
		board.setMemberId(boardDTO.getMemberId());
		board.setReplyCount(boardDTO.getReplyCount());
		return board;
	} // end toEntity()
	
    // AttachDTO를 Attach로 변환하는 메서드
    private Attach toEntity(AttachDTO attachDTO) {
        Attach attach = new Attach();
        attach.setAttachId(attachDTO.getAttachId());
        attach.setBoardId(attachDTO.getBoardId());
        attach.setAttachPath(attachDTO.getAttachPath());
        attach.setAttachRealName(attachDTO.getAttachRealName());
        attach.setAttachChgName(attachDTO.getAttachChgName());
        attach.setAttachExtension(attachDTO.getAttachExtension());
        attach.setAttachDateCreated(attachDTO.getAttachDateCreated());
        return attach;
    }
    
    // Attach를 AttachDTO로 변환하는 메서드
    private AttachDTO toDTO(Attach attach) {
    	AttachDTO attachDTO = new AttachDTO();
        attachDTO.setAttachId(attach.getAttachId());
        attachDTO.setBoardId(attach.getBoardId());
        attachDTO.setAttachPath(attach.getAttachPath());
        attachDTO.setAttachRealName(attach.getAttachRealName());
        attachDTO.setAttachChgName(attach.getAttachChgName());
        attachDTO.setAttachExtension(attach.getAttachExtension());
        attachDTO.setAttachDateCreated(attach.getAttachDateCreated());
        return attachDTO;
    }
	
} // end BoardServiceImple
