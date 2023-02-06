package com.imooc.coupon.constant;

/**
 * Global constant
 */
public class Constant {

    /**
     * Message topic for kafka
     */
    public static final String TOPIC = "user_coupon_op";

    /**
     * Redis Key prefix
     */
    public static class RedisPrefix {

        /**
         * prefix for coupon code
         */
        public static final String COUPON_TEMPLATE = "coupon_template_code_";

        /**
         * prefix for all usable coupons of current user
         */
        public static final String USER_COUPON_USABLE = "user_coupon_usable_";

        /**
         * prefix for all used coupons of current user
         */
        public static final String USER_COUPON_USED = "user_coupon_used_";

        /**
         * prefix for all expired coupons of current user
         */
        public static final String USER_COUPON_EXPIRED = "user_coupon_expired_";
    }
}
