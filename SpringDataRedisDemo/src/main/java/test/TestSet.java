package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestSet {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void setValue(){
        redisTemplate.boundSetOps("nameset").add("ljj");
        redisTemplate.boundSetOps("nameset").add("lisi");
        redisTemplate.boundSetOps("nameset").add("ljh");
    }

    @Test
    public void getValue(){
        Set set = redisTemplate.boundSetOps("nameset").members();
        System.out.println("set = " + set);
    }

    @Test
    public void removeValue(){
        redisTemplate.boundSetOps("nameset").remove("ljj");
    }
    @Test
    public void delete(){
        redisTemplate.delete("nameset");
    }
}
