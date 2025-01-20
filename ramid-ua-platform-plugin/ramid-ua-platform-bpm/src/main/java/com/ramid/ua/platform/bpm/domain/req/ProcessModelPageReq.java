
package com.ramid.ua.platform.bpm.domain.req;

import com.ramid.framework.db.mybatisplus.page.PageRequest;
import com.ramid.ua.platform.bpm.feign.domain.enums.ProcessModelStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 流程模型管理分页查询Request
 *
 * @author Levin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProcessModelPageReq")
public class ProcessModelPageReq extends PageRequest {

    @Schema(description = "模型名称")
    private String diagramName;

    @Schema(description = "类别ID")
    private String categoryId;

    @Schema(description = "模型状态:0-未部署,1-已经部署,2-新版本待部署")
    private ProcessModelStatus status;
}
