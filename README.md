**具体源码见：** [https://github.com/cug-lucifer/mp-demo/tree/master](https://github.com/cug-lucifer/mp-demo/tree/master)
# 快速入门
## 入门案例
### 需求：
1. 新增用户功能
2. 根据id查询用户
3. 根据id批量查询用户
4. 根据id更新用户
5. 根据id删除用户

### 使用MybatisPlus的基本步骤
1. 引入MybatisPlus依赖，代替Mybatis依赖
```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
</dependency>
```
2. 定义Mapper接口并继承BaseMapper
```java
public interface UserMapper extends BaseMapper<User> {
}
```
## 常见注解
MybatisPlus通过扫描实体类，并基于反射获取实体信息作为数据库表信息。
约定(注解)大于配置
**约定** 
- 类名驼峰转下花心作为表名
- 名为id的字段作为主键
- 变量名驼峰转下划线作为表的字段名

**自定义注解**
- @TableName 指定表名
- @TableId 用来指定表中主键字段信息
	-  IdType枚举： AUTO 自增长；
	-  INPUT 通过set方法自行输入；
	-  ASSIGN_ID：分配ID，默认雪花算法；
- @TableField 用来指定表中普通字段信息 
	- 成员变量名与数据库字段名不一致；
	- 变量名以is开头，且是布尔值；
	- 成员变量名与数据库关键字冲突，使用转义字符；
	- 成员变量不是数据库字段(exist = false)；

## 常见配置
```yaml
mybatis-plus:
  type-aliases-package: com.itheima.mp.domain.po # 别名扫描包
  mapper-locations: "classpath*:/mapper/**/*.xml" # Mapper.xml 文件地址，默认值
  configuration:
    map-underscore-to-camel-case: true # 是否开启下划线和驼峰的映射
    cache-enabled: false # 是否开启二级缓存
  global-config:
    db-config:
      id-type: assign_id # id默认未雪花算法生成
      update-strategy: not_null # 更新策略：只更新非空字段
```

# 核心功能
## 条件构造器 Wrapper
### 基于QueryWrapper的查询
需求：
1. 查询出名字中带o的，存款大于等于1000元的人的id、username、info、balance
2. 更新用户名为jack的用户的余额为2000
```java
	@Test
    void testQueryWrapper(){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>()
                .select("id","username","info","balance")
                .like("username","o")
                .ge("balance",1000);
        List<User> users = userMapper.selectList(userQueryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateByQueryWrapper(){
        User user = new User();
        user.setBalance(2000);
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("username", "jack");
        userMapper.update(user,wrapper);
    }
```
### 基于UpdateWrapper的更新
需求：更新id为1，2，4的用户的余额，扣200
```java
	@Test
    void testUpdateWrapper(){
        List<Long> ids = List.of(1L, 2L, 4L);
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance - 200")
                .in("id", ids);
        userMapper.update(null,wrapper);
    }
```
### 基于LambdaQuertyWarapper
```java
	@Test
    void testLambdaQueryWrapper(){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getInfo, User::getBalance)
                .like(User::getUsername,"o")
                .ge(User::getBalance,1000);
        List<User> users = userMapper.selectList(lambdaQueryWrapper);
        users.forEach(System.out::println);
    }
```
- QueryWrapper和LambdaQueryWrapper通常用来构建select、delete、update的where条件部分
- UpdateWrapper和LambdaUpdateWrapper通常只有在set语句中比较特殊才使用
- 尽量使用LambdaQueryWrapper和LambdaUpdateWrapper，避免硬编码。

## 自定义SQL
可以利用Wrapper构建复杂的where条件，然后自定义剩下的select语句
1. 基于Wrapper构建where条件
2. 在mapper方法参数中使用Param注解声明wrapper的变量名称，必须是ew
3. 自定义SQL语句，并使用wrapper方法

## Service接口
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/dc1f01fc7fab442892f3c324001a2d36.png)
使用流程？
- 自定义Service接口继承IService接口
```java
public interface IUserService extends IService<User> {}
```
- 自定义Service实现类，并继承ServiImpl类
```java
.@Service
public class UserServiceImpl 
	extends ServiceImpl<UserMapper, User> 
	implements IUserService {}
```
### 开发业务接口

案例： 基于Restful风格实现下面接口
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/7924bf693567400cbcf9193c5ee12018.png)
### IService的Lambda查询
需求：实现一个根据复杂条件查询用户的接口，查询条件如下：
- name：用户名关键字，可以为空
- status：用户状态，可以为空
- minBalance：最小余额，可以为空
- maxBalance：最大余额，可以为空
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/0a5f636a4bb04f599b5c87a63a74abda.png)
```java

    @Test
    void testSaveBatch(){
        List<User> users = new ArrayList<>(1000);
        long b = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            users.add(buildUser(i));
            if (i%1000==0){
                userService.saveBatch(users);
                users.clear();
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("TimeUsing: "+ (e-b));
    }
```

