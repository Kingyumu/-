package web.servlet;

import domain.User;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

@WebServlet("/sendEmailServlet")
public class SendEmailServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取参数
        String registerUsername = request.getParameter("registerUsername");
        String registerPassword = request.getParameter("registerPassword");
        String registerEmail = request.getParameter("registerEmail");

        //调用Service查询此邮箱是否已被注册
        UserService service = new UserServiceImpl();
        User user = service.findUserByEmail(registerEmail);

        if (user != null){//邮箱已存在
            boolean emailExits = true;
            request.setAttribute("emailExits", emailExits);
            //回显数据
            request.setAttribute("registerUsername", registerUsername);
            request.setAttribute("registerPassword", registerPassword);
            request.setAttribute("registerEmail", registerEmail);
            request.getRequestDispatcher("/register.jsp").forward(request, response);

        } else {//邮箱不存在

            //生成验证码
            String base = "0123456789";
            int size = base.length();
            Random r = new Random();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 6; i++ ){
                //产生0到size-1的随机值
                int index = r.nextInt(size);
                //在base字符串中获取下标为index的字符
                char c = base.charAt(index);
                sb.append(c);
            }

            //将验证码放入Cookie中
            String checkCode = sb.toString();
            Cookie cookie = new Cookie("CHECKCODE_SERVER", checkCode);
            cookie.setPath(request.getContextPath());
            cookie.setMaxAge(60 * 5);//5分钟
            response.addCookie(cookie);

            /**
             *  发送电子邮件验证码
             */
            // 创建Properties 类用于记录邮箱的一些属性
            Properties props = new Properties();
            // 表示SMTP发送邮件，必须进行身份验证
            props.put("mail.smtp.auth", "true");
            //此处填写SMTP服务器
            props.put("mail.smtp.host", "smtp.qq.com");
            //端口号，QQ邮箱给出了两个端口，但是另一个我一直使用不了，所以就给出这一个587
            props.put("mail.smtp.port", "587");
            // 此处填写你的账号
            props.put("mail.user", "kingyumu@vip.qq.com");
            // 此处的密码就是前面说的16位STMP口令
            props.put("mail.password", "yyjpcgfzysaqbchf");

            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };

            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            InternetAddress form;
            try {
                form = new InternetAddress(props.getProperty("mail.user"));
                message.setFrom(form);

                // 设置收件人的邮箱
                InternetAddress to = new InternetAddress(registerEmail);
                message.setRecipient(MimeMessage.RecipientType.TO, to);

                // 设置邮件标题
                message.setSubject("用户信息管理系统验证");

                // 设置邮件的内容体
                message.setContent("验证码：" + checkCode + "，5分钟内有效，请尽快使用！", "text/html;charset=UTF-8");

                // 最后当然就是发送邮件啦
                Transport.send(message);

                //回显数据
                request.setAttribute("registerUsername", registerUsername);
                request.setAttribute("registerPassword", registerPassword);
                request.setAttribute("registerEmail", registerEmail);

                //将发送电子邮件状态存入request域中
                boolean emailSucc = true;
                request.setAttribute("emailSucc", emailSucc);
            } catch (Exception e) {
                e.printStackTrace();

                //回显数据
                request.setAttribute("registerUsername", registerUsername);
                request.setAttribute("registerPassword", registerPassword);
                request.setAttribute("registerEmail", registerEmail);

                //将发送电子邮件状态存入request域中
                boolean emailError = true;
                request.setAttribute("emailError", emailError);
            }
            request.getRequestDispatcher("/register.jsp").forward(request, response);

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
