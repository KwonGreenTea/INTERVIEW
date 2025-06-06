package pj.interview.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import pj.interview.web.domain.Attach;

@Mapper
public interface AttachMapper {
    int insert(Attach attach);
    List<Attach> selectByBoardId(int boardId);
    Attach selectByAttachId(int attachId);
    int insertModify(Attach attach);
    int delete(int boardId);
    List<Attach> selectOldList();
}
