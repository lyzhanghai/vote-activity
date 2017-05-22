package com.lyl.outsourcing.activity.controller;

import com.lyl.outsourcing.activity.annotation.AdminUserAuth;
import com.lyl.outsourcing.activity.annotation.OpenID;
import com.lyl.outsourcing.activity.annotation.WxAuth;
import com.lyl.outsourcing.activity.dto.Result;
import com.lyl.outsourcing.activity.dto.request.VoteItemSaveReq;
import com.lyl.outsourcing.activity.dto.request.VoteItemUpdateReq;
import com.lyl.outsourcing.activity.service.VoteItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by liyilin on 2017/5/16.
 */
@RestController
@RequestMapping("/voteItem")
public class VoteItemController {
    @Autowired
    private VoteItemService voteItemService;

    @AdminUserAuth
    @PostMapping
    public Object save(@RequestBody @Validated VoteItemSaveReq saveReq) {
        Result result = voteItemService.save(saveReq);
        return result;
    }

    @AdminUserAuth
    @PostMapping("/{id}")
    public Object update(@RequestBody @Validated VoteItemUpdateReq updateReq) {
        Result result = voteItemService.update(updateReq);
        return result;
    }

    @AdminUserAuth
    @PostMapping("/delete")
    public Object delete(@RequestBody Set<Long> idSet) {
        Result result = voteItemService.delete(idSet);
        return result;
    }

    @GetMapping
    public Result page(@RequestParam Long voteId,
                           @RequestParam(defaultValue = "1") Integer pageIndex,
                       @RequestParam(defaultValue = "20") Integer pageSize) {
        Result result = voteItemService.page(voteId, pageIndex, pageSize);
        return result;
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        Result result = voteItemService.getById(id);
        return result;
    }

    @WxAuth
    @PostMapping("/vote/{voteItemId}")
    Result vote(@OpenID String openId,
                @PathVariable Long voteItemId) {
        Result result = voteItemService.vote(openId, voteItemId);
        return result;
    }

    @GetMapping("/statistics/{voteId}")
    Result getStatisticsByVoteId(Long voteId) {
        Result result = voteItemService.getStatisticsByVoteId(voteId);
        return result;
    }
}
