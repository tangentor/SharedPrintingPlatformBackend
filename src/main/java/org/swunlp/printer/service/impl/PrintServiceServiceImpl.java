package org.swunlp.printer.service.impl;

import org.springframework.stereotype.Service;
import org.swunlp.printer.entity.PrintServiceInfo;
import org.swunlp.printer.service.PrintServiceService;
import org.swunlp.printer.util.RedisUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PrintServiceServiceImpl implements PrintServiceService {

    @Resource
    private RedisUtil<PrintServiceInfo> redisUtil;

    private String key = "sharePrinter:services:";

    @Override
    public String register(PrintServiceInfo printServiceInfo) {
        //检查是否有ID
        String id = printServiceInfo.getId();
        if(id == null){
            id = UUID.randomUUID().toString().replace("-","");
        }
        printServiceInfo.setId(id);
        //直接放入到redis中，默认有效时间1分钟
        boolean b = redisUtil.set(key+id, printServiceInfo, 60);
        if(b){
            return id;
        }
        return "注册失败";
    }

    @Override
    public List<PrintServiceInfo> listAll() {
        List<PrintServiceInfo> collect = redisUtil.keys(key + "*").stream()
                .map(key -> redisUtil.get(key))
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public PrintServiceInfo findById(String serviceId) {
        return redisUtil.get(key+serviceId);
    }
}
