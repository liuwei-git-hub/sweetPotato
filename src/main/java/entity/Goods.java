package entity;

import lombok.Data;

/**
 * 商品
 */
@Data
public class Goods {
    private Integer id;
    private String name;
    private String introduce;//商品介绍
    private Integer stock;//商品储备
    private String unit;//单位
    private Integer price;//价格
    private Integer discount;//折扣
    private Integer buyGoodsNum;//购买商品数量

    public Integer getBuyGoodsNum() {
        return buyGoodsNum;
    }

    public void setBuyGoodsNum(Integer buyGoodsNum) {
        this.buyGoodsNum = buyGoodsNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price*1.0/100;
    }
    public int getPriceInt() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}
