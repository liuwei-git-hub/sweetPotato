package Servlet;

import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 商品上架
 */
@WebServlet("/inbound")
public class GoodsPutAwayServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        String stock = req.getParameter("stock");
        String introduce = req.getParameter("introduce");
        String unit = req.getParameter("unit");
        String price = req.getParameter("price");
        String discount = req.getParameter("discount");

        double doublePrice = Double.valueOf(price);
        int realPrice = new Double(doublePrice*100).intValue();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            String sql = "insert into goods(name,stock,introduce,unit,price,discount)values(?,?,?,?,?,?)";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);
            ps.setString(1,name);
            ps.setInt(2,Integer.valueOf(stock));
            ps.setString(3,introduce);
            ps.setString(4,unit);
            ps.setInt(5,realPrice);
            ps.setInt(6,Integer.valueOf(discount));
            int ret = ps.executeUpdate();
            if(ret == 1){
                resp.sendRedirect("index.html");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(connection,ps,null);
        }

    }
}
