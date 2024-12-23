package com.atguigu.ssyx.acl.service;

import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;


public interface RoleService extends IService<Role> {

    //1.分页查询获取角色列表
    IPage<Role> selectRolePage(Page<Role> pageParam, RoleQueryVo roleQueryVo);

    //根据用户获取角色数据
    Map<String, Object> getRoleByAdminId(Long adminId);

    //给用户分配角色
    void saveAdminRoleRelationShip(Long adminId, Long[] roleIds);
}
