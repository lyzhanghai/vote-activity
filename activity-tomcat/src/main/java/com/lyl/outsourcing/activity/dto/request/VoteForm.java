package com.lyl.outsourcing.activity.dto.request;

import com.lyl.outsourcing.activity.dto.SaveGroup;
import com.lyl.outsourcing.activity.dto.UpdateGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by liyilin on 2017/5/3.
 */
public class VoteForm {
    @NotNull(groups = UpdateGroup.class)
    private Long id;
    @NotNull(groups = SaveGroup.class)
    private Long activityId;
    @NotBlank
    @Size(max = 30)
    private String name;
    @NotBlank
    @Size(max = 500)
    private String role;
    @NotBlank
    @Size(max = 10000)
    private String introduction;
    @NotNull
    private Date startTime;
    @NotNull
    private Date endTime;

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
