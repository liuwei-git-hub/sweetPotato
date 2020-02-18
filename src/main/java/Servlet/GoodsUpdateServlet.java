package Servlet;

import entity.Goods;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.spec.PSSParameterSpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * 更新商品
 */
@WebServlet("/updategoods")
public class GoodsUpdateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String goodsIdString = req.getParameter("goodsID");
        int goodsId = Integer.valueOf(goodsIdString);
        String name = req.getParameter("name");
        String stock = req.getParameter("stock");
        String introduce = req.getParameter("introduce");
        String unit = req.getParameter("unit");
        String price = req.getParameter("price");
        String discount = req.getParameter("discount");
        double doublePrice = Double.valueOf(price);
        int realPrice = new Double(doublePrice * 100).intValue();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Goods goods = this.getGoods(goodsId);
        //查看当前goodsId是否存在
        if(goods == null){
            System.out.println("没有该商品");
            resp.sendRedirect("index.html");
        }else{
            //找到后进行删除
            goods.setName(name);
            goods.setIntroduce(introduce);
            goods.setStock(Integer.valueOf(stock));
            goods.setUnit(unit);
            goods.setPrice(Integer.valueOf(price));
            goods.setDiscount(Integer.valueOf(discount));
        }
        boolean effect = this.modifyGoods(goods);
        //更新插入数据库
        if(effect){
            System.out.println("更新成功！");
            resp.sendRedirect("goodsbrowse.html");
        }else{
            System.out.println("更新失败！");
            resp.sendRedirect("index.html");
        }
    }
    private boolean modifyGoods(Goods goods){
        Connection connection = null;
        PreparedStatement ps = null;
        boolean effect = false;
        try{
            String sql = "update goods set name=?,introduce=?,stock=?,unit=?,price=?,discount=? where id=?";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);
            ps.setString(1,goods.getName());
            ps.setString(2,goods.getIntroduce());
            ps.setInt(3,goods.getStock());
            ps.setString(4,goods.getUnit());
            ps.setInt(5,goods.getPriceInt());
            ps.setInt(6,goods.getDiscount());
            ps.setInt(7,goods.getId());
            effect = (ps.executeUpdate() == 1);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,ps,null);
        }
        return effect;
    }
    //查询商品
    private Goods getGoods(int goodsid){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Goods goods = null;
        try{
            String sql = "select * from goods where id=?";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);
            ps.setInt(1,goodsid);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                 goods = this.extractGoods(resultSet);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,ps,resultSet);
        }
        return goods;
    }
    //解析商品
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
