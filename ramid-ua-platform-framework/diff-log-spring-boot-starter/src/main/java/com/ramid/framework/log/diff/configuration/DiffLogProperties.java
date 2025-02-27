package com.ramid.framework.log.diff.configuration;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Levin
 */
@Data
@ConfigurationProperties(prefix = DiffLogProperties.PREFIX)
public class DiffLogProperties {

    public static final String PREFIX = "extend.boot.log.diff";
    /**
     * 字段名称的替换变量
     */
    private final String FIELD_PLACEHOLDER = "__fieldName";
    /**
     * 更新前的值的替换变量
     */
    private final String SOURCE_VALUE_PLACEHOLDER = "__sourceValue";
    /**
     * 更新后的值的替换变量
     */
    private final String TARGET_VALUE_PLACEHOLDER = "__targetValue";
    /**
     * 列表添加项的值的替换变量
     */
    private final String LIST_ADD_VALUE_PLACEHOLDER = "__addValues";
    /**
     * 列表删除项的替换变量
     */
    private final String LIST_DEL_VALUE_PLACEHOLDER = "__delValues";
    /**
     * 是否检查
     */
    private boolean checkAnnotation = true;
    /**
     * 格式化输出 DIFF 差异结果树
     */
    private boolean prettyValuePrinter = true;
    /**
     * 全局忽略(默认情况下如果没给表单独配置则取全局忽略)
     */
    private List<String> ignoreGlobalFields = Lists.newArrayList("deleted", "createdTime", "createdBy", "createdName", "lastModifiedTime", "lastModifiedBy", "lastModifiedName");
    /**
     * 字段从空改为有值的时候的日志内容模板
     */
    private String addTemplate = "【" + FIELD_PLACEHOLDER + "】从【空】修改为【" + TARGET_VALUE_PLACEHOLDER + "】";
    /**
     * 列表修改后只有添加项的时候的日志内容模板
     */
    private String addTemplateForList = "【" + FIELD_PLACEHOLDER + "】添加了【" + LIST_ADD_VALUE_PLACEHOLDER + "】";
    /**
     * 列表修改后只有删除项的时候的日志内容模板
     */
    private String deleteTemplateForList = "【" + FIELD_PLACEHOLDER + "】删除了【" + LIST_DEL_VALUE_PLACEHOLDER + "】";
    /**
     * 列表修改后既有有删除项又有添加项的时候的日志内容模板
     */
    private String updateTemplateForList = "【" + FIELD_PLACEHOLDER + "】添加了【" + LIST_ADD_VALUE_PLACEHOLDER + "】删除了【" + LIST_DEL_VALUE_PLACEHOLDER + "】";
    /**
     * 字段更新后的日志内容模板
     */
    private String updateTemplate = "【" + FIELD_PLACEHOLDER + "】从【" + SOURCE_VALUE_PLACEHOLDER + "】修改为【" + TARGET_VALUE_PLACEHOLDER + "】";
    /**
     * 字段值被设置为null后的日志内容模板
     */
    private String deleteTemplate = "删除了【" + FIELD_PLACEHOLDER + "】：【" + SOURCE_VALUE_PLACEHOLDER + "】";
    /**
     * 多个字段的日志内容拼接一起的时候的分隔符
     */
    private String fieldSeparator = "；";
    /**
     * 添加或者删除多个列表项的时候，list中多个项直接的分隔符
     */
    private String listItemSeparator = "，";
    /**
     * 当对象存在嵌套对象的时候，比如order里面有个user，user分为创建人和更新人，那么：创建人『的』用户ID，其中『的』就是 ofWord
     */
    private String ofWord = "的";

    /**
     * 是否记录日志
     */
    private Boolean diffLog = false;

    private String useEqualsMethod;

    public void setAddTemplate(String template) {
        validatePlaceHolder(template);
        this.addTemplate = template;
    }

    public void setUpdateTemplate(String template) {
        validatePlaceHolder(template);
        this.updateTemplate = template;
    }

    public void setDeleteTemplate(String template) {
        validatePlaceHolder(template);
        this.deleteTemplate = template;
    }

    private void validatePlaceHolder(String template) {
        if (!template.contains(FIELD_PLACEHOLDER) && !template.contains(SOURCE_VALUE_PLACEHOLDER) && !template.contains(TARGET_VALUE_PLACEHOLDER)) {
            throw new IllegalArgumentException("请检查 diffLog template, 模板需要配置 {{#fieldName}},{{#sourceValue}},{{#targetValue}} 三个变量中的任何一个");
        }
    }

    public String formatAdd(String fieldName, Object targetValue) {
        return addTemplate.replace(FIELD_PLACEHOLDER, fieldName)
                .replace(TARGET_VALUE_PLACEHOLDER, String.valueOf(targetValue));
    }

    public String formatUpdate(String fieldName, Object sourceValue, Object targetValue) {
        return updateTemplate.replace(FIELD_PLACEHOLDER, fieldName)
                .replace(SOURCE_VALUE_PLACEHOLDER, String.valueOf(sourceValue))
                .replace(TARGET_VALUE_PLACEHOLDER, String.valueOf(targetValue));
    }

    public String formatDeleted(String fieldName, Object sourceValue) {
        return deleteTemplate.replace(FIELD_PLACEHOLDER, fieldName)
                .replace(SOURCE_VALUE_PLACEHOLDER, String.valueOf(sourceValue));
    }

    public String formatList(String fieldName, String addContent, String delContent) {
        if (StrUtil.isNotBlank(addContent) && StrUtil.isBlank(delContent)) {
            return addTemplateForList.replace(FIELD_PLACEHOLDER, fieldName).replace(LIST_ADD_VALUE_PLACEHOLDER, addContent);
        }
        if (StrUtil.isBlank(addContent) && StrUtil.isNotBlank(delContent)) {
            return deleteTemplateForList.replace(FIELD_PLACEHOLDER, fieldName).replace(LIST_DEL_VALUE_PLACEHOLDER, delContent);
        }
        if (StrUtil.isNotBlank(addContent) && StrUtil.isNotBlank(delContent)) {
            return updateTemplateForList.replace(FIELD_PLACEHOLDER, fieldName)
                    .replace(LIST_ADD_VALUE_PLACEHOLDER, addContent)
                    .replace(LIST_DEL_VALUE_PLACEHOLDER, delContent);
        }
        return "";
    }

}
