package com.imooc.coupon.vo;


import com.imooc.coupon.constant.PeriodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRule {

    private Expiration expiration;

    private Discount discount;

    /**
     * Maximum number of coupon per person
     */
    private Integer limitatNion;

    private Usage usage;

    /**
     * can be applied together with any other coupon
     *
     */
    private String weight;


    public boolean validate(){
        return expiration.validate() && discount.validate()
                && limitatNion > 0 && usage.validate()
                && StringUtils.isNotEmpty(weight);
    }

    /**
     * valid period rule
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Expiration {

        /**
         * map to the [code] in PeriodType
         */
        private Integer period;

        /**
         * valid interval: only apply to dynamic valid period
         */
        private Integer gap;


        /**
         * expriration time
         */
        private Long deadline;

        boolean validate() {
            return null != PeriodType.of(period) && gap > 0 && deadline >0;
        }
    }

    /**
     * discount, apply to different types of coupon
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Discount {


        /**
         * amount of discount:
         * eg. ($20) off when order over $100
         * (85%) of original price (15% off)
         * ($10) off no conditions
         */
        private Integer quota;

        /**
         * baseline to apply discount
         */
        private Integer base;

        boolean validate(){
            return quota > 0 && base >0;
        }

    }

    /**
     * Usage range
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Usage {

        private String province;

        private String city;

        private String goodsType;

        boolean validate(){
            return StringUtils.isNotEmpty(province)
                    && StringUtils.isNotEmpty(city)
                    && StringUtils.isNotEmpty(goodsType);
        }
    }
}
