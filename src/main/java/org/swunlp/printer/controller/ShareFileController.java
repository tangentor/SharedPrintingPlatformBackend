package org.swunlp.printer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;
import org.swunlp.printer.entity.Record;
import org.swunlp.printer.entity.Sharefile;
import org.swunlp.printer.result.ResponseResult;
import org.swunlp.printer.service.RecordService;
import org.swunlp.printer.service.SharefileService;
import org.swunlp.printer.state.RecordState;
import org.swunlp.printer.util.RedisUtil;
import org.swunlp.printer.util.UsernameUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shareFiles")
@ResponseResult
public class ShareFileController {

    @Resource
    private SharefileService sharefileService;

    @Resource
    private RecordService recordService;

    @Resource
    private RedisUtil<Sharefile> redisUtil;

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
        // 根据下载次数与打印次数进行加权判断（暂未使用）
        //SELECT DISTINCT md5  FROM `t_record` ORDER BY time DESC
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT md5").orderByDesc("time").last("limit 5");
        //直接使用记录表返回最近操作的
        List<Record> list = recordService.list(wrapper);
        List<Sharefile> res = list.stream().map(v -> detail(v.getMd5()))
                .collect(Collectors.toList());
        System.out.println(res);
        return sharefileService.handleURLtoReal(res);
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
