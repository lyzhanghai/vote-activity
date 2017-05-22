package com.lyl.outsourcing.activity.controller;

import com.lyl.outsourcing.activity.annotation.AdminUserAuth;
import com.lyl.outsourcing.activity.dto.Result;
import com.lyl.outsourcing.activity.dto.SaveGroup;
import com.lyl.outsourcing.activity.dto.UpdateGroup;
import com.lyl.outsourcing.activity.dto.request.RaffleForm;
import com.lyl.outsourcing.activity.service.RaffleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by liyilin on 2017/5/16.
 */
@RestController
@RequestMapping("/raffle")
public class RaffleController {
    @Autowired
    private RaffleService raffleService;

    @AdminUserAuth
    @PostMapping
    public Object save(@RequestBody @Validated(SaveGroup.class) RaffleForm raffleForm) {
        Result result = raffleService.save(raffleForm);
        return result;
    }

    @AdminUserAuth
    @PostMapping("/{id}")
    public Object update(@RequestBody @Validated(UpdateGroup.class) RaffleForm raffleForm) {
        Result result = raffleService.update(raffleForm);
        return result;
    }

    @AdminUserAuth
    @PostMapping("/delete")
    public Object delete(@RequestBody Set<Long> idSet) {
        Result result = raffleService.delete(idSet);
        return result;
    }

    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id) {
        Result result = raffleService.getById(id);
        return result;
    }

    @GetMapping("/activityId/{acitivityId}")
    public Object getByActivityId(@PathVariable Long acitivityId) {
        Result result = raffleService.getByActivityId(acitivityId);
        return result;
    }

    @GetMapping
    public Object page(@RequestParam(defaultValue = "1") int pageIndex,
                       @RequestParam(defaultValue = "20") int pageSize) {
        Result result = raffleService.page(pageIndex, pageSize);
        return result;
    }
}
