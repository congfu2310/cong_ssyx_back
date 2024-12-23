package com.atguigu.ssyx.acl.utils;

import com.atguigu.ssyx.model.acl.Permission;

import java.util.ArrayList;
import java.util.List;


//通过递归的方式将权限列表转化为具有层次结构的树形结构
public class PermissionHelper {

    public static List<Permission> buildPermission(List<Permission> allList) {
        //创建最终数据封装list集合
        List<Permission> trees = new ArrayList<>();
        //遍历所有菜单 allList集合，得到第一层数据， pid = 0
        for (Permission permission : allList) {
            //如果pid = 0，则为最顶层菜单（全部数据）
            if (permission.getPid() == 0) {
                permission.setLevel(1);
                //递归调用方法，一层一层找
                trees.add(findChildren(permission,allList));
            }
        }
        return trees;
    }


    //这里两个参数 permission是指的上层节点，allList指的是所有菜单
    public static Permission findChildren(Permission permission,List<Permission> allList) {
        permission.setChildren(new ArrayList<>());
        //遍历allList所有菜单数据
        //判断：当前节点id = pid 是否一样，封装，递归往下找
        for (Permission it : allList) {
            //当前节点id = pid是否一样
            if(permission.getId().longValue() == it.getPid().longValue()) {
                int level = permission.getLevel() + 1;
                it.setLevel(level);
                if (permission.getChildren() == null) {
                    permission.setChildren(new ArrayList<>());
                }
                //封装下一层数据
                permission.getChildren().add(findChildren(it,allList));
            }
        }
        return permission;
    }
}
