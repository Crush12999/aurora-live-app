package com.aurora.live.api.controller;

import com.aurora.live.user.interfaces.IUserRpc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author halo
 * @since 2024/3/23 16:30
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @DubboReference
    private IUserRpc userRpc;

    @GetMapping("/dubbo")
    public String dubbo(){
        return userRpc.test();
    }

}
