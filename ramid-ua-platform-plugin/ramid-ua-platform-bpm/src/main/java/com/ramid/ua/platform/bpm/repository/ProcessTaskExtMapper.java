package com.ramid.ua.platform.bpm.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ramid.framework.db.mybatisplus.ext.SuperMapper;
import com.ramid.ua.platform.bpm.domain.entity.ProcessTaskExt;
import com.ramid.ua.platform.bpm.domain.req.ProcessTaskPageReq;
import com.ramid.ua.platform.bpm.domain.resp.ProcessTaskExtResp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Levin
 */
@Repository
public interface ProcessTaskExtMapper extends SuperMapper<ProcessTaskExt> {

    /**
     * 查看任务列表
     *
     * @param page 分页对象
     * @param req  req
     * @return 分页结果
     */
    Page<ProcessTaskExtResp> pageList(@Param("page") Page<ProcessTaskExt> page, @Param("req") ProcessTaskPageReq req);
}
