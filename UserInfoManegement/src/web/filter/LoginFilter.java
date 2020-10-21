package web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 登录验证的过滤器
 */
@WebFilter("/*")
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //强制转换
        HttpServletRequest request = (HttpServletRequest) req;

        //获取资源请求路径
        String uri = request.getRequestURI();
        //判断是否包含登录相关资源路径
        if (uri.contains("/loginUser.jsp") || uri.contains("/loginServlet") || uri.contains("/register.jsp")
                || uri.contains("/css/") || uri.contains("/fonts/")
                || uri.contains("/img/") || uri.contains("/js/")
                || uri.contains("/registerServlet") || uri.contains("/sendEmailServlet")
                || uri.contains("/findUserByPageServlet")){
            //包含，用户就是想登录，放行
            chain.doFilter(req, resp);
        } else {
            //不包含，需要验证用户是否登录
            //从session中获取user
            Object user = request.getSession().getAttribute("user");
            if (user != null){
                //已经登录，放行
                chain.doFilter(req, resp);
            } else {
                //没有登录，跳转登录页面
                boolean loginFalse = true; //登录状态
                //将登录状态存入request域中
                request.setAttribute("loginFalse", loginFalse);
                request.getRequestDispatcher("/loginUser.jsp").forward(request, resp);
            }
        }

        //chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
