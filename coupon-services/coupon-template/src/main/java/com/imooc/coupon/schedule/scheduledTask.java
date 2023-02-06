package com.imooc.coupon.schedule;

import com.imooc.coupon.dao.CouponTemplateDao;
import com.imooc.coupon.entity.CouponTemplate;
import com.imooc.coupon.vo.TemplateRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Scheduled task to clear the expired coupon template
 */
@Slf4j
@Component
public class scheduledTask {

    private final CouponTemplateDao templateDao;


    public scheduledTask(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    /**
     * offline expired template
     */
    @Scheduled(fixedRate = 60 *60 * 1000)
    public void offlineCouponTemplate(){
        log.info("Start to Expire Coupon Template");

        List<CouponTemplate> templates = templateDao.findAllByExpired(false);
        if (CollectionUtils.isEmpty(templates)) {
            log.info("Done To Expire CouponTemplate");
            return;
        }
        Date curr = new Date();
        List<CouponTemplate> expiredTemplates =
                new ArrayList<>(templates.size());
        templates.forEach(t -> {
            // According to the "expiration" in template rule to validate the expiration of the template
            TemplateRule rule = t.getRule();
            if (rule.getExpiration().getDeadline() < curr.getTime()){
                t.setExpired(true);
                expiredTemplates.add(t);
            }
        });
        if (CollectionUtils.isNotEmpty(expiredTemplates)){
            log.info("Expired CouponTemplate Num: {}",
                    templateDao.saveAll(expiredTemplates));
        }

        log.info("Done To Expire CouponTemplate");
    }
}
