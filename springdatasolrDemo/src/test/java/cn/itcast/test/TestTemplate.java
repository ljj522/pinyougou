package cn.itcast.test;

import cn.itcast.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.beans.Beans;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-solr.xml")
public class TestTemplate {

    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testAdd(){
        TbItem item = new TbItem();
        item.setId(1L);
        item.setTitle("华为METE10");
        item.setCategory("手机");
        item.setBrand("华为");
        item.setGoodsId(10L);
        item.setPrice(new BigDecimal(3000.01));

        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }

    @Test
    public void findById(){
        TbItem item = solrTemplate.getById(1L, TbItem.class);
        try {
            System.out.println("solrTemplate = " + item.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("没有该商品");
        }
    }

    @Test
    public void deleteById(){
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    @Test
    public void testAddList(){
        List list = new ArrayList();
        for (int i=0;i<100;i++){
            TbItem item = new TbItem();
            item.setId(i+1L);
            item.setTitle("华为METE"+i);
            item.setCategory("手机");
            item.setBrand("华为"+i);
            item.setSeller("华为旗舰店");
            item.setGoodsId(10L);
            item.setPrice(new BigDecimal(3000.01));
            list.add(item);
        }
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    @Test
    public void testPageQuery(){
        Query query = new SimpleQuery("*:*");

        query.setOffset(20);//开始索引
        query.setRows(20);//每页记录数
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        for (TbItem item:page.getContent()){
            System.out.println(item.getTitle()+" "+item.getPrice()+" "+item.getBrand());
        }
        System.out.println("总记录数：" + page.getTotalElements());
        System.out.println("总页数：" + page.getTotalPages());
    }

    @Test
    public void testPageQueryMutil(){
        Query query = new SimpleQuery("*:*");

        Criteria criteria = new Criteria("item_category").contains("手机");
        criteria=criteria.and("item_brand").contains("2");
         query.addCriteria(criteria);

        //query.setOffset(20);//开始索引
        //query.setRows(20);//每页记录数
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        for (TbItem item:page.getContent()){
            System.out.println(item.getTitle()+" "+item.getPrice()+" "+item.getBrand());
        }
        System.out.println("总记录数：" + page.getTotalElements());
        System.out.println("总页数：" + page.getTotalPages());
    }

    @Test
    public void deleteAll(){

        Query query=new SimpleQuery("*:*");

        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}
