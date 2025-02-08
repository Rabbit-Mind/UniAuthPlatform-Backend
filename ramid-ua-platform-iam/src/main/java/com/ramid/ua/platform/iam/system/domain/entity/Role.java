package com.ramid.ua.platform.iam.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ramid.framework.commons.entity.Entity;
import com.ramid.framework.commons.security.DataScopeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role")
@EqualsAndHashCode(callSuper = true)
public class Role extends Entity<Long> {
    
    @Schema(description = "租户ID")
    private Long tenantId;
    
    @Schema(description = "角色名称")
    private String name;
    
    @Schema(description = "角色编码")
    private String code;
    
    @Schema(description = "超级角色")
    @TableField("super")
    private Boolean superRole;
    
    @Schema(description = "租户描述")
    private String description;
    
    @Schema(description = "是否只读")
    private Boolean readonly;
    
    @Schema(description = "状态(true=启用;false=禁用)")
    private Boolean status;
    
    @Schema(description = "权限类型")
    private DataScopeType scopeType;
}
