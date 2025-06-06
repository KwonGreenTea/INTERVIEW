package pj.interview.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.log4j.Log4j;

@ControllerAdvice // controller advice 적용
@Log4j
public class ControllerExceptionHandler {
	
    @ExceptionHandler(Exception.class) // 특정 예외를 처리하는 메서드 정의
    // Exception.class : 모든 예외 적용
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // HTTP 응답 상태 코드 지정
    public String handlerException(Exception ex) {
    	log.info("error message : " + ex.getMessage());
    	return "error-page";
    } // end handlerException()

} // end ControllerExceptionHandler
