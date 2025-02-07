package com.ramid.ua.platform.bpm.domain.req;

import com.alibaba.fastjson2.JSONArray;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Levin
 */
@Data
@Schema(name = "FormDesignSaveReq")
public class FormDesignSaveReq {

    @Schema(defaultValue = "脚本")
    private String script;

    @NotEmpty(message = "表单配置")
    private JSONArray schemas;
}
