package com.lyl.outsourcing.activity.dto.request;

import com.lyl.outsourcing.activity.dto.SaveGroup;
import com.lyl.outsourcing.activity.dto.UpdateGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by liyilin on 2017/5/8.
 */
public class RaffleItemForm {
    @NotNull(groups = UpdateGroup.class)
    private Long id;
    @NotNull(groups = SaveGroup.class)
    private Long raffleId;
    @NotBlank(groups = {SaveGroup.class, UpdateGroup.class})
    @Size(max = 30, groups = {SaveGroup.class, UpdateGroup.class})
    private String name;
    @Size(min = 1, max = 100, groups = {SaveGroup.class, UpdateGroup.class})
    private String description;

    private boolean price = true;
    @NotNull(groups = {SaveGroup.class, UpdateGroup.class})
    @Min(value = 1, groups = {SaveGroup.class, UpdateGroup.class})
    private int totalNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRaffleId() {
        return raffleId;
    }

    public void setRaffleId(Long raffleId) {
        this.raffleId = raffleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public boolean isPrice() {
        return price;
    }

    public void setPrice(boolean price) {
        this.price = price;
    }
}
