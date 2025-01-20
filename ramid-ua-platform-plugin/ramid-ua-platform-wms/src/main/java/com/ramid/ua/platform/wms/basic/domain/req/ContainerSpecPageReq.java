package com.ramid.ua.platform.wms.basic.domain.req;

import com.ramid.framework.db.mybatisplus.page.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 容器规格分页查询请求数据
 *
 * @author ddCat
 * @since 2024-06-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ContainerSpecPageReq", description = "容器规格分页查询请求数据")
public class ContainerSpecPageReq extends PageRequest {

    @Schema(description = "容器规格名称")
    private String name;

    @Schema(description = "状态")
    private String status;
}
