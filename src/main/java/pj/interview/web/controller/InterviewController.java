package pj.interview.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

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
			model.addAttribute("auth", "해당 사용자의 경력이 기입되어있지 않습니다.");
		} else if (memberDTO.getGender() == null) { // 성별 미기입
			model.addAttribute("auth", "해당 사용자의 성별이 기입되어있지 않습니다.");
		} else if (memberDTO.getSector() == null) { // 직군 미기입
			model.addAttribute("auth", "해당 사용자의 직군이 기입되어있지 않습니다.");
		} else { 
			model.addAttribute("auth", "1");
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
		String userFilePath = "C:\\upload\\" + memberId + ".json";
		String questionFilePath = "C:\\upload\\" + "q_" + memberId + ".json";
		String answerFilePath = "C:\\upload\\" + "a_" + memberId + ".json";
		String resultFilePath = "C:\\upload\\" + "result_a_" + memberId + ".json";
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
		while (!questionFile.exists() && waitTime < 30000) { // 최대 60초 대기
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
		
		InterviewDTO interviewDTO = new InterviewDTO();
		interviewDTO.setMemberId(memberId);
		interviewDTO.setQuestion(question);
		
		// INTERVIEW 테이블 질문 생성
		int result = interviewService.createInterview(interviewDTO);
		
		// User JSON 삭제
		userFile.delete();
					
		if(result > 0) {
			return new ResponseEntity<String>(question, HttpStatus.OK);
		} else {
			questionFile.delete();
			return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/answer", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> answerGET(Model model, String answer, @AuthenticationPrincipal UserDetails userDetails)
			throws IOException, InterruptedException {
		log.info("answerGET()");
		
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("error", "fail");
		
		// User ID 불러옴
		String memberId = userDetails.getUsername();

		/* 배포 */
		String questionFilePath = "/home/ubuntu/question/" + "q_" + memberId + ".json";
		String answerFilePath = "/home/ubuntu/answer/" + "a_" + memberId + ".json";
		String resultFilePath = "/home/ubuntu/result/" + "result_a_" + memberId + ".json";
		
		/* 개발
		String questionFilePath = "C:\\upload\\" + "q_" + memberId + ".json";
		String answerFilePath = "C:\\upload\\" + "a_" + memberId + ".json";
		String resultFilePath = "C:\\upload\\" + "result_a_" + memberId + ".json";
		*/
		
		File questionFile = new File(questionFilePath);
		File answerFile = new File(answerFilePath);
		
		// file 미존재시
	    if (!questionFile.exists()) {
	        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	    }
	    
	    
	    // ObjectMapper 준비
	    ObjectMapper objectMapper = new ObjectMapper();

	    // JSON 파일 로드
	    Map<String, Object> q_jsonMap = objectMapper.readValue(questionFile, Map.class);
	    
	    // user_answer 추가
	    List<Map<String, Object>> userAnswerList = new ArrayList<>();
	    Map<String, Object> userAnswer = new HashMap<>();
	    userAnswer.put("text", answer); // 답변 내용 추가
	    userAnswerList.add(userAnswer);

	    q_jsonMap.put("user_answer", userAnswerList);

	    // 저장
	    objectMapper.writerWithDefaultPrettyPrinter().writeValue(answerFile, q_jsonMap);
	    
	 
	    /*
	    // Question JSON 로드
	    ObjectMapper objectMapper = new ObjectMapper();
	    Map<String, Object> q_jsonMap = objectMapper.readValue(questionFile, Map.class);
	    
	    
	    // 제안 답변 가져옴
	 	Map<String, Object> summaryMap = (Map<String, Object>) ((Map<String, Object>) q_jsonMap.get("answer")).get("summary");
	 	String suggest = (String) summaryMap.get("text");
	 	
	 	log.info(memberId + "의 question Json 파일에서 제안 가져옴");
		
		
		
		// -- 답변 내용을 Answer JSON에 저장 후 result 디렉토리로 파일 이동
	 	Map<String, Object> a_jsonMap = objectMapper.readValue(questionFile, Map.class);
	 	
	    Map<String, Object> answerMap = (Map<String, Object>) a_jsonMap.get("answer");
	    List<Map<String, Object>> intentList = (List<Map<String, Object>>) answerMap.get("user_answer");
	    if (intentList != null && !intentList.isEmpty()) {
	        intentList.get(0).put("text", answer);
	    }
	    */

	    log.info(memberId + "의 answer Json에 답변 생성");
	    
	    // 저장 후 파일 권한 777로 설정 (rwxrwxrwx)
        Path path = answerFile.toPath();
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
        Files.setPosixFilePermissions(path, perms);
	    
	    log.info(memberId + "의 answer Json 파일 생성 완료");
		
		// -- 결과 JSON 파일이 생성될 때까지 대기 
	    File resultFile = new File(resultFilePath);
	    int waitTime = 0;
		while (!resultFile.exists() && waitTime < 100000) { // 최대 100초 대기
			Thread.sleep(1000);
			waitTime += 1000;
		}
		
		// 생성되지 않았을 때
		if (!resultFile.exists()) {
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// -- JSON 파일을 읽어와서 model을 통해 클라이언트에 전달
		Map<String, Object> rsl_jsonMap = objectMapper.readValue(resultFile, Map.class);

        // 추천 답변 추출
        String suggest = (String) rsl_jsonMap.get("recommended_answer");

        // 1. grade 추출
        String grade = (String) rsl_jsonMap.get("grade");

        // 2. feedback 리스트 추출
        List<String> feedbackList = (List<String>) rsl_jsonMap.get("feedback");
        
        // 3. analysis 객체 가져오기
        Map<String, Object> analysisMap = (Map<String, Object>) rsl_jsonMap.get("analysis");

        String emotion = (String) analysisMap.get("pred_emotion_kor");
        String intention = (String) analysisMap.get("pred_intent_kor");
		
		log.info(memberId + "의 Result Json 파일 내에 질문 출력 완료");
				
		InterviewDTO interviewDTO = new InterviewDTO();
		interviewDTO.setAnswer(answer);
		interviewDTO.setMemberId(memberId);
		interviewDTO.setSuggest(suggest);
		interviewDTO.setIntention(intention);
		interviewDTO.setEmotion(emotion);
		interviewDTO.setGrade(grade);
		
		// INTERVIEW 테이블 결과 업데이트
		int result = interviewService.updateRslInterview(interviewDTO);
		
		if(result > 0) {
			questionFile.delete();
			answerFile.delete();
			
			Map<String, Object> response = new HashMap<>();
			response.put("suggest", suggest);
			response.put("intention", intention);
			response.put("emotion", emotion);
			response.put("grade", grade);
			response.put("feedbackList", feedbackList);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			// result JSON 삭제
			resultFile.delete();
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/userList")
	public void userListGET() {
		log.info("userListGET()");

	}

} // end InterviewController