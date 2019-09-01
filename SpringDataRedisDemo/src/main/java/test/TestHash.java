package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestHash {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testSetValue(){
        redisTemplate.boundHashOps("name").put("a","ljj");
        redisTemplate.boundHashOps("name").put("b","lisi");
        redisTemplate.boundHashOps("name").put("c","ljh");
    }

    @Test
    public void testKeyValue(){
        Set keys = redisTemplate.boundHashOps("name").keys();
        System.out.println("redisTemplate = " + keys);
    }

    @Test
    public void testGetValue(){
        List list = redisTemplate.boundHashOps("name").values();
        System.out.println("list = " + list);
    }

    @Test
    public void searchByKey(){
        Object str = redisTemplate.boundHashOps("name").get("b");
        System.out.println("str = " + str);
    }
    @Test
    public void delete(){
        redisTemplate.boundHashOps("name").delete("c");
    }
}
