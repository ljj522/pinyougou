package com.pinyougou.search.service.impl;



import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    //搜索
    @Override
    public Map search(Map searchMap) {
        Map map=new HashMap();

        String keywords = (String) searchMap.get("keywords");
        searchMap.put("keywords",keywords.replace(" ",""));//去掉空格

/*        Query query = new SimpleQuery("*:*");
        //添加查询条件
        Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);*/

        //1.查询列表
        map.putAll(searchList(searchMap));
        //2.分组查询商品分类列表
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList",categoryList);
        //查询品牌与规格列表
        String category = (String) searchMap.get("category");
        if (!category.equals("")){
            map.putAll(searchBrandAndSpecList(category));
        }else {
            if (categoryList.size()>0){
                map.putAll(searchBrandAndSpecList(categoryList.get(0)));
            }
        }

        return map;
    }

    //导入列表
    @Override
    public void imporList(List list) {

        solrTemplate.saveBeans(list);
        solrTemplate.commit();

    }

    //删除数据
    @Override
    public void deleteByGoodsIds(List goodsIds) {
        System.out.println("删除商品ID"+goodsIds);

        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_goodsid").in(goodsIds);
        query.addCriteria(criteria);

        solrTemplate.delete(query);
        solrTemplate.commit();

    }

    //查询列表
    private Map searchList(Map searchMap){

        Map map=new HashMap();

        //高亮显示初始化
        HighlightQuery query = new SimpleHighlightQuery();
        //高亮选项
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");//高亮域
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        highlightOptions.setSimplePostfix("</em>");
        query.setHighlightOptions(highlightOptions);//为查询对象设置亮度选项
        //1.1添加查询条件
        Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //1.2搜索商品分类过滤
        if (!"".equals(searchMap.get("category"))){
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        //1.3按品牌过滤
        if (!"".equals(searchMap.get("brand"))){
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        //1.4按规格过滤
        if (searchMap.get("spec")!=null){
            Map<String,String> specMap= (Map<String,String>) searchMap.get("spec");
            for (String key : specMap.keySet()){
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_spec_"+key).is(specMap.get(key));
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //1.5按价格过滤
        if (!"".equals(searchMap.get("price"))){
            String[] price = ((String) searchMap.get("price")).split("-");
            if (!price[0].equals("0")){//如果最低价不等于0
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
            if (!price[1].equals("*")){ //如果最低价格不等于0
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").lessThanEqual(price[1]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //1.6分页
        Integer pageNo = (Integer) searchMap.get("pageNo");//获取页码
        if (pageNo==null){
            pageNo=1;
        }
        Integer pageSize = (Integer) searchMap.get("pageSize");//获取页大小
        if (pageSize==null){
            pageSize=20;
        }
        query.setOffset((pageNo-1)*pageSize);//从第几条记录查询
        query.setRows(pageSize);

        //1.7排序
        String sortValue = (String) searchMap.get("sort");//ASC DESC
        String sortField = (String) searchMap.get("sortField");//排序字段
        if (sortValue!=null&&!sortValue.equals("")){
            if (sortValue.equals("ASC")){
                Sort sort=new Sort(Sort.Direction.ASC,"item_"+sortField);
                query.addSort(sort);
            }
            if (sortValue.equals("DESC")){
                Sort sort=new Sort(Sort.Direction.DESC,"item_"+sortField);
                query.addSort(sort);
            }
        }




        //*********获取高亮结构集**************
        //高亮页对象
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //高亮入口集合(每条记录的高亮入口)
        List<HighlightEntry<TbItem>> entryList = page.getHighlighted();
        for (HighlightEntry<TbItem> entry:entryList){
            //高亮列表(高亮域的个数)
            List<HighlightEntry.Highlight> highlightList = entry.getHighlights();
/*            for (HighlightEntry.Highlight h:highlightList){
                List<String> sns = h.getSnipplets();//每个域有可能存储多值
                System.out.println(sns);
            }*/

            if (highlightList.size()>0 && highlightList.get(0).getSnipplets().size()>0){
                TbItem item = entry.getEntity();
                item.setTitle(highlightList.get(0).getSnipplets().get(0));
            }
        }
        map.put("rows",page.getContent());
        map.put("totalPages", page.getTotalPages());//返回总页数
        map.put("total", page.getTotalElements());//返回总记录数
        return map;

    }

    //分组查询
    private List<String> searchCategoryList(Map searchMap){
        List<String> list = new ArrayList();

        Query query = new SimpleQuery("*:*");
        //添加查询条件where
        Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //设置分组选项
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");//group by
        query.setGroupOptions(groupOptions);
        //获取分组页
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        //获取分组结果对象
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
        //获取分组入口页
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
        //获取分组入口集合
        List<GroupEntry<TbItem>> entryList = groupEntries.getContent();

        for (GroupEntry<TbItem> entry:entryList){
             list.add(entry.getGroupValue());
        }
        return list;
    }

    @Autowired
    private RedisTemplate redisTemplate;

    //根据商品分类名称查询品牌与规格列表
    private Map searchBrandAndSpecList(String category){
        Map map = new HashMap();
        //1.根据商品分类名称得到模板ID
        Long templateId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if (templateId!=null){
            //2.根据模板ID获取品牌列表
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(templateId);
            map.put("brandList",brandList);
            //3.根据模板ID获取回个列表
            List specList = (List) redisTemplate.boundHashOps("specList").get(templateId);
            map.put("specList",specList);
        }

        return map;
    }

}
