package com.ramid.ua.platform.bpm.service;

import com.ramid.framework.db.mybatisplus.ext.SuperService;
import com.ramid.ua.platform.bpm.domain.entity.ProcessCategory;
import com.ramid.ua.platform.bpm.domain.req.ProcessCategorySaveReq;

/**
 * 流程类别(ProcessCategory)业务层接口
 *
 * @author Levin
 */
public interface ProcessCategoryService extends SuperService<ProcessCategory> {

    /**
     * 保存
     *
     * @param vo ${@link ProcessCategorySaveReq} 流程类别保存
     */
    void create(ProcessCategorySaveReq vo);


    /**
     * 通过id更新
     *
     * @param req ${@link ProcessCategorySaveReq} 流程类别更新
     * @param id  ${@link String} 类别id
     */
    void modify(Long id, ProcessCategorySaveReq req);
}
