package web.servlet;

import domain.User;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("utf-8");

        //获取参数
        String username = request.getParameter("userName");
        String password = request.getParameter("passWord");

        //调用Service查询用户名密码
        UserService service = new UserServiceImpl();
        User loginUser = service.login(username, password);
        HttpSession session = request.getSession();

        //判断用户名密码是否正确
        if (loginUser != null){
            //正确，登录成功

            //如果勾选了【记住密码】
            if ("1".equals(request.getParameter("remPwd"))){
                Cookie remPwdC = new Cookie("remPwd", loginUser.getUsername() + ":" + loginUser.getPassword());
                remPwdC.setPath(request.getContextPath());
                remPwdC.setMaxAge(60 * 60 * 24);//一天
                response.addCookie(remPwdC);
            }

            //如果勾选了【自动登录】
            if ("2".equals(request.getParameter("autoLogin"))){
                Cookie autoLoginC = new Cookie("autoLogin", loginUser.getUsername());
                autoLoginC.setPath(request.getContextPath());
                autoLoginC.setMaxAge(60 * 3);//三分钟
                response.addCookie(autoLoginC);
            }

            //将用户存入session
            session.setAttribute("user", loginUser);

            //跳转页面
            response.sendRedirect(request.getContextPath() + "/findUserByPageServlet");
        } else {
            //错误，登录失败

            boolean loginFlag = true; //登录状态
            //将登录状态存入request域中
            request.setAttribute("loginFlag", loginFlag);
            //回显数据
            request.setAttribute("loginError", username);
            //跳转登录页面
            request.getRequestDispatcher("/loginUser.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
