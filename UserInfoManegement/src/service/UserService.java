package service;

import domain.PageBean;
import domain.User;

import java.util.List;
import java.util.Map;

/**
 *  用户管理的业务接口
 */
public interface UserService {
    /**
     * 查询所有用户信息
     * @return
     */
    List<User> findAll();

    /**
     * 登录方法
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password);

    /**
     * 保存User
     * @param user
     */
    void addUser(User user);

    /**
     * 根据id删除User
     * @param id
     */
    void deleteUser(String id);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    User findUserById(String id);

    /**
     * 修改用户信息
     * @param user
     */
    void updateUser(User user);

    /**
     * 批量删除用户信息
     * @param ids
     */
    void delSelectedUser(String[] ids);

    /**
     * 添加注册信息
     * @param username
     * @param password
     */
    void addRegisterInfo(String username, String password, String email);

    /**
     * 查询用户名是否存在
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 查询邮箱是否存在
     * @param email
     * @return
     */
    User findUserByEmail(String email);

    /**
     * 分页条件查询
     * @param currentPage
     * @param rows
     * @param condition
     * @return
     */
    PageBean<User> findUserByPage(String currentPage, String rows, Map<String, String[]> condition);
}
