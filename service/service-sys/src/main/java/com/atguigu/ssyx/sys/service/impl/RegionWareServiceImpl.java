package com.atguigu.ssyx.sys.service.impl;

import com.atguigu.ssyx.common.exception.SsyxException;
import com.atguigu.ssyx.common.result.ResultCodeEnum;
import com.atguigu.ssyx.model.sys.RegionWare;
import com.atguigu.ssyx.sys.mapper.RegionWareMapper;
import com.atguigu.ssyx.sys.service.RegionWareService;
import com.atguigu.ssyx.vo.sys.RegionWareQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RegionWareServiceImpl extends ServiceImpl<RegionWareMapper, RegionWare> implements RegionWareService {

    @Autowired
    private RegionWareMapper regionWareMapper;


    //开通区域列表
    @Override
    public IPage<RegionWare> selectPageRegionWare(Page<RegionWare> pageParam, RegionWareQueryVo regionWareQueryVo) {

        //1. 获取查询条件值
        String keyword = regionWareQueryVo.getKeyword();
        //2. 判断条件值是否为空，不为空封装条件
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(keyword)) {
            //想要根据区域名称或者仓库名称来查询 所以用like or
            wrapper.like(RegionWare::getRegionName, keyword)
                    .or().like(RegionWare::getWareName, keyword);
        }
        IPage<RegionWare> regionWarePage = baseMapper.selectPage(pageParam, wrapper);
        return regionWarePage;
    }


    //添加开通区域
    @Override
    public void saveRegionWare(RegionWare regionWare) {
        //判断区域是否已开通
        LambdaQueryWrapper<RegionWare> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(RegionWare::getRegionId, regionWare.getRegionId());
        Integer count = regionWareMapper.selectCount(queryWrapper);
        if (count > 0) {//已开通
            throw new SsyxException(ResultCodeEnum.REGION_OPEN);
        }
        baseMapper.insert(regionWare);
    }


    //取消开通区域
    @Override
    public void updateStatus(Long id, Integer status) {
        RegionWare regionWare = baseMapper.selectById(id);
        regionWare.setStatus(status);
        baseMapper.updateById(regionWare);
    }
}
