package pj.interview.web.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = { "pj.interview.web.service", "pj.interview.web.aspect" })
@EnableAspectJAutoProxy
@MapperScan(basePackages = { "pj.interview.web.persistence" })
@EnableTransactionManagement // 트랜잭션 관리 활성화
public class RootConfig {
	
	/* 배포 설정 */
	@Bean
	public DataSource dataSource() { // DataSource 빈 생성 메서드
		System.setProperty("oracle.net.tns_admin", "/home/ubuntu/wallet");
		
		HikariConfig config = new HikariConfig(); // 설정 객체 생성
		config.setDriverClassName("oracle.jdbc.OracleDriver"); // JDBC 드라이버 클래스명 설정
		config.setJdbcUrl("jdbc:oracle:thin:@(description= (retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1521)(host=adb.ap-chuncheon-1.oraclecloud.com))(connect_data=(service_name=g70e91f7150db1c_hyfocus_high.adb.oraclecloud.com))(security=(ssl_server_dn_match=yes)))"); // DB 접속 URL 설정
		
		config.setUsername("admin"); // DB 계정 이름 설정
		config.setPassword("KBSkbs1213!@"); // DB 계정 비밀번호 설정

		config.setMaximumPoolSize(10); // 최대 커넥션 풀 크기 설정
		config.setConnectionTimeout(30000); // 커넥션 타임아웃 설정 (30초)
		HikariDataSource ds = new HikariDataSource(config); // 설정 객체를 사용하여 DataSource 빈 생성
		return ds; // DataSource 빈 반환
	}
	
	
	/* 개발설정 
	@Bean
	public DataSource dataSource() { // DataSource 빈 생성 메서드
		HikariConfig config = new HikariConfig(); // 설정 객체
		config.setDriverClassName("oracle.jdbc.OracleDriver"); // jdbc 드라이버 정보
		config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:xe"); // DB 연결 url
		config.setUsername("test"); // DB 사용자 아이디
		config.setPassword("1234"); // DB 사용자 비밀번호
		
		config.setMaximumPoolSize(10); // 최대 풀(Pool) 크기 설정
		config.setConnectionTimeout(30000); // Connection 타임 아웃 설정(30초)
		HikariDataSource ds = new HikariDataSource(config); // config 객체를 참조하여 DataSource 객체 생성
		return ds; // ds 객체 리턴
	}
	*/

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		return (SqlSessionFactory) sqlSessionFactoryBean.getObject();
	}

	// 트랜잭션 관리 빈을 생성하는 메서드
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
} // end RootConfig