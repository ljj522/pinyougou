package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestList {

    @Autowired
    private RedisTemplate redisTemplate;

    //左一再
    @Test
    public void testSetValue(){
        redisTemplate.boundListOps("namelist1").rightPush("ljj");
        redisTemplate.boundListOps("namelist1").rightPush("lisi");
        redisTemplate.boundListOps("namelist1").rightPush("ljh");
    }

    @Test
    public void testGetValue(){
        List list = redisTemplate.boundListOps("namelist1").range(0,10);
        System.out.println("list = " + list);
    }

    @Test
    public void testSetValue2(){
        redisTemplate.boundListOps("namelist2").leftPush("ljj");
        redisTemplate.boundListOps("namelist2").leftPush("lisi");
        redisTemplate.boundListOps("namelist2").leftPush("ljh");
    }

    @Test
    public void testGetValue2(){
        List list = redisTemplate.boundListOps("namelist2").range(0,10);
        System.out.println("list = " + list);
    }

    @Test
    public void searchByIndex(){
         String str=(String)redisTemplate.boundListOps("namelist2").index(1);
        System.out.println("redisTemplate = " + str);
    }
    @Test
    public void removeValue(){
        redisTemplate.boundListOps("namelist2").remove(1,"ljj");
    }
}
