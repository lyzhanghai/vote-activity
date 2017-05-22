package com.lyl.outsourcing.activity.controller;

import com.lyl.outsourcing.activity.annotation.AdminUserAuth;
import com.lyl.outsourcing.activity.annotation.OpenID;
import com.lyl.outsourcing.activity.annotation.WxAuth;
import com.lyl.outsourcing.activity.dto.Result;
import com.lyl.outsourcing.activity.dto.SaveGroup;
import com.lyl.outsourcing.activity.dto.UpdateGroup;
import com.lyl.outsourcing.activity.dto.request.RaffleItemForm;
import com.lyl.outsourcing.activity.service.RaffleItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by liyilin on 2017/5/16.
 */
@RestController
@RequestMapping("/raffleItem")
public class RaffleItemController {
    @Autowired
    private RaffleItemService raffleItemService;

    @AdminUserAuth
    @PostMapping
    public Object save(@RequestBody @Validated(SaveGroup.class) RaffleItemForm saveForm) {
        Result result = raffleItemService.save(saveForm);
        return result;
    }

    @AdminUserAuth
    @PostMapping("/{id}")
    public Object update(@RequestBody @Validated(UpdateGroup.class) RaffleItemForm updateForm) {
        Result result = raffleItemService.update(updateForm);
        return result;
    }

    @AdminUserAuth
    @PostMapping("/delete")
    public Object delete(@RequestBody Set<Long> idSet) {
        Result result = raffleItemService.delete(idSet);
        return result;
    }


    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id) {
        Result result = raffleItemService.getById(id);
        return result;
    }

    @GetMapping("/raffleId/{raffleId}")
    public Object getByRaffleId(@PathVariable Long raffleId) {
        Result result = raffleItemService.getByRaffleId(raffleId);
        return result;
    }

    @WxAuth
    @GetMapping("/raffleRecord/{raffleId}")
    public Object getRaffleRecord(@OpenID String openId,
                                  @PathVariable Long raffleId) {
        Result result = raffleItemService.getRaffleRecord(openId, raffleId);
        return result;
    }

    @WxAuth
    @PostMapping("/raffle/{raffleId}")
    public Object raffleByRaffleId(@OpenID String openId,
                                   @PathVariable Long raffleId) {
        Result result = raffleItemService.raffleByRaffleId(openId, raffleId);
        return result;
    }

    @WxAuth
    @PostMapping("/achieve")
    public Object achieveRaffleItem(@OpenID String openId,
                                    @RequestParam Long raffleId) {
        Result result = raffleItemService.achieveRaffleItem(openId, raffleId);
        return result;
    }
}
