package com.ramid.ua.platform.bpm.feign;

import com.ramid.framework.commons.FeignConstants;
import com.ramid.ua.platform.bpm.feign.domain.req.StartInstanceReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Levin
 */
@FeignClient(name = FeignConstants.BPM_FEIGN_NAME, dismiss404 = true)
public interface ProcessServiceFeign {


    @PostMapping("/process-models/start-instance")
    void startProcess(@RequestBody StartInstanceReq req);


}
