package pj.interview.web.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j;
import pj.interview.web.domain.Reply;
import pj.interview.web.domain.ReplyDTO;
import pj.interview.web.persistence.BoardMapper;
import pj.interview.web.persistence.ReplyMapper;

@Service
@Log4j
public class ReplyServiceImple implements ReplyService{
	
	@Autowired
	private ReplyMapper replyMapper;
	
	@Autowired
	private BoardMapper boardMapper;
	
	
	@Transactional(value = "transactionManager") // transactionManager가 관리
	@Override
	public int createReply(ReplyDTO replyDTO){
		log.info("createReply()");
		log.info("replyDTO = " + replyDTO);
		int insertResult = replyMapper.insert(toEntity(replyDTO));
		log.info(insertResult + "행 댓글 추가");
		int updateResult = boardMapper.updateReplyCount(replyDTO.getBoardId(), 1);
		log.info(updateResult + "행 게시판 수정");
		return 1;
	}

	@Override
	public List<ReplyDTO> getAllReply(int boardId) {
		log.info("getAllReply()");
		log.info("boardId = " + boardId);
		List<Reply> list = replyMapper.selectListByBoardId(boardId);
		
		return list.stream().map(this::toDTO).collect(Collectors.toList());
	}


	@Override
	public int updateReply(int replyId, String replyContent) {
		log.info("updateReply()");
		log.info("replyId = " + replyId);
		log.info("replyContent = " + replyContent);
		Reply reply = new Reply();
		reply.setReplyId(replyId);
		reply.setReplyContent(replyContent);
		return replyMapper.update(reply);
	}

	@Transactional(value = "transactionManager")
	@Override
	public int deleteReply(int replyId, int boardId) {
		log.info("deleteReply()");
		log.info("replyId = " + replyId);
		int deleteResult = replyMapper.delete(replyId);
		log.info(deleteResult + "행 댓글 삭제");
		
		int updateResult = boardMapper
				.updateReplyCount(boardId, -1);
		log.info(updateResult + "행 게시판 수정");
		return 1;
	}
	
	public ReplyDTO toDTO(Reply reply) {
		ReplyDTO replyDTO = new ReplyDTO();
		replyDTO.setBoardId(reply.getBoardId());
		replyDTO.setReplyId(reply.getReplyId());
		replyDTO.setMemberId(reply.getMemberId());
		replyDTO.setReplyContent(reply.getReplyContent());
		replyDTO.setReplyDateCreated(reply.getReplyDateCreated());
		return replyDTO;
	} // end toDTO()
	
	public Reply toEntity(ReplyDTO replyDTO) {
		Reply reply = new Reply();
		reply.setBoardId(replyDTO.getBoardId());
		reply.setReplyId(replyDTO.getReplyId());
		reply.setMemberId(replyDTO.getMemberId());
		reply.setReplyContent(replyDTO.getReplyContent());
		reply.setReplyDateCreated(replyDTO.getReplyDateCreated());
		return reply;
	} // end toEntity()

} // end ReplyServiceImple
