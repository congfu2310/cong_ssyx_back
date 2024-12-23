package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.acl.service.AdminService;
import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.common.utils.MD5;
import com.atguigu.ssyx.model.acl.Admin;
import com.atguigu.ssyx.vo.acl.AdminQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/acl/user")
@Api(tags = "用户管理")
@CrossOrigin //跨域
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    //1. 拿到用户列表
    @ApiOperation("用户列表")
    @GetMapping("{current}/{limit}")
    public Result list(@PathVariable Long current, @PathVariable Long limit, AdminQueryVo adminQueryVo) {
        Page<Admin> pageParam = new Page<Admin>(current, limit);
        IPage<Admin> pageModel = adminService.selectPageUser(pageParam, adminQueryVo);
        return Result.ok(pageModel);
    }

    //2. 根据用户ID查询用户
    @ApiOperation("根据用户ID查询用户")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Admin admin = adminService.getById(id);
        return Result.ok(admin);
    }

    //3. 新增新用户
    @ApiOperation("新增新用户")
    @PostMapping("save")
    public Result save(@RequestBody Admin admin) {
        //获取输入的密码
        String password = admin.getPassword();
        //对输入的密码进行加密 MD5
        String passwordMD5 = MD5.encrypt(password);
        adminService.save(admin);
        return Result.ok(null);
    }

    //4. 修改用户
    @ApiOperation("修改用户")
    @PutMapping("update")
    public Result update(@RequestBody Admin admin) {
        adminService.updateById(admin);
        return Result.ok(admin);
    }

    //5. 根据id删除用户
    @ApiOperation("根据id删除用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        adminService.removeById(id);
        return Result.ok(null);
    }

    //6. 根据ids批量删除用户
    @ApiOperation("根据id列表删除用户")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        adminService.removeByIds(idList);
        return Result.ok(null);
    }


    //7. 获取所有角色，根据用户id查询用户分配角色列表
    @ApiOperation("获取用户角色")
    @GetMapping("toAssign/{adminId}")
    public Result toAssign(@PathVariable Long adminId) {
        //这里map里边是有两个部分的东西，一个是所有角色，另一个是该ID的角色
        Map<String, Object> map = roleService.getRoleByAdminId(adminId);
        return Result.ok(map);
    }


    // 8. 给用户分配角色
    @ApiOperation("根据用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestParam Long adminId, @RequestParam Long[] roleId) {
        roleService.saveAdminRoleRelationShip(adminId, roleId);
        return Result.ok(null);
    }
}








