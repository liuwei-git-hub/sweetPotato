package Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.deploy.util.Waiter;
import com.sun.org.apache.xpath.internal.operations.Or;
import common.OrderStatus;
import entity.Account;
import entity.Order;
import entity.Orderltem;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 浏览订单
 */
@WebServlet("/orderBrowse")
public class OrderBrowseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("orderBrowse");
        System.out.println("已经运行");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html : charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        //根据当前用户ID查询订单
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("user");
        List<Order> ordersList = this.queryOrder(account.getId());
        System.out.println("ordersList"+ordersList);
        //查询到多个订单，List<Order>
        if (ordersList == null) {
            System.out.println("订单null");
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter pw = resp.getWriter();
            objectMapper.writeValue(pw, ordersList);
            Writer writer = resp.getWriter();
            writer.write(pw.toString());
        }
        //查询为null了，则没有订单
        //不为null，list转为json，放送前端
    }

    private List<Order> queryOrder(Integer account_id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Order> list = new ArrayList<>();
        try {
            String sql = this.getsql("@accountOrder");
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);
            resultSet = ps.executeQuery();
            Order order = null;
            while (resultSet.next()) {
                if(order == null){
                    order = new Order();
                    this.extractOrder(order,resultSet);
                    list.add(order);
                }
                String orderId = resultSet.getString("order_id");
                if(!orderId.equals(order.getId())){
                        order = new Order();
                        this.extractOrder(order,resultSet);
                        list.add(order);
                }

                Orderltem orderltem = this.extractOrderItem(resultSet);
                order.orderltemList.add(orderltem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void extractOrder(Order order, ResultSet resultSet) throws SQLException {
    order.setId(resultSet.getString("order_id"));
    order.setAccount_id(resultSet.getInt("account_id"));
    order.setAccount_name(resultSet.getString("account_name"));
    order.setCreate_time(resultSet.getString("create_time"));
    order.setFinish_time(resultSet.getString("finish_time"));
    order.setActual_amount(resultSet.getInt("actual_amount"));
    order.setTotal_money(resultSet.getInt("total_money"));
    order.setOrder_status(OrderStatus.valueOf(resultSet.getInt("order_status")));
    }

    private Orderltem extractOrderItem(ResultSet resultSet) throws SQLException {
        Orderltem orderltem = new Orderltem();
        orderltem.setId(resultSet.getInt("item_id"));
        orderltem.setOrderId(resultSet.getString("order_id"));
        orderltem.setGoodsId(resultSet.getInt("goods_id"));
        orderltem.setGoodsName(resultSet.getString("goods_name"));
        orderltem.setGoodsIntroduce(resultSet.getString("goods_introduce"));
        orderltem.setGoodsNum(resultSet.getInt("goods_num"));
        orderltem.setGoodsUnit(resultSet.getString("goods_unit"));
        orderltem.setGoodsPrice(resultSet.getInt("goods_price"));
        orderltem.setGoodsDiscount(resultSet.getInt("goods_discount"));
        return orderltem;
    }

    /**
     * 生成sql语句
     *
     * @param
     * @return
     */
    private String getsql(String sqlName) {
        InputStream in = this.getClass().getClassLoader().
                getResourceAsStream("scricpt/" + sqlName.substring(1) + ".sql");
        if (in == null) {
            throw new RuntimeException("加载sql文件出错");
        } else {
            //字节流转字符流
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(isr);

            try {
                StringBuffer sb = new StringBuffer();
                sb.append(reader.readLine());
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(" ").append(line);
                }
                System.out.println("sb"+sb);
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("sql语句发生异常");
            }
        }
    }
}
