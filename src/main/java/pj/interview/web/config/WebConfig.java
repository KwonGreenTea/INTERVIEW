package pj.interview.web.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

// web.xml과 동일
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
	

	// root application context(Root WebApplicationContext)에 적용하는 설정 클래스 지정 메서드
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { RootConfig.class, SecurityConfig.class }; // RootConfig, SecurityConfig 클래스 리턴

	}

	// servlet application context(Servlet WebApplicationContext)에 적용하는 설정 클래스 지정
	// 메서드
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { ServletConfig.class }; // ServletConfig 클래스 리턴
	}

	// Servlet Mapping 메서드
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" }; // 기본 경로 리턴
	}


	
} // end WebConfig
