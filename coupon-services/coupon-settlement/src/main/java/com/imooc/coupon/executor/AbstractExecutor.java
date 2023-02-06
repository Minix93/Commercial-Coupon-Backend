package com.imooc.coupon.executor;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.vo.GoodsInfo;
import com.imooc.coupon.vo.SettlementInfo;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class for executor
 */
public abstract class AbstractExecutor {


    /**
     * 1. this only implements the coupon with single goods type, multi goods type coupon need to override this method
     * 2. goods type only need to match one type in the coupon template rules
     * Validate if goods type matchs coupon rule
     * @param settlement
     * @return
     */
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlement){
        List<Integer> goodsType = settlement.getGoodsInfos()
                .stream().map(GoodsInfo::getType)
                .collect(Collectors.toList());

        List<Integer> templateGoodsType = JSON.parseObject(
                settlement.getCouponAndTemplateInfos().get(0).getTemplate()
                        .getRule().getUsage().getGoodsType(),
                List.class
        );

        return CollectionUtils.isNotEmpty(
                CollectionUtils.intersection(goodsType, templateGoodsType)
        );
    }

    /**
     * process the scenario where goodstype doesn't match
     * @param settlement {@link SettlementInfo} settlement info send from user
     * @param goodsSum goods price sum
     * @return {@link SettlementInfo} modified settlement info
     */
    protected SettlementInfo processGoodsTypeNotSatisfy(
            SettlementInfo settlement, double goodsSum
    ) {

        boolean isGoodsTypeSatisfy = isGoodsTypeSatisfy(settlement);

        // when goodstype is not satisfied, return the original price and empty the coupon list
        if (!isGoodsTypeSatisfy) {
            settlement.setCost(goodsSum);
            settlement.setCouponAndTemplateInfos(Collections.emptyList());
            return settlement;
        }

        return null;
    }

    /**
     * Calculate the total price
     * @param goodsInfos
     * @return
     */
    protected double goodsCostSum(List<GoodsInfo> goodsInfos) {

        return goodsInfos.stream().mapToDouble(
                g -> g.getPrice() * g.getCount()
        ).sum();
    }

    /**
     * Keep 2 decimals
     * @param value
     * @return
     */
    protected double retain2Decimals(double value) {
        return new BigDecimal(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * minimum price user need to pay
     * @return
     */
    protected double minCost() {

        return 0.1;
    }
}
