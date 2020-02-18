package Servlet;

import entity.Account;
import org.apache.commons.codec.digest.DigestUtils;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 登录
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println("username"+username);
        System.out.println("password"+password);//MD5加密
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Account account = new Account();
        try {
            String sql = "select id,username,password from account where username=? and password=?";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);
            ps.setString(1,username);
            //ps.setString(2,password);
            ps.setString(2,DigestUtils.md5Hex(password));
            resultSet = ps.executeQuery();
            if(resultSet.next()){
                account.setId(resultSet.getInt("id"));
                account.setUsername(resultSet.getString("username"));
                account.setPassword(resultSet.getString("password"));

            }
            Writer writer = resp.getWriter();

            if(account.getId() == null){
//                System.out.println("用户名或密码错误");
                writer.write("<h2>用户名或密码错误:"+username+"</h2>");
            }else{
                //获取session,如果session不存在，就新建一个，创建的当前请求体的用户
                HttpSession session = req.getSession();
                session.setAttribute("user",account);
                resp.sendRedirect("index.html");
            }
        }catch (SQLException e){
            e.printStackTrace();

        }
    }
}
