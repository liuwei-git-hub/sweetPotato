package Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Goods;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品浏览
 */
@WebServlet("/goodsbrowse")
public class GoodsBrowseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("GoodsBrowseServlet");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Goods> list = new ArrayList<>();
        try {
            String sql = "select id,name,introduce,stock,unit,price,discount from goods";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);
            resultSet = ps.executeQuery();
            //解析结果集
            while(resultSet.next()){
                Goods goods = this.extractGoods(resultSet);
                if(goods != null){
                    list.add(goods);
                }
            }
            System.out.println("list"+list);
            //将list转换为Json
            ObjectMapper mapper = new ObjectMapper();
            PrintWriter pw = resp.getWriter();
            mapper.writeValue(pw,list);
            Writer writer = resp.getWriter();
            writer.write(pw.toString());
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,ps,resultSet);
        }
    }
    public Goods extractGoods(ResultSet resultSet) throws SQLException{
        Goods goods = new Goods();
        goods.setId(resultSet.getInt("id"));
        goods.setName(resultSet.getString("name"));
        goods.setIntroduce(resultSet.getString("introduce"));
        goods.setStock(resultSet.getInt("stock"));
        goods.setUnit(resultSet.getString("unit"));
        goods.setPrice(resultSet.getInt("price"));
        goods.setDiscount(resultSet.getInt("discount"));
        return goods;
    }
}
