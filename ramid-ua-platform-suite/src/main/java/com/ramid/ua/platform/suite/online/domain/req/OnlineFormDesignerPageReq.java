package com.ramid.ua.platform.suite.online.domain.req;

import com.ramid.framework.db.mybatisplus.page.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "OnlineFormDesignerPageReq")
public class OnlineFormDesignerPageReq extends PageRequest {
    
    @Schema(description = "定义KEY")
    private String definitionKey;
    
    @Schema(description = "标题")
    private String title;
    
}
