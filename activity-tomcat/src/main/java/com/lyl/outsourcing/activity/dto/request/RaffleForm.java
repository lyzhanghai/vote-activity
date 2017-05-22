package com.lyl.outsourcing.activity.dto.request;

import com.lyl.outsourcing.activity.dto.SaveGroup;
import com.lyl.outsourcing.activity.dto.UpdateGroup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by liyilin on 2017/5/8.
 */
public class RaffleForm implements Serializable {
    @NotNull(groups = UpdateGroup.class)
    private Long id;

    private Long activityId;
    @NotNull(groups = {UpdateGroup.class, SaveGroup.class})
    @Size(min = 1, max = 30, groups = {UpdateGroup.class, SaveGroup.class})
    private String name;
    @NotNull(groups = {UpdateGroup.class, SaveGroup.class})
    @Size(min = 1, max = 500, groups = {UpdateGroup.class, SaveGroup.class})
    private String role;
    @NotNull(groups = {UpdateGroup.class, SaveGroup.class})
    private Date startTime;
    @NotNull(groups = {UpdateGroup.class, SaveGroup.class})
    private Date endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}
