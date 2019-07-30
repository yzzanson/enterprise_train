import com.enterprise.base.entity.UserEntity;
import com.enterprise.mapper.users.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by anson on 18/3/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
//@ContextConfiguration({"classpath*:applicationContext.xml"})
@ContextConfiguration({"classpath*:spring-mybatis.xml"})
//当然 你可以声明一个事务管理 每个单元测试都进行事务回滚 无论成功与否
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class UserServiceTest {

    @Resource
    private UserMapper userMapper;

//    @Test
//    public void testCreateUser(){
//        UserEntity userEntity = new UserEntity();
//        userEntity.setAvatar("http://a.jpg");
//        userEntity.setCreatedTime(new Date());
//        userEntity.setUpdatedTime(new Date());
//        userEntity.setDingUserId(1);
//        userEntity.setName("professor");
//        usersService.createUser(userEntity);
//    }

    @Test
    public void testGetUserById() {
        UserEntity userEntity = userMapper.getUserById(6);
        System.out.println(userEntity);
    }

}
