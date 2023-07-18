package org.swunlp.printer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.swunlp.printer.constants.RecordState;
import org.swunlp.printer.entity.Record;
import org.swunlp.printer.entity.Sharefile;
import org.swunlp.printer.mapper.RecordMapper;
import org.swunlp.printer.service.RecordService;
import org.swunlp.printer.service.SharefileService;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author TangXi
* @description 针对表【t_record】的数据库操作Service实现
* @createDate 2023-06-30 13:36:58
*/
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record>
    implements RecordService{

    private final SharefileService sharefileService;

    public RecordServiceImpl(SharefileService sharefileService) {
        this.sharefileService = sharefileService;
    }

    @Override
    @Transactional
    public boolean add(String documentId, String user, RecordState recordState) {
        Record record = new Record();
        record.setMd5(documentId);
        record.setTime(new Date());
        record.setUid(user);
        record.setType(recordState.value());
        boolean save = save(record);
        if(save){
            //对应的文件基本信息更新
            Sharefile file = sharefileService.getByMd5(documentId);
            if (file!=null){
                if(recordState.equals(RecordState.PRINT)){
                    file.setPrintingTimes(file.getPrintingTimes()+1);
                } else {
                    file.setDownloadTimes(file.getDownloadTimes()+1);
                }
                return sharefileService.updateById(file);
            }
        }
        return false;
    }

    @Override
    public List<Sharefile> popular() {
        // 根据下载次数与打印次数进行加权判断（暂未使用）
        //SELECT DISTINCT md5  FROM `t_record` ORDER BY time DESC
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT md5").orderByDesc("time");
        //直接使用记录表返回最近操作的
        List<Record> list = list(wrapper);
        List<Sharefile> res = list.stream().map(v -> sharefileService.detail(v.getMd5()))
                .filter(Objects::nonNull)
                .limit(6)
                .collect(Collectors.toList());
//        System.out.println(res);
        return res;
    }
}




