package com.ramid.ua.platform.suite.online.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ramid.framework.db.mybatisplus.ext.SuperMapper;
import com.ramid.ua.platform.suite.online.domain.entity.OnlineFormData;
import com.ramid.ua.platform.suite.online.domain.req.OnlineFormDesignerPageReq;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineFormDataMapper extends SuperMapper<OnlineFormData> {
    
    IPage<OnlineFormData> pageList(@Param("page") Page<?> page, @Param("req") OnlineFormDesignerPageReq req);
    
}
