package com.lyl.outsourcing.activity.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lyl.outsourcing.activity.dto.SaveGroup;
import com.lyl.outsourcing.activity.dto.UpdateGroup;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by liyilin on 2017/5/6.
 */
public class ActivityForm {
    @NotNull(groups = UpdateGroup.class)
    private long id;
    @NotNull(groups = {SaveGroup.class, UpdateGroup.class})
    private String name;
    @NotNull(groups = {SaveGroup.class, UpdateGroup.class})
    private Date startTime;
    @NotNull(groups = {SaveGroup.class, UpdateGroup.class})
    private Date endTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
