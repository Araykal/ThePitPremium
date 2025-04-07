package cn.charlotte.pit.data.sub;

import lombok.Data;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/27 16:56
 */
@Data
public class TradeLimit {

    private long lastRefresh;
    private int times;
    private double amount;

}
