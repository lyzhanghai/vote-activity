package com.lyl.outsourcing.activity.controller;

import com.lyl.outsourcing.activity.annotation.AdminUserAuth;
import com.lyl.outsourcing.activity.dto.Result;
import com.lyl.outsourcing.activity.dto.SaveGroup;
import com.lyl.outsourcing.activity.dto.UpdateGroup;
import com.lyl.outsourcing.activity.dto.request.ActivityForm;
import com.lyl.outsourcing.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by liyilin on 2017/5/16.
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @AdminUserAuth
    @PostMapping
    public Object save(@RequestBody @Validated(SaveGroup.class) ActivityForm activityForm) {
        Result result = activityService.save(activityForm);
        return result;
    }

    @AdminUserAuth
    @PostMapping("/{id}")
    public Object update(@RequestBody @Validated(UpdateGroup.class) ActivityForm activityForm) {
        Result result = activityService.update(activityForm);
        return result;
    }

    @AdminUserAuth
    @PostMapping("/delete")
    public Object delete(@RequestBody Set<Long> idSet) {
        Result result = activityService.delete(idSet);
        return result;
    }

    @GetMapping
    public Object page(@RequestParam(defaultValue = "") String nameLike,
                       @RequestParam(defaultValue = "1") Integer pageIndex,
                       @RequestParam(defaultValue = "20") Integer pageSize) {
        Result result = activityService.page(nameLike, pageIndex, pageSize);
        return result;
    }

    @GetMapping("/{id}")
    public Object getById(@RequestParam Long id) {
        Result result = activityService.getById(id);
        return result;
    }
}
