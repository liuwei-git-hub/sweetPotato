package Servlet;

import entity.Account;

import org.apache.commons.codec.digest.DigestUtils;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * 注册
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try{
            String sql = "insert into account(username,password)values(?,?)";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,username);
            //ps.setString(2,password);
            ps.setString(2,DigestUtils.md5Hex(password));
            ps.executeUpdate();
            resp.sendRedirect("login.html");
        }catch (SQLException e){
            e.printStackTrace();
        }finally{
            DBUtil.close(connection,ps,null);
        }
    }
}
