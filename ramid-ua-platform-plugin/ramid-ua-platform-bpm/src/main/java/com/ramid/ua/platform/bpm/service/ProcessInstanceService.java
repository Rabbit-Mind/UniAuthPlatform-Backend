package com.ramid.ua.platform.bpm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.ua.platform.bpm.domain.req.ProcessInstancePageReq;
import com.ramid.ua.platform.bpm.domain.resp.ProcessInstanceDetailResp;
import com.ramid.ua.platform.bpm.domain.resp.ProcessInstancePageResp;
import com.ramid.ua.platform.bpm.domain.resp.ProcessTaskCommentResp;
import com.ramid.ua.platform.bpm.domain.resp.RenderFormResp;
import com.ramid.ua.platform.bpm.feign.domain.req.StartInstanceReq;
import com.ramid.ua.platform.bpm.feign.domain.resp.StartInstanceResp;

import java.util.List;

/**
 * 流程实例业务层接口
 *
 * @author Levin
 */
public interface ProcessInstanceService {

    /**
     * 流程实例列表
     *
     * @param req 请求参数
     * @return 查询结果
     */
    IPage<ProcessInstancePageResp> pageList(ProcessInstancePageReq req);

    /**
     * 作废流程实例
     *
     * @param processInstanceId processInstanceId
     */
    void cancel(String processInstanceId);

    /**
     * 激活/挂起流程实例
     *
     * @param instanceId instanceId 流程实例ID
     * @param activate   activate true = 激活
     */
    void suspendOrResumeInstance(Long instanceId, Boolean activate);

    /**
     * 查询流程实例详情
     *
     * @param id id
     * @return 查询结果
     */
    ProcessInstanceDetailResp detail(String id);

    /**
     * 通过流程实例获取表单信息
     *
     * @param id id
     * @return 表单信息和数据
     */
    RenderFormResp renderForm(String id);

    /**
     * 审核信息
     *
     * @param procInstId 流程实例ID
     * @return 审核信息
     */
    List<ProcessTaskCommentResp> approvalInfo(String procInstId);

    /**
     * 启动流程实例
     *
     * @param req req
     */
    StartInstanceResp startProcess(StartInstanceReq req);

}
