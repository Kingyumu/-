package dao.impl;

import dao.UserDao;
import domain.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<User> findAll() {
        //使用JDBC操作数据库
        //定义sql
        String sql = "select * from user";
        List<User> users = template.query(sql, new BeanPropertyRowMapper<User>(User.class));

        return users;
    }

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        try{
            String sql = "select * from login where username = ? and password = ?";
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
            return user;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void add(User user) {
        //定义sql
        String sql = "insert into user values(null,?,?,?,?,?,?)";
        //执行sql
        template.update(sql, user.getName(), user.getGender(), user.getAge(), user.getAddress(), user.getQq(), user.getEmail());
    }

    @Override
    public void delete(int id) {
        //定义sql
        String sql = "delete from user where id = ?";
        //执行sql
        template.update(sql, id);
    }

    @Override
    public User findById(int id) {
        String sql = "select * from user where id = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), id);
    }

    @Override
    public void update(User user) {
        String sql = "update user set name = ?, gender = ?, age = ?, address = ?, qq = ?, email = ? WHERE id = ?";
        template.update(sql, user.getName(), user.getGender(), user.getAge(), user.getAddress(), user.getQq(), user.getEmail(), user.getId());
    }

    @Override
    public void addUsernameAndPasswordAndEmail(String username, String password, String email) {
        String sql = "insert into login values(null, ?, ?, ?)";
        template.update(sql, username, password, email);
    }

    @Override
    public User findByUsername(String username) {
        try{
            String sql = "select * from login where username = ?";
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
            return user;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User findByEmail(String email) {
        try{
            String sql = "select * from login where email = ?";
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), email);
            return user;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int findTotalCount(Map<String, String[]> condition) {
        //处理查询不到数据的异常
        try {
            //定义模板初始化sql
            String sql = "select count(*) from user WHERE 1 = 1 ";
            StringBuilder sb = new StringBuilder(sql);
            //遍历map
            Set<String> keySet = condition.keySet();//获取所有键
            //定义参数的集合
            List<Object> params = new ArrayList<Object>();
            for (String key : keySet){
                //排除分页条件参数
                if ("currentPage".equals(key) || "rows".equals(key)){
                    continue;
                }

                //获取value
                String value = condition.get(key)[0];
                //判断value是否有值
                if (value != null && !"".equals(value)){
                    //有值
                    sb.append(" and " + key + " like ? ");
                    params.add("%" + value + "%");//？的值
                }
            }

            return template.queryForObject(sb.toString(), Integer.class, params.toArray());
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<User> findByPage(int start, int rows, Map<String, String[]> condition) {
        //处理查询不到数据的异常
        try {
            String sql = "select * from user WHERE 1 = 1";
            StringBuilder sb = new StringBuilder(sql);
            //遍历map
            Set<String> keySet = condition.keySet();//获取所有键
            //定义参数的集合
            List<Object> params = new ArrayList<Object>();
            for (String key : keySet){
                //排除分页条件参数
                if ("currentPage".equals(key) || "rows".equals(key)){
                    continue;
                }

                //获取value
                String value = condition.get(key)[0];
                //判断value是否有值
                if (value != null && !"".equals(value)){
                    //有值
                    sb.append(" and " + key + " like ? ");
                    params.add("%" + value + "%");//？的值
                }
            }

            //添加分页查询
            sb.append(" limit ?, ? ");
            //添加分页查询参数值
            params.add(start);
            params.add(rows);

            return template.query(sb.toString(), new BeanPropertyRowMapper<User>(User.class), params.toArray());
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
