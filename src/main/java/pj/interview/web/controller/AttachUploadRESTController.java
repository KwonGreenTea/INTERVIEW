package pj.interview.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;
import pj.interview.web.domain.AttachDTO;
import pj.interview.web.service.AttachService;
import pj.interview.web.util.FileUploadUtil;

@RestController
@RequestMapping(value = "/attach")
@Log4j
public class AttachUploadRESTController {
    @Autowired
    private String uploadPath;
    
    @Autowired
    private AttachService attachService;

    // 첨부 파일 생성 
	@PostMapping
	public ResponseEntity<ArrayList<AttachDTO>> createAttach(MultipartFile[] files) {
		log.info("createAttach");

		ArrayList<AttachDTO> list = new ArrayList<>();

		for (MultipartFile file : files) {

			// UUID 생성
			String chgName = UUID.randomUUID().toString();
			// 파일 저장
			FileUploadUtil.saveFile(uploadPath, file, chgName);

			String path = FileUploadUtil.makeDatePath();
			String extension = FileUploadUtil.subStrExtension(file.getOriginalFilename());

			AttachDTO attachDTO = new AttachDTO();
			// 파일 경로 설정
			attachDTO.setAttachPath(path);
			// 파일 실제 이름 설정
			attachDTO.setAttachRealName(FileUploadUtil.subStrName(file.getOriginalFilename()));
			// 파일 변경 이름(UUID) 설정
			attachDTO.setAttachChgName(chgName);
			// 파일 확장자 설정
			attachDTO.setAttachExtension(extension);

			list.add(attachDTO);
		}

		return new ResponseEntity<ArrayList<AttachDTO>>(list, HttpStatus.OK);
	}
	
    // 첨부 파일 다운로드(GET)
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> download(int attachId) throws IOException {
        log.info("download()");
        
        // attachId로 상세 정보 조회
        AttachDTO attachDTO = attachService.getAttachById(attachId);
        String attachPath = attachDTO.getAttachPath();
        String attachChgName = attachDTO.getAttachChgName();
        String attachExtension = attachDTO.getAttachExtension();
        String attachRealName = attachDTO.getAttachRealName();
        
        // 서버에 저장된 파일 정보 생성
        String resourcePath = uploadPath + File.separator + attachPath + File.separator + attachChgName;
        // 파일 리소스 생성
        Resource resource = new FileSystemResource(resourcePath);
        // 다운로드할 파일 이름을 헤더에 설정
        HttpHeaders headers = new HttpHeaders();
        String attachName = new String(attachRealName.getBytes("UTF-8"), "ISO-8859-1");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + attachName + "." + attachExtension);

        // 파일을 클라이언트로 전송
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    } // end download()

	// 첨부 파일 삭제
    @PostMapping("/delete")
    public ResponseEntity<Integer> deleteAttach(String attachPath, String attachChgName) {
    	log.info("deleteAttach()");
    	FileUploadUtil.deleteFile(uploadPath, attachPath, attachChgName);
    	
    	return new ResponseEntity<Integer>(1, HttpStatus.OK);
    }
    
}
