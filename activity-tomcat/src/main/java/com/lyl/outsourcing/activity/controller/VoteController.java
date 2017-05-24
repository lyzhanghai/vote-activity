package com.lyl.outsourcing.activity.controller;

import com.lyl.outsourcing.activity.annotation.AdminUserAuth;
import com.lyl.outsourcing.activity.annotation.OpenID;
import com.lyl.outsourcing.activity.annotation.WxAuth;
import com.lyl.outsourcing.activity.dto.Result;
import com.lyl.outsourcing.activity.dto.SaveGroup;
import com.lyl.outsourcing.activity.dto.UpdateGroup;
import com.lyl.outsourcing.activity.dto.request.VoteForm;
import com.lyl.outsourcing.activity.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by liyilin on 2017/5/16.
 */
@RestController
@RequestMapping("/vote")
public class VoteController {
    @Autowired
    private VoteService voteService;

    @AdminUserAuth
    @PostMapping
    public Object save(@RequestBody @Validated(SaveGroup.class) VoteForm voteForm) {
        Result result = voteService.save(voteForm);
        return result;
    }

    @AdminUserAuth
    @PostMapping("/{id}")
    public Object update(@RequestBody @Validated(UpdateGroup.class) VoteForm voteForm) {
        Result result = voteService.update(voteForm);
        return result;
    }

    @AdminUserAuth
    @PostMapping("/delete")
    public Object delete(@RequestBody Set<Long> idSet) {
        Result result = voteService.delete(idSet);
        return result;
    }

    @GetMapping
    public Object page(@OpenID String openId,
                       Long activityId,
                       @RequestParam(defaultValue = "1") Integer pageIndex,
                       @RequestParam(defaultValue = "20")Integer pageSize) {
        Result result = voteService.page(openId, activityId, pageIndex, pageSize);
        return result;
    }

    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id) {
        Result result = voteService.getById(id);
        return result;
    }

    @WxAuth
    @GetMapping("/unfinishVote/{activityId}")
    public Object getUnfinishVote(@OpenID String openId,
                                  @PathVariable Long activityId) {
        Result result = voteService.getUnfinishVote(openId, activityId);
        return result;
    }
}
