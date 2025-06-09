package pj.interview.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j;
import pj.interview.web.domain.InterviewDTO;
import pj.interview.web.domain.MemberDTO;
import pj.interview.web.service.InterviewService;
import pj.interview.web.service.MemberService;

@Controller // @Component
// * 표현 계층(Presentation Layer)
// - 클라이언트(JSP 페이지 등)와 service를 연결하는 역할
@RequestMapping(value = "/interview")
@Log4j
public class InterviewController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private InterviewService interviewService;

	@GetMapping("/page")
	public void interviewGET(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		log.info("interviewGET()");
		
		// User 정보를 불러움
		String memberId = userDetails.getUsername();
		MemberDTO memberDTO = memberService.getMemberById(memberId);
		
		// User 정보 중 면접 데이터에 필요한 파일이 없을 시 예외처리
		if (memberDTO.getCareer() == null) { // 경력 미기입
			model.addAttribute("result", "해당 사용자의 경력이 기입되어있지 않습니다.");
		} else if (memberDTO.getGender() == null) { // 성별 미기입
			model.addAttribute("result", "해당 사용자의 성별이 기입되어있지 않습니다.");
		} else if (memberDTO.getSector() == null) { // 직군 미기입
			model.addAttribute("result", "해당 사용자의 직군이 기입되어있지 않습니다.");
		} else { 
			model.addAttribute("result", "1");
		}
	} // end interviewGET()

	// UTF-8 명시
	// User 정보를 보내 질문 가져오기
	@GetMapping(value = "/question", produces = "text/plain;charset=UTF-8") 
	public ResponseEntity<String> questionGET(Model model, @AuthenticationPrincipal UserDetails userDetails)
			throws IOException, InterruptedException {
		log.info("questionGET()");

		// User ID 불러옴
		String memberId = userDetails.getUsername();
		MemberDTO memberDTO = memberService.getMemberById(memberId);

		// 파일 경로
		// 디렉토리에 기존에 남아있는 파일이 있으면 삭제
		/* 배포 */
		String userFilePath = "/home/ubuntu/user/" + memberId + ".json";
		String questionFilePath = "/home/ubuntu/question/" + "q_" + memberId + ".json";
		String answerFilePath = "/home/ubuntu/answer/" + "a_" + memberId + ".json";
		String resultFilePath = "/home/ubuntu/result/" + "result_a_" + memberId + ".json";
		
		/* 개발
		String userFilePath = "D:\\upload\\" + memberId + ".json";
		String questionFilePath = "D:\\upload\\" + "q_" + memberId + ".json";
		String answerFilePath = "D:\\upload\\" + "a_" + memberId + ".json";
		String resultFilePath = "D:\\upload\\" + "result_a_" + memberId + ".json";
		*/
		File userFile = new File(userFilePath);
		File questionFile = new File(questionFilePath);
		File answerFile = new File(answerFilePath);
		File resultFile = new File(resultFilePath);
		
		if (userFile.exists()) userFile.delete();
		if (questionFile.exists()) questionFile.delete();
		if (answerFile.exists()) answerFile.delete();
		if (resultFile.exists()) resultFile.delete();
		
		// -- User 정보를 (memberId).Json 파일로 만들어 디렉토리에 저장
		Map<String, Object> userInfo = new LinkedHashMap<>();
		userInfo.put("occupation", memberDTO.getSector());
		userInfo.put("channel", "MOCK");
		userInfo.put("place", "ONLINE");
		userInfo.put("gender", memberDTO.getGender());
		userInfo.put("ageRange", "-34"); // 예시: 나이 고정
		userInfo.put("experience", memberDTO.getCareer());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writerWithDefaultPrettyPrinter().writeValue(userFile, userInfo);
        
        // 저장 후 파일 권한 777로 설정 (rwxrwxrwx)
        Path path = userFile.toPath();
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
        Files.setPosixFilePermissions(path, perms);

        log.info(memberId + "의 User Json 파일 생성 완료");
        
		// -- 질문이 생성될 때까지 대기 
		int waitTime = 0;
		while (!questionFile.exists() && waitTime < 30000) { // 최대 30초 O대기
			Thread.sleep(1000);
			waitTime += 1000;
		}

		// 생성되지 않았을 때
		if (!questionFile.exists()) {
			// User JSON 삭제
			userFile.delete();
			return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// -- JSON 파일을 읽어와서 질문 내용을 클라이언트에 전달
		// 질문 Json 불러오기
		Map<String, Object> jsonMap = objectMapper.readValue(questionFile, Map.class);

		// Json에서 중첩된 맵 꺼내기 question->text
		Map<String, Object> questionMap = (Map<String, Object>) jsonMap.get("question");
		String question = (String) questionMap.get("text");
		
		log.info(memberId + "의 Question Json 파일 내에 질문 출력 완료");
		
		// User JSON 삭제
		userFile.delete();
		
		InterviewDTO interviewDTO = new InterviewDTO();
		interviewDTO.setMemberId(memberId);
		interviewDTO.setQuestion(question);
		
		// INTERVIEW 테이블 질문 생성
		int result = interviewService.createInterview(interviewDTO);
		
		if(result > 0) {
			return new ResponseEntity<String>(question, HttpStatus.OK);
		} else {
			// Question JSON 삭제
			questionFile.delete();
			return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/answer", produces = "text/plain;charset=UTF-8") 
	public ResponseEntity<String> answerGET(String answer, @AuthenticationPrincipal UserDetails userDetails)
			throws IOException, InterruptedException {
		log.info("answerGET()");
		
		// User ID 불러옴
		String memberId = userDetails.getUsername();

		/* 배포 */
		String questionFilePath = "/home/ubuntu/question/" + "q_" + memberId + ".json";
		String answerFilePath = "/home/ubuntu/answer/" + "a_" + memberId + ".json";
		String resultFilePath = "/home/ubuntu/result/" + "result_a_" + memberId + ".json";
		
		/* 개발
		String questionFilePath = "D:\\upload\\" + "q_" + memberId + ".json";
		String answerFilePath = "D:\\upload\\" + "a_" + memberId + ".json";
		String resultFilePath = "D:\\upload\\" + "result_a_" + memberId + ".json";
		*/
		
		// file 미존재시
		File questionFile = new File(questionFilePath);
	    if (!questionFile.exists()) {
	        return new ResponseEntity<>("fail", HttpStatus.NOT_FOUND);
	    }
		
		// -- 답변 내용을 JSON에 저장 후 다른 디렉토리에 파일 이동
	    // ObjectMapper로 JSON 읽기
	    ObjectMapper objectMapper = new ObjectMapper();
	    Map<String, Object> jsonMap = objectMapper.readValue(questionFile, Map.class);
	    
	    Map<String, Object> answerMap = (Map<String, Object>) jsonMap.get("answer");
	    List<Map<String, Object>> intentList = (List<Map<String, Object>>) answerMap.get("user_answer");
	    
	    if (intentList != null && !intentList.isEmpty()) {
	        intentList.get(0).put("text", answer);
	    }

	    File answerFile = new File(answerFilePath);
	    objectMapper.writerWithDefaultPrettyPrinter().writeValue(answerFile, jsonMap);
	    
	    // 저장 후 파일 권한 777로 설정 (rwxrwxrwx)
        Path path = answerFile.toPath();
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
        Files.setPosixFilePermissions(path, perms);
	    
	    log.info(memberId + "의 answer Json 파일 생성 완료");
		
		// -- 결과 JSON 파일이 생성될 때까지 대기 
	    File resultFile = new File(resultFilePath);
	    int waitTime = 0;
		while (!resultFile.exists() && waitTime < 60000) { // 최대 60초 대기
			Thread.sleep(1000);
			waitTime += 1000;
		}
		
		// 생성되지 않았을 때
		if (!resultFile.exists()) {
			return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// -- JSON 파일을 읽어와서 결과 내용을 클라이언트에 전달
		// JSON 전체 로드
		Map<String, Object> jsonMap_rsl = objectMapper.readValue(resultFile, Map.class);

		// 1. intent -> category
		List<Map<String, Object>> intentList2 = (List<Map<String, Object>>) ((Map<String, Object>) jsonMap_rsl.get("answer")).get("intent");
		String intent = (String) intentList2.get(0).get("category");

		// 2. emotion -> category
		List<Map<String, Object>> emotionList = (List<Map<String, Object>>) ((Map<String, Object>) jsonMap_rsl.get("answer")).get("emotion");
		String emotion = (String) emotionList.get(0).get("category");

		// 3. summary -> text
		Map<String, Object> summaryMap = (Map<String, Object>) ((Map<String, Object>) jsonMap_rsl.get("answer")).get("summary");
		String suggest = (String) summaryMap.get("text");
		
		log.info(memberId + "의 Result Json 파일 내에 질문 출력 완료");
				
		InterviewDTO interviewDTO = new InterviewDTO();
		interviewDTO.setAnswer(answer);
		interviewDTO.setMemberId(memberId);
		interviewDTO.setIntention(intent);
		interviewDTO.setEmotion(emotion);
		interviewDTO.setSuggest(suggest);
				
		// INTERVIEW 테이블 결과 업데이트
		int result = interviewService.updateRslInterview(interviewDTO);
		
		if(result > 0) {
			questionFile.delete();
			answerFile.delete();
			
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} else {
			// Question JSON 삭제
			resultFile.delete();
			return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/userList")
	public void userListGET() {
		log.info("userListGET()");

	}

} // end InterviewController