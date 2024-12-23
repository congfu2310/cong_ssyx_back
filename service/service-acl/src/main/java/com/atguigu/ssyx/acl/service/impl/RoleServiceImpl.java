package com.atguigu.ssyx.acl.service.impl;

import com.atguigu.ssyx.acl.mapper.RoleMapper;
import com.atguigu.ssyx.acl.service.AdminRoleService;
import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.model.acl.AdminRole;
import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private AdminRoleService adminRoleService;

    //1.分页查询获取角色列表
    @Override
    public IPage<Role> selectRolePage(Page<Role> pageParam, RoleQueryVo roleQueryVo) {
        //获取条件值：roleName
        String roleName = roleQueryVo.getRoleName();
        //创建条件构造器对象
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        //在这里判断roleName是否为空
        if (!StringUtils.isEmpty(roleName)) {
            //封装查询条件
            wrapper.like(Role::getRoleName, roleName);
        }
        //调用mapper方法实现条件分页查询
        //这里的baseMapper指的就是RoleMapper接口，因为RoleServiceImpl实现类继承了ServiceImpl<RoleMapper, Role>
        //baseMapper 是 MyBatis-Plus 自动注入的 RoleMapper 实现类的对象
        // 因此，当你调用 baseMapper.selectPage(pageParam, wrapper) 时，
        // 实际上调用的是 RoleMapper 中继承的 BaseMapper 的 selectPage 方法，它会执行分页查询
        IPage<Role> pageModel = baseMapper.selectPage(pageParam, wrapper);
        return pageModel;
    }

    //根据用户获取角色数据和获取所有角色数据
    @Override
    public Map<String, Object> getRoleByAdminId(Long adminId) {
        //1.查询所有角色
        //MyBatis-Plus 查询操作,参数传入了null，意味着没有任何查询条件，执行的是 SELECT * FROM Role;，即查询 Role 表中的所有记录
        List<Role> allRoleList = baseMapper.selectList(null);

        //2.根据用户id查询用户分配的角色列表 --- adminRole表
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        //2.1 根据用户id查询用户角色关系表拿到该用户的角色信息
        wrapper.eq(AdminRole::getAdminId, adminId);
        List<AdminRole> adminRoleList = adminRoleService.list(wrapper);
        //2.2 通过2.1返回的所有角色信息，获取所有角色id (使用是stream流获取)
        List<Long> roleIdsList = adminRoleList.stream().map(item -> item.getRoleId()).collect(Collectors.toList());
        //2.3 创建新的list集合，用于存储用户配置角色
        List<Role> assignRoleList = new ArrayList<>();

        //2.4 遍历所有角色列表allRolesList得到每个角色
        //判断所有角色里面是否包含已经分配角色的id，封装到2.3里面新的list集合
        for (Role role : allRoleList) {
            //判断
            if (roleIdsList.contains(role.getId())) {
                assignRoleList.add(role);
            }
        }
        //封装到map，返回所有角色
        Map<String, Object> result = new HashMap<>();
        result.put("allRolesList", allRoleList);
        result.put("assignRoles", assignRoleList);
        return result;
    }


    //给用户分配角色
    @Override
    public void saveAdminRoleRelationShip(Long adminId, Long[] roleIds) {
        //1.先删除该用户已被分配的角色-根据admin_id删除 admin_role表里对应的数据
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId, adminId);
        adminRoleService.remove(wrapper);
        //2.重新分配
        //遍历多个角色id，拿到每个角色的id，拿着每个角色id和admin_id批次添加进admin_role表中
        List<AdminRole> list = new ArrayList<>();
        for(Long roleId: roleIds){
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            //放到list集合里
            list.add(adminRole);
        }
        adminRoleService.saveBatch(list);
    }
}
