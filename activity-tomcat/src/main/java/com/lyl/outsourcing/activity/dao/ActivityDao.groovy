package com.lyl.outsourcing.activity.dao

import com.lyl.outsourcing.activity.dao.mapper.ActivityMapper
import com.lyl.outsourcing.activity.entity.Activity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by liyilin on 2017/4/21.
 */
@Repository
class ActivityDao extends BaseDao<Activity> implements ActivityMapper {

    @Autowired
    @Delegate
    private ActivityMapper mapper

    ActivityDao() {
        super(Activity.class)
    }
}
