package pj.interview.web.task;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;
import pj.interview.web.domain.Attach;
import pj.interview.web.persistence.AttachMapper;

@Component
@Log4j
public class AttachCheckTask {

	@Autowired
	private AttachMapper attachMapper;

	@Autowired
	private String uploadPath;

//	@Scheduled(cron = "0 * * * * *") // 0초마다 실행
//	public void test() {
//		log.warn("========================");
//		log.warn("Test Task run");
//	}

	// 현재 날짜에서 1일 전 파일 정보에 저장되지 않은 파일 목록을 삭제하는 메서드
	@Scheduled(cron = "0 0 3 * * *") // 매일 03:00 마다 실행 
	public void deleteAttachs() {
		log.warn("========================");
		log.warn("Delete Attach Task Run");

		// 현재 날짜에서 1일 전 저장된 파일 정보 조회
		List<Attach> attachList = attachMapper.selectOldList();
		
		// 파일 정보가 없는 경우 종료
		if (attachList.size() == 0) {
			return;
		}
		
		// 파일 정보에서 파일 이름만 추출하여 List<String>으로 변경
		List<String> savedList = attachList.stream() // 데이터 처리 기능 사용을 위한 stream 변경
				.map(this::toChgName) // attach를 attach.getAttachChgName()으로 변경 
				.collect(Collectors.toList()); // stream을 list로 변경
		
		// 파일 정보 중 이미지 파일인 경우 섬네일 이름을 생성하여 savedList에 추가 
		attachList.stream()
			.filter(this::isImage) // 이미지 파일만 필터링
			.map(this::toThumbnailName) // 섬네일 이름으로 변경
			.forEach(name -> savedList.add(name)); // savedList에 각 섬네일 이름 저장
		log.warn(savedList);

		// 현재 날짜에서 1일 전 업로드 폴더 경로 생성
		File targetDir = Paths.get(uploadPath, attachList.get(0).getAttachPath()).toFile();
		
		// 업로드 폴더에 저장된 파일 목록 중 
		// savedList에 파일 이름이 없는 경우만 조회
		File[] removeFiles = targetDir.listFiles(file -> savedList.contains(file.getName()) == false);

		for (File file : removeFiles) {
			log.warn(file.getAbsolutePath());
			file.delete(); // 파일 삭제
		}

	}

	// attach를 전달받아 파일 이름 리턴
	private String toChgName(Attach attach) {
		return attach.getAttachChgName();
	}
	
	// attach를 전달받아 파일 이름을 썸네일 파일 이름으로 변경
	private String toThumbnailName(Attach attach) {
		return "t_" + attach.getAttachChgName() + "." + attach.getAttachExtension();
	}
	
	// 확장자로 이미지 파일 확인
	private boolean isImage(Attach attach) {
		if(attach.getAttachExtension().equals("jpg") || 
				attach.getAttachExtension().equals("png") || 
				attach.getAttachExtension().equals("jpeg") || 
				attach.getAttachExtension().equals("gif")) {
			return true;
		} else {
			return false;
		}
	}

}
