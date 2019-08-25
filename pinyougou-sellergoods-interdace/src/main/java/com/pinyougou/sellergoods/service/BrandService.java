package com.pinyougou.sellergoods.service;

//品牌接口

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

public interface BrandService {

    public List<TbBrand> findAll();

    //品牌分页
    public PageResult findPage(int pageNum,int pageSize);

    //返回下拉列表
    public List<Map> selectOptionList();

    //增加
    public void add(TbBrand brand);

    //根据id查询
    public TbBrand findOne(Long id);

    //修改
    public void update(TbBrand brand);

    //删除
    public void delete(Long[] ids);

    //品牌分页
    public PageResult findPage(TbBrand brand,int pageNum,int pageSize);


}
