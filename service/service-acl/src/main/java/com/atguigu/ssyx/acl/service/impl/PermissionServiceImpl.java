package com.atguigu.ssyx.acl.service.impl;

import com.atguigu.ssyx.acl.mapper.PermissionMapper;
import com.atguigu.ssyx.acl.service.PermissionService;
import com.atguigu.ssyx.acl.utils.PermissionHelper;
import com.atguigu.ssyx.model.acl.Permission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
        implements PermissionService {


    //获取全部菜单
    @Override
    public List<Permission> queryAllPermission() {
        //1.查询出所有的菜单
        List<Permission> allPermissionList = baseMapper.selectList(null);
        //2.转换要求数据格式--把权限数据构建成树形结构数据
        List<Permission> result = PermissionHelper.buildPermission(allPermissionList);
        return result;
    }

    //递归删除菜单
    @Override
    public void removeChildById(Long id) {
        //先创建一个空的 idList，用来存放所有需要删除的菜单ID
        List<Long> idList = new ArrayList<>();
        //获取指定菜单下的所有子菜单ID，并将这些ID添加到 idList 中
        this.getAllPermissionId(id, idList);
        idList.add(id);
        //调用方法批量删除
        baseMapper.deleteBatchIds(idList);
    }

    //找当前菜单ID下的所有子菜单ID
    private void getAllPermissionId(Long id, List<Long> idList) {
        //根据当前菜单id查询下面子菜单列表
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getPid, id);//where id = pid
        List<Permission> childList = baseMapper.selectList(wrapper);
        //递归查询是否还有子菜单，有的话就递归查询
        childList.stream().forEach(item -> {
            idList.add(item.getId());
            //对于每个子菜单，如果它有子菜单，递归调用 getAllPermissionId 方法
            this.getAllPermissionId(item.getId(), idList);
        });
    }
}
