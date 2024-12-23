package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Api(tags = "角色接口")
@RestController
@RequestMapping("/admin/acl/role")
@CrossOrigin
public class RoleController {
    @Autowired
    private RoleService roleService;

    //1.分页查询获取角色列表
    @ApiOperation("获取角色条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageList(@PathVariable Long page, @PathVariable Long limit, RoleQueryVo roleQueryVo) {
        ///创建分页查询对象，用于传递查询条件-当前页、条数
        Page<Role> pageParam = new Page<>(page, limit);
        //调用方法实现条件查询，返回分页对象
        IPage<Role> pageModel = roleService.selectRolePage(pageParam, roleQueryVo);
        return Result.ok(pageModel);
    }


    //2.根据ID获取角色信息
    @ApiOperation(value = "获取角色")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return Result.ok(role);
    }

    //3.新增角色
    @ApiOperation(value = "新增角色")
    @PostMapping("save")
    public Result save(@RequestBody Role role) {
        boolean is_save = roleService.save(role);
        if (is_save) {
            return Result.ok(null);
        }
        {
            return Result.fail(null);
        }
    }

    //4.修改角色
    @ApiOperation("修改角色")
    @PutMapping("update")
    public Result update(@RequestBody Role role) {
        roleService.updateById(role);
        return Result.ok(null);
    }

    //5.根据ID删除角色
    @ApiOperation("根据ID删除角色")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        roleService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id列表删除角色")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids) {
        roleService.removeByIds(ids);
        return Result.ok(null);
    }
}

