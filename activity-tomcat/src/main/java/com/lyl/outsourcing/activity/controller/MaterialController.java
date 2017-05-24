package com.lyl.outsourcing.activity.controller;

import com.lyl.outsourcing.activity.annotation.AdminUserAuth;
import com.lyl.outsourcing.activity.dto.Result;
import com.lyl.outsourcing.activity.dto.request.MaterialSaveReq;
import com.lyl.outsourcing.activity.dto.request.MaterialUpdateReq;
import com.lyl.outsourcing.activity.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

/**
 * Created by liyilin on 2017/5/2.
 */
@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    @AdminUserAuth
    @PostMapping
    public Object save(@Validated MaterialSaveReq saveReq, @RequestParam("file") MultipartFile file) throws IOException {
        Result result = materialService.save(saveReq, file);
        return result;
    }

    @AdminUserAuth
    @PostMapping("/{id}")
    public Object update(@Validated @RequestBody MaterialUpdateReq updateReq) {
        Result result = materialService.update(updateReq);
        return result;
    }

    @AdminUserAuth
    @PostMapping("/delete")
    public Object delete(@RequestBody Set<String> idSet) {
        Result result = materialService.delete(idSet);
        return result;
    }

    @GetMapping
    public Object page(
            String type,
            @RequestParam(defaultValue = "1") Integer pageIndex,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        Result result = materialService.page(type, pageIndex, pageSize);
        return result;
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable String id) {
        Result result = materialService.getById(id);
        return result;
    }
}
