package web.servlet;

import domain.User;
import org.apache.commons.beanutils.BeanUtils;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("utf-8");

        //获取用户填写的验证码
        String verifycode = request.getParameter("verifycode");

        //获取参数
        String registerUsername = request.getParameter("registerUsername");
        String registerPassword = request.getParameter("registerPassword");
        String registerEmail = request.getParameter("registerEmail");

        //获取Cookie中的验证码
        Cookie[] cookies = request.getCookies();
        String checkcode_server = null;
        if (cookies != null && cookies.length > 0){
            for (Cookie c : cookies){
                if ("CHECKCODE_SERVER".equals(c.getName())){
                    checkcode_server = c.getValue();
                }
            }
        }

        //销毁Cookie
        Cookie cookie = new Cookie("CHECKCODE_SERVER", null);
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        //验证码不正确verifycode
        if (!verifycode.equalsIgnoreCase(checkcode_server)){//忽略英文大小写验证
            boolean checkCodeFlag = true;
            //将验证码错误状态存入request域中
            request.setAttribute("checkCodeFlag", checkCodeFlag);

            //回显数据
            request.setAttribute("registerUsername", registerUsername);
            request.setAttribute("registerPassword", registerPassword);
            request.setAttribute("registerEmail", registerEmail);

            //跳转注册页面
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        //调用Service查询用户名是否存在
        UserService service = new UserServiceImpl();
        User user = service.findUserByUsername(registerUsername);

        //判断所注册的用户名是否存在
        if (user != null){//注册用户名存在，不可注册，注册失败
            //跳转到register.jsp
            boolean registerErrorFlag = true;
            //将注册失败状态存入request域中
            request.setAttribute("registerErrorFlag", registerErrorFlag);
            //回显数据
            request.setAttribute("registerUsername", registerUsername);
            request.setAttribute("registerPassword", registerPassword);
            request.setAttribute("registerEmail", registerEmail);
            request.getRequestDispatcher("/register.jsp").forward(request, response);

        } else {//注册用户名不存在，可以注册
            //调用Service注册
            service.addRegisterInfo(registerUsername, registerPassword, registerEmail);
            //跳转到loginUser.jsp
            boolean registerFlag = true;
            //将注册成功状态存入request域中
            request.setAttribute("registerFlag", registerFlag);
            //回显数据
            request.setAttribute("registerUsername", registerUsername);
            request.setAttribute("registerPassword", registerPassword);
            request.setAttribute("registerEmail", registerEmail);
            request.getRequestDispatcher("/loginUser.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
