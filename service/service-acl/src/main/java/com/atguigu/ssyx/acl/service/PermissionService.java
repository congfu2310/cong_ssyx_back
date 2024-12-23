package com.atguigu.ssyx.acl.service;

import com.atguigu.ssyx.model.acl.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PermissionService extends IService<Permission>{
    //获取全部菜单
    List<Permission> queryAllPermission();

    // 递归删除菜单
    void removeChildById(Long id);
}
