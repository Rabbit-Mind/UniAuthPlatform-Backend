package com.ramid.ua.platform.bpm.repository;

import com.ramid.framework.db.mybatisplus.ext.SuperMapper;
import com.ramid.ua.platform.bpm.domain.entity.ProcessInstanceExt;
import org.springframework.stereotype.Repository;

/**
 * @author Levin
 */
@Repository
public interface ProcessInstanceExtMapper extends SuperMapper<ProcessInstanceExt> {
}
