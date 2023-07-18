package org.swunlp.printer.controller;

import org.springframework.web.bind.annotation.*;
import org.swunlp.printer.annotation.ResponseResult;
import org.swunlp.printer.constants.RecordState;
import org.swunlp.printer.entity.Sharefile;
import org.swunlp.printer.service.RecordService;
import org.swunlp.printer.service.SharefileService;
import org.swunlp.printer.util.UsernameUtil;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/shareFiles")
@ResponseResult
public class ShareFileController {

    private final SharefileService sharefileService;

    private final RecordService recordService;

    public ShareFileController(SharefileService sharefileService, RecordService recordService) {
        this.sharefileService = sharefileService;
        this.recordService = recordService;
    }


    /**
     * 将上传的文件进行共享
     * @return
     */
    @PostMapping("/add")
    public boolean add(@RequestBody Sharefile sharefile) {
        return sharefileService.newRecord(sharefile);
    }

    @GetMapping("/detail/{md5}")
    public Sharefile detail(@PathVariable String md5) {
        return sharefileService.detail(md5);
    }

    /**
     * 搜索相关的文件
     * @return
     */
    @GetMapping("/{searchKey}")
    public List<Sharefile> search(@PathVariable String searchKey) {
        return sharefileService.search(searchKey);
    }

    /**
     * 搜索相关的文件
     * @return
     */
    @GetMapping("/popular")
    public List<Sharefile> popular() {
        return recordService.popular();
    }

    @GetMapping("/download/{md5}")
    public boolean download(@PathVariable String md5) {
        //记录增加
        return recordService.add(md5,UsernameUtil.getLoginUser(), RecordState.DOWNLOAD);
    }

    @PostMapping("/update")
    public boolean update(@RequestBody Sharefile sharefile){
        //获取当前请求的用户
        String uid = UsernameUtil.getLoginUser();
        if(Objects.equals(sharefile,uid)){
            return sharefileService.updateById(sharefile);
        } else {
            throw new RuntimeException("非法操作：修改他人文件信息");
        }
    }
}
