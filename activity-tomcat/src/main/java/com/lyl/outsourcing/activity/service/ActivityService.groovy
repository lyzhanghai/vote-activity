package com.lyl.outsourcing.activity.service

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import com.lyl.outsourcing.activity.dao.ActivityDao
import com.lyl.outsourcing.activity.dao.VoteDao
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.dto.request.ActivityForm
import com.lyl.outsourcing.activity.entity.Activity
import com.lyl.outsourcing.activity.entity.ActivityExample
import com.lyl.outsourcing.activity.exception.ErrorCode
import com.lyl.outsourcing.activity.exception.FieldException
import com.lyl.outsourcing.activity.exception.ObjectNotFoundException
import com.lyl.outsourcing.activity.exception.WebException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by liyilin on 2017/5/3.
 */
@Service
class ActivityService {
    @Autowired
    private ActivityDao activityDao
    @Autowired
    private VoteDao voteDao

    Result save(ActivityForm saveForm) {
        Activity activity = new Activity()
        activity.name = saveForm.name
        activity.startTime = saveForm.startTime
        activity.endTime = saveForm.endTime

        Date now = new Date()
        activity.createTime = now
        activity.updateTime = now

        activityDao.insert(activity)

        new Result(0, null, activity)
    }

    Result update(ActivityForm updateForm) {
        Activity activity = activityDao.selectByPrimaryKey(updateForm.id)
        if (activity == null)
            throw new ObjectNotFoundException(updateForm.id, Activity.class)

        activity.name = updateForm.name
        activity.startTime = updateForm.startTime
        activity.endTime = updateForm.endTime
        activity.updateTime = new Date()

        assertTimeLegal(activity)

        activityDao.updateByPrimaryKey(activity)

        new Result(0, null, activity)
    }

    Result delete(Set<Long> idSet) {
        def list = []
        idSet.each {
            try {
                long count = voteDao.countByActivityId(it)
                if (count > 0)
                    throw new WebException(ErrorCode.GLOBAL_ERROR, "ID为${it}的活动下还有${count}个投票项目，因此不能删除该活动")
                int row = activityDao.deleteByPrimaryKey(it)
                if (row == 1) list << [id:it, msg:null]
                else throw new ObjectNotFoundException(it, Activity.class)
            } catch (Exception e) {
                list << [id:it, msg:"${e.getClass().getName()}: ${e.getMessage()}"]
            }
        }
        new Result(0, null, list)
    }

    /**
     * 检测活动修改开始和结束时间的合法性
     * @param activity
     */
    private assertTimeLegal(Activity activity) {
        if (activity.startTime.time >= activity.endTime.time)
            throw new FieldException("startTime", "开始时间必须早于结束时间")
        if (voteDao.countWithIlegalStartTime(activity.id, activity.startTime) != 0)
            throw new FieldException("startTime", "活动开始时间必须早于或等于该活动下所有投票主题的开始时间")
        if (voteDao.countWithIlegalEndTime(activity.id, activity.startTime) != 0)
            throw new FieldException("endTime", "活动结束时间必须迟于或等于该活动下所有投票主题的结束时间")
    }

    Result page(String nameLike, int pageIndex, int pageSize) {
        ActivityExample example = new ActivityExample()
        example.or().andNameLike("%${nameLike}%")
        PageHelper.startPage(pageIndex, pageSize)
        List<Activity> list = activityDao.selectByExample(example)
        PageInfo<Activity> pageInfo = new PageInfo<>(list)
        new Result(0, null, pageInfo)
    }

    Result getById(long id) {
        Activity activity = activityDao.selectByPrimaryKey(id)
        if (activity == null)
            throw new ObjectNotFoundException(id, Activity.class)
        new Result(0, null, activity)
    }
}
