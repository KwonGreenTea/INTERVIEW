package pj.interview.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.log4j.Log4j;
import pj.interview.web.domain.BoardDTO;
import pj.interview.web.service.BoardService;
import pj.interview.web.util.PageMaker;
import pj.interview.web.util.Pagination;

// 창준 왔다감 0608
@Controller // @Component
// * 표현 계층(Presentation Layer)
// - 클라이언트(JSP 페이지 등)와 service를 연결하는 역할
@RequestMapping(value = "/board") 
@Log4j
public class BoardController {

	@Autowired
	private BoardService boardService;

	// 페이지 번호 및 페이지 사이즈를 전송받아 게시글 페이징 처리
	// list.jsp 페이지로 페이징 처리된 게시글 데이터 및 페이지 번호 구성 데이터 전송
	@GetMapping("/list")
	public void list(Model model, Pagination pagination) { // pagination을 클라이언트에서 전송
		log.info("list()");
		log.info("pagnation = " + pagination);
		List<BoardDTO> boardList = boardService.getPagingBoards(pagination);
		
		PageMaker pageMaker = new PageMaker(); // PageMaker 객체 생성
		pageMaker.setPagination(pagination); // pagination 객체 적용
		pageMaker.setTotalCount(boardService.getTotalCount(pagination)); // totalCount 값 적용 
		
		log.info(pageMaker);
		model.addAttribute("boardList", boardList);
		model.addAttribute("pageMaker", pageMaker);
	} // end list()

	@GetMapping("/register")
	public void registerGET() {
		log.info("registerGET()");
	} // end registerGET()

	// 게시글 데이터를 form-data를 전송받아 BoardService로 전송
	@PostMapping("/register")
	public String registerPOST(BoardDTO boardDTO, RedirectAttributes reAttr) {
		log.info("registerPOST()");
		log.info(boardDTO.toString());

		int result = boardService.createBoard(boardDTO);
		log.info(result + "행 등록");
		return "redirect:/board/list";
	} // end registerPOST()

	@GetMapping("/detail")
	public void detail(Model model, Integer boardId, @ModelAttribute("pagination") Pagination pagination) {
		log.info("detail()");
		log.info("boardId = " + boardId);
		log.info("pagination = " + pagination);
		BoardDTO boardDTO = boardService.getBoardById(boardId);

		model.addAttribute("boardDTO", boardDTO);
	} // end detail()

	// 선택된 게시글을 modify.jsp로 전송
	@GetMapping("/modify")
	public void modifyGET(Model model, Integer boardId, @ModelAttribute("pagination") Pagination pagination) {
		log.info("modifyGET()");
		log.info("boardId = " + boardId);
		log.info("pagination = " + pagination);
		BoardDTO boardDTO = boardService.getBoardById(boardId);
		model.addAttribute("boardDTO", boardDTO);
	} // end updateGET()

	// modify.jsp에서 수정할 데이터를 전송받아 게시글 데이터 수정
	@PreAuthorize("isAuthenticated() and (principal.username == #boardDTO.memberId)")
	@PostMapping("/modify")
	public String modifyPOST(BoardDTO boardDTO, Pagination pagination, RedirectAttributes reAttr) {
		log.info("modifyPOST()");
		log.info("boardDTO = " + boardDTO.toString());
		int result = boardService.updateBoard(boardDTO);
		log.info(result + "행 수정");
		
		// redirect에서 값을 전송하기 위한 방법
		reAttr.addAttribute("pageNum", pagination.getPageNum());
		reAttr.addAttribute("pageSize", pagination.getPageSize());
		reAttr.addAttribute("type", pagination.getType());
		reAttr.addAttribute("keyword", pagination.getKeyword());
		
		return "redirect:/board/list";
	} // end updatePOST()

	// detail.jsp에서 boardId를 전송받아 게시글 데이터 삭제
	@PreAuthorize("isAuthenticated() and (principal.username == #memberId)")
	@PostMapping("/delete")
	public String delete(Integer boardId, Pagination pagination, String memberId, RedirectAttributes reAttr) {
		log.info("delete() 호출 : boardId = " + boardId);
		int result = boardService.deleteBoard(boardId);
		log.info(result + "행 삭제");
		
		// redirect에서 값을 전송하기 위한 방법
		reAttr.addAttribute("pageNum", pagination.getPageNum());
		reAttr.addAttribute("pageSize", pagination.getPageSize());
		reAttr.addAttribute("type", pagination.getType());
		reAttr.addAttribute("keyword", pagination.getKeyword());
		
		return "redirect:/board/list";
	} // end delete()

} // end BoardController
