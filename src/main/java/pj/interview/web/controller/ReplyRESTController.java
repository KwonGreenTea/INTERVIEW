package pj.interview.web.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j;
import pj.interview.web.domain.ReplyDTO;
import pj.interview.web.service.ReplyService;


// * RESTful url과 의미
// /reply (POST) : 댓글 추가(insert)
// /reply/all/숫자 (GET) : 해당 글 번호(boardId)의 모든 댓글 검색(select)
// /reply/숫자 (PUT) : 해당 댓글 번호(replyId)의 내용을 수정(update)
// /reply/숫자 (DELETE) : 해당 댓글 번호(replyId)의 댓글을 삭제(delete)

@RestController
@RequestMapping(value="/reply")
@Log4j
public class ReplyRESTController {
	
	@Autowired
	private ReplyService replyService;
	
	@PreAuthorize("principal.username == #replyDTO.memberId")
	@PostMapping // POST : 댓글 입력
	public ResponseEntity<Integer> createReply(@RequestBody ReplyDTO replyDTO) {
		log.info("createReply()");
		log.info("replyDTO = " + replyDTO);
		
		int result = replyService.createReply(replyDTO);

		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	@GetMapping("/all/{boardId}") // GET : 댓글 선택(all)
	public ResponseEntity<List<ReplyDTO>> readAllReply(
			@PathVariable("boardId") int boardId) {
		// @PathVariable("boardId") : {boardId} 값을 설정된 변수에 저장
		log.info("readAllReply()");
		log.info("boardId = " + boardId);
		
		List<ReplyDTO> list = replyService.getAllReply(boardId);
		return new ResponseEntity<List<ReplyDTO>>(list, HttpStatus.OK);
	}
	
	@PreAuthorize("principal.username == #replyDTO.memberId")
	@PutMapping("/{replyId}") // PUT : 댓글 수정
	public ResponseEntity<Integer> updateReply(
			@PathVariable("replyId") int replyId,
			@RequestBody ReplyDTO replyDTO
			){
		log.info("updateReply()");
		log.info("replyId = " + replyId);
		int result = replyService.updateReply(replyId, replyDTO.getReplyContent());
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	@PreAuthorize("principal.username == #memberId")
	@DeleteMapping("/{replyId}/{boardId}") // DELETE : 댓글 삭제
	public ResponseEntity<Integer> deleteReply(
			@PathVariable("replyId") int replyId, 
			@PathVariable("boardId") int boardId, 
			@RequestBody String memberId) {
		log.info("deleteReply()");
		log.info("replyId = " + replyId);
		
		int result = replyService.deleteReply(replyId, boardId);
		
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
} // ReplyRESTcontroller
