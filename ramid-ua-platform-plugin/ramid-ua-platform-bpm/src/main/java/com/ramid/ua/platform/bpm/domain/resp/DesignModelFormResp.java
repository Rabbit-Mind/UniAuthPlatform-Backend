package com.ramid.ua.platform.bpm.domain.resp;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Levin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DesignModelFormResp {

    @Schema(defaultValue = "脚本")
    private String script;

    @NotEmpty(message = "表单配置")
    private JSONArray schemas;

    @NotEmpty(message = "模型ID")
    private Long modelId;

}
