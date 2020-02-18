package common;

import entity.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public enum OrderStatus {
    PLAYING(1,"待支付"),OK(2,"支付完成");
    private int flg;
    private String desc;
    OrderStatus(int flg,String desc){
        this.flg = flg;
        this.desc = desc;
    }
    public int getFlg() {
        return flg;
    }

    public void setFlg(int flg) {
        this.flg = flg;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    //根据数字找状态
    public static OrderStatus valueOf(int flg){
        for(OrderStatus orderStatus : OrderStatus.values()){
            if(orderStatus.flg == flg){
                return orderStatus;
            }
        }
        throw  new RuntimeException("支付失败");
    }

}
