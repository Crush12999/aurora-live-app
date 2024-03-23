package com.aurora.live.api.controller;

import com.aurora.live.user.interfaces.IUserRpc;
import com.aurora.live.user.model.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController
 *
 * @author halo
 * @since 2024/3/23 16:30
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @DubboReference
    private IUserRpc userRpc;

    @GetMapping("/get-user-info")
    public UserDTO getUserInfo(Long userId){
        return userRpc.selectOneByUserId(userId);
    }

}