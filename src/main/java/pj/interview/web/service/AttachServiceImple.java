package pj.interview.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;
import pj.interview.web.domain.Attach;
import pj.interview.web.domain.AttachDTO;
import pj.interview.web.persistence.AttachMapper;

@Service
@Log4j
public class AttachServiceImple implements AttachService {

	@Autowired
    private AttachMapper attachMapper;


    @Override
    public AttachDTO getAttachById(int attachId) {
    	log.info("getAttachById()");
        return toDTO(attachMapper.selectByAttachId(attachId));
    }
    
    // Attach를 AttachDTO로 변환하는 메서드
    private AttachDTO toDTO(Attach attach) {
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setAttachId(attach.getAttachId());
        attachDTO.setBoardId(attach.getBoardId());
        attachDTO.setAttachPath(attach.getAttachPath());
        attachDTO.setAttachRealName(attach.getAttachRealName());
        attachDTO.setAttachChgName(attach.getAttachChgName());
        attachDTO.setAttachExtension(attach.getAttachExtension());
        attachDTO.setAttachDateCreated(attach.getAttachDateCreated());

        return attachDTO;
    }
}
