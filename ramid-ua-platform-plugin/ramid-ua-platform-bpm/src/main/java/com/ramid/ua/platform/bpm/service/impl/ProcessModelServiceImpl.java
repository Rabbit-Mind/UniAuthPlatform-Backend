

package com.ramid.ua.platform.bpm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.framework.commons.exception.CheckedException;
import com.ramid.framework.commons.security.AuthenticationContext;
import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.bpm.domain.entity.ProcessCategory;
import com.ramid.ua.platform.bpm.domain.entity.ProcessDeployHistory;
import com.ramid.ua.platform.bpm.domain.entity.ProcessModel;
import com.ramid.ua.platform.bpm.domain.entity.ProcessModelForm;
import com.ramid.ua.platform.bpm.domain.req.*;
import com.ramid.ua.platform.bpm.domain.resp.*;
import com.ramid.ua.platform.bpm.feign.domain.enums.ProcessModelStatus;
import com.ramid.ua.platform.bpm.repository.ProcessCategoryMapper;
import com.ramid.ua.platform.bpm.repository.ProcessDeployHistoryMapper;
import com.ramid.ua.platform.bpm.repository.ProcessModelFormMapper;
import com.ramid.ua.platform.bpm.repository.ProcessModelMapper;
import com.ramid.ua.platform.bpm.service.ProcessIdentityService;
import com.ramid.ua.platform.bpm.service.ProcessModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 流程模型管理(DesignModel)业务层实现
 *
 * @author Levin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessModelServiceImpl extends SuperServiceImpl<ProcessModelMapper, ProcessModel> implements ProcessModelService {

    private final RepositoryService repositoryService;
    private final ProcessCategoryMapper processCategoryMapper;
    private final ProcessDeployHistoryMapper processDeployHistoryMapper;
    private final ProcessModelFormMapper processModelFormMapper;
    private final RuntimeService runtimeService;
    private final AuthenticationContext context;
    private final ProcessIdentityService processIdentityService;

    @Override
    public IPage<ProcessModelPageResp> pageList(ProcessModelPageReq req) {
        return baseMapper.selectPage(req.buildPage(), Wraps.<ProcessModel>lbQ()
                        .eq(ProcessModel::getStatus, req.getStatus())
                        .eq(ProcessModel::getDiagramName, req.getDiagramName())
                        .eq(ProcessModel::getCategoryCode, req.getCategoryId()))
                .convert(x -> BeanUtil.toBean(x, ProcessModelPageResp.class));
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public void create(DesignModelSaveReq req) {
        ProcessCategory category = Optional.ofNullable(this.processCategoryMapper.selectById(req.getCategoryId())).orElseThrow(() -> CheckedException.notFound("流程类型不存在"));
        final Long count = this.baseMapper.selectCount(Wraps.<ProcessModel>lbQ().eq(ProcessModel::getDefinitionKey, req.getDefinitionKey())
                .eq(ProcessModel::getTenantId, context.tenantId()));
        if (count != null && count > 0) {
            throw CheckedException.badRequest("bpm.design.duplicate-process-key");
        }
        var bean = BeanUtil.toBean(req, ProcessModel.class);
        bean.setTenantId(context.tenantId());
        bean.setCategoryCode(category.getCode());
        bean.setCategoryName(category.getName());
        bean.setDiagramIcon(req.getDiagramIcon());
        bean.setDiagramData(bean.getDiagramData());
        this.baseMapper.insert(bean);
    }

    @Override
    @DSTransactional(rollbackFor = {Exception.class, Error.class})
    public void modify(Long id, DesignModelSaveReq req) {
        ProcessCategory category = Optional.ofNullable(this.processCategoryMapper.selectById(req.getCategoryId())).orElseThrow(() -> CheckedException.notFound("流程类型不存在"));
        final Long count = this.baseMapper.selectCount(Wraps.<ProcessModel>lbQ().ne(ProcessModel::getId, id)
                .eq(ProcessModel::getDefinitionKey, req.getDefinitionKey())
                .eq(ProcessModel::getTenantId, context.tenantId()));
        if (count != null && count > 0) {
            throw CheckedException.badRequest("bpm.design.duplicate-process-key");
        }
        ProcessModel record = BeanUtil.toBean(req, ProcessModel.class);
        record.setId(id);
        record.setStatus(ProcessModelStatus.NEW_VERSION_TO_DEPLOY);
        record.setCategoryCode(category.getCode());
        record.setCategoryName(category.getName());
        record.setDiagramData(record.getDiagramData());
        this.baseMapper.updateById(record);
    }

    @Override
    public ProcessModelDetailResp detail(Long modelId) {
        ProcessModel processModel = Optional.ofNullable(this.baseMapper.selectById(modelId)).orElseThrow(() -> CheckedException.badRequest("bpm.design.not-exists"));
        return BeanUtil.toBean(processModel, ProcessModelDetailResp.class);
    }


    @Override
    @DSTransactional(rollbackFor = {Exception.class, Error.class})
    public void deleteByModel(Long modelId, DesignModelDeleteReq req) throws RuntimeException {
        final ProcessModel model = Optional.ofNullable(this.baseMapper.selectById(modelId))
                .orElseThrow(() -> CheckedException.notFound("bpm.design.not-exists"));
        // 删除模型部署数据
        deleteDeployment(model.getDeployId(), req);
        // 删除自存历史部署信息以及引擎部署信息
        final List<ProcessDeployHistory> historyList = processDeployHistoryMapper.selectList(ProcessDeployHistory::getModelId, modelId);
        if (CollUtil.isNotEmpty(historyList)) {
            historyList.stream().map(ProcessDeployHistory::getDeploymentId).distinct().forEach(deploymentId -> deleteDeployment(deploymentId, req));
        }
        this.processDeployHistoryMapper.physicalDeleteByModelId(modelId);
        this.baseMapper.deleteById(modelId);
    }

    private void deleteDeployment(String deploymentId, DesignModelDeleteReq req) {
        if (StringUtils.isBlank(deploymentId)) {
            return;
        }
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
        if (deployment == null) {
            return;
        }
        repositoryService.deleteDeployment(deployment.getId(), req.getCascade(), req.getSkipCustomListeners(), req.getSkipIoMappings());
    }


    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public void deployById(Long id) {
        processIdentityService.execute(() -> {
            var tenantId = context.tenantId();
            var processModel = Optional.ofNullable(baseMapper.selectById(id)).orElseThrow(() -> CheckedException.badRequest("bpm.design.not-exists"));
            var deployment = repositoryService.createDeployment().tenantId(String.valueOf(tenantId))
                    .addString(processModel.getDiagramName() + ".bpmn20.xml", processModel.getDiagramData())
                    .name(processModel.getDiagramName()).deploy();
            var processCategory = Optional.ofNullable(this.processCategoryMapper.selectById(processModel.getCategoryId())).orElseThrow(() -> CheckedException.notFound("模型部署失败,流程类目不存在"));
            var definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
            this.updateById(ProcessModel.builder().id(id).status(ProcessModelStatus.DEPLOYED)
                    .definitionId(definition.getId()).definitionKey(definition.getKey())
                    .deployId(deployment.getId()).deployName(deployment.getName())
                    .deployTime(deployment.getDeploymentTime().toInstant())
                    .version(definition.getVersion())
                    .build());
            // 添加流程信息到历史表
            final ProcessDeployHistory deploymentHistory = ProcessDeployHistory.builder().modelId(id).remark(processModel.getRemark())
                    .diagramData(processModel.getDiagramData()).diagramNames(processModel.getDiagramName())
                    // 既然是历史表应该冗余租户相关内容
                    .tenantId(tenantId).tenantCode(context.tenantCode())
                    .tenantName(context.tenantCode())
                    .deploymentId(deployment.getId()).deploymentName(deployment.getName()).deploymentTime(deployment.getDeploymentTime().toInstant())
                    .processCategoryId(processCategory.getId()).processCategoryCode(processCategory.getCode()).processCategoryName(processCategory.getName())
                    .processDefinitionId(definition.getId()).processDefinitionKey(definition.getKey())
                    .version(definition.getVersion()).build();
            this.processDeployHistoryMapper.insert(deploymentHistory);
            return null;
        });
    }


    @Override
    public void saveFormDesign(Long id, FormDesignSaveReq req) {
        Optional.ofNullable(baseMapper.selectById(id)).orElseThrow(() -> CheckedException.badRequest("bpm.design.not-exists"));
        final ProcessModelForm modelForm = this.processModelFormMapper.selectOne(Wraps.<ProcessModelForm>lbQ().eq(ProcessModelForm::getModelId, id));
        if (modelForm == null) {
            this.processModelFormMapper.insert(ProcessModelForm.builder().modelId(id)
                    .formSchemas(req.getSchemas().toJSONString())
                    .formScript(req.getScript()).build());
        } else {
            this.processModelFormMapper.updateById(ProcessModelForm.builder()
                    .id(modelForm.getId()).modelId(id)
                    .formSchemas(JSON.toJSONString(req.getSchemas()))
                    .formScript(req.getScript()).build());
        }
    }

    @Override
    public DesignModelFormResp findFormDesign(Long id) {
        Optional.ofNullable(baseMapper.selectById(id)).orElseThrow(() -> CheckedException.badRequest("bpm.design.not-exists"));
        final ProcessModelForm modelForm = this.processModelFormMapper.selectOne(Wraps.<ProcessModelForm>lbQ().eq(ProcessModelForm::getModelId, id));
        if (modelForm == null) {
            return null;
        }
        return DesignModelFormResp.builder().modelId(modelForm.getModelId())
                .schemas(JSONArray.parseArray(modelForm.getFormSchemas()))
                .script(modelForm.getFormScript())
                .build();
    }

    @Override
    public List<DesignModelGroupListResp> groupList() {
        var list = this.processCategoryMapper.selectList(ProcessCategory::getStatus, 1);
        if (CollUtil.isEmpty(list)) {
            return List.of();
        }
        final List<ProcessModel> modelList = this.baseMapper.selectList(Wraps.<ProcessModel>lbQ().eq(ProcessModel::getStatus, ProcessModelStatus.DEPLOYED));
        final Map<Long, List<ProcessModel>> map = modelList.stream().collect(Collectors.groupingBy(ProcessModel::getCategoryId));
        return list.stream().map(category -> {
            final List<DesignModelListResp> designModelList = Optional.ofNullable(map.get(category.getId())).orElseGet(List::of)
                    .stream()
                    .map(x -> DesignModelListResp.builder()
                            .id(x.getId()).definitionId(x.getDefinitionId()).diagramName(x.getDiagramName())
                            .diagramIcon(x.getDiagramIcon())
                            .build())
                    .toList();
            return DesignModelGroupListResp.builder().categoryId(category.getId()).categoryName(category.getName()).modelList(designModelList).build();
        }).toList();
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public void startInstance(Long id, InstanceStartReq req) {
        ProcessModel processModel = Optional.ofNullable(this.baseMapper.selectById(id)).orElseThrow(() -> CheckedException.notFound("模型不存在"));
        if (processModel.getStatus() != ProcessModelStatus.DEPLOYED) {
            throw CheckedException.badRequest("模型未部署");
        }
        // 设置流程参数
        JSONObject variables = new JSONObject(req.getFormData());
        variables.put("ext.processInstName", req.getInstanceName());
        variables.put("ext.businessKey", req.getBusinessKey());
        variables.put("ext.businessGroup", req.getBusinessGroup());
        variables.put("ext.formData", req.getFormData());
        long startTime = System.currentTimeMillis();
        runtimeService.createProcessInstanceByKey(processModel.getDefinitionKey())
                .processDefinitionTenantId(context.tenantId().toString())
                .businessKey(StrUtil.blankToDefault(IdUtil.fastSimpleUUID(), req.getBusinessKey()))
                .setVariables(variables).execute();
        log.debug("start instance 耗时 => {}", (System.currentTimeMillis() - startTime));
    }

}