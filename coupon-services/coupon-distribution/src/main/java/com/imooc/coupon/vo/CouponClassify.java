package com.imooc.coupon.vo;

import com.imooc.coupon.constant.CouponStatus;
import com.imooc.coupon.constant.PeriodType;
import com.imooc.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classify coupon based on the status
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponClassify {

    /**
     * usable coupons
     */
    private List<Coupon> usable;

    /**
     * used coupons
     */
    private List<Coupon> used;

    /**
     * expired coupons
     */
    private List<Coupon> expired;


    /**
     * classify current coupons
     * @param coupons
     * @return
     */
    public static CouponClassify classify(List<Coupon> coupons){
        List<Coupon> usable = new ArrayList<>(coupons.size());
        List<Coupon> used = new ArrayList<>(coupons.size());
        List<Coupon> expired = new ArrayList<>(coupons.size());

        coupons.forEach(c -> {
            boolean isTimeExpired;
            long curTime = new Date().getTime();

            // check if coupon expired
            if (c.getTemplateSDK().getRule().getExpiration().getPeriod().equals(
                    PeriodType.REGULAR.getCode()
            )){
                isTimeExpired = c.getTemplateSDK().getRule().getExpiration().getDeadline() <= curTime;
            } else {
                isTimeExpired = DateUtils.addDays(
                        c.getAssignTime(),
                        c.getTemplateSDK().getRule().getExpiration().getGap()
                ).getTime() <= curTime;
            }

            if (c.getStatus() == CouponStatus.USED){
                used.add(c);
            }else if (c.getStatus() == CouponStatus.EXPIRED || isTimeExpired){
                expired.add(c);
            }else{
                usable.add(c);
            }

        });

        return new CouponClassify(usable, used, expired);
    }
}
