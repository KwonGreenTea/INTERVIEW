package pj.interview.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

@Controller // @Component
// * 표현 계층(Presentation Layer)
// - 클라이언트(JSP 페이지 등)와 service를 연결하는 역할
@RequestMapping(value = "/main") 
@Log4j
public class MainController {
	
	@GetMapping
	public void mainGet() {
		log.info("mainGet()");
	} // end mainGet()

} // end MainController