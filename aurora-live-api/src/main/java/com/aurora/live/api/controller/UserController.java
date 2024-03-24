package com.aurora.live.api.controller;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.aurora.live.user.interfaces.IUserRpc;
import com.aurora.live.user.model.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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

    @PutMapping("/update-user-info")
    public boolean updateUserInfo(@RequestBody UserDTO userDTO) {
        return userRpc.updateUserInfo(userDTO);
    }

    @PostMapping("/insert-user")
    public boolean insertUser(@RequestBody UserDTO userDTO) {
        return userRpc.insertUser(userDTO);
    }

    @GetMapping("/batch-query-user-info")
    public Map<Long, UserDTO> batchQueryUserInfo(String userIdStr) {
        if (!StringUtils.hasText(userIdStr)) {
            return Maps.newHashMap();
        }
        return userRpc.batchQueryUserInfo(
                Arrays.stream(userIdStr.split(","))
                        .map(Long::valueOf)
                        .collect(Collectors.toList())
        );
    }

}
