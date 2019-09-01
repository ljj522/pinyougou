package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {

    //搜索
    public Map search(Map searchMap);

    //导入列表
    public void imporList(List list);

    //删除数据
    public void deleteByGoodsIds(List goodsIds);
}
