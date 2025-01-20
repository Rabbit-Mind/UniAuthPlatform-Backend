package com.ramid.ua.platform.bpm.repository;

import com.ramid.framework.db.mybatisplus.ext.SuperMapper;
import com.ramid.ua.platform.bpm.domain.entity.ProcessTaskComment;
import org.springframework.stereotype.Repository;

/**
 * @author levin
 */
@Repository
public interface ProcessTaskCommentMapper extends SuperMapper<ProcessTaskComment> {
}
