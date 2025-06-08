package pj.interview.web.service;

import java.util.List;

import pj.interview.web.domain.ReplyDTO;

public interface ReplyService {
	int createReply(ReplyDTO replyDTO);
	List<ReplyDTO> getAllReply(int boardId);
	int updateReply(int replyId, String replyContent);
	int deleteReply(int replyId, int boardId);
} // end ReplyService
