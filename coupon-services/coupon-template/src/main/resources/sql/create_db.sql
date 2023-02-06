CREATE TABLE IF NOT EXISTS `coupon_data`.`coupon_template` (
     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Auto increased primary key',
     `available` boolean NOT NULL DEFAULT false COMMENT ' true: available, false: unavailable',
     `expired` boolean NOT NULL DEFAULT false COMMENT ' true: expired, false: not expired',
     `name` varchar(64) NOT NULL DEFAULT '' COMMENT 'Coupon Name',
     `logo` varchar(256) NOT NULL DEFAULT '' COMMENT 'Coupon logo',
     `intro` varchar(256) NOT NULL DEFAULT '' COMMENT 'Coupon Description',
     `category` varchar(64) NOT NULL DEFAULT '' COMMENT 'Coupon Categroy',
     `product_line` int(11) NOT NULL DEFAULT '0' COMMENT 'ProductLine',
     `coupon_count` int(11) NOT NULL DEFAULT '0' COMMENT 'Total Count',
     `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT 'Creation time',
     `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Creation user',
     `template_key` varchar(128) NOT NULL DEFAULT '' COMMENT 'Coupon Template code',
     `target` int(11) NOT NULL DEFAULT '0' COMMENT 'Target user',
     `rule` varchar(1024) NOT NULL DEFAULT '' COMMENT 'CouponTemplate Rule: JSON format of TemplateRule',
     PRIMARY KEY (`id`),
     KEY `idx_category` (`category`),
     KEY `idx_user_id` (`user_id`),
     UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='Coupon Template Table';



-- truncate coupon_template;