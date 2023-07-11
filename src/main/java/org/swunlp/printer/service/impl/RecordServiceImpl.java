package org.swunlp.printer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.swunlp.printer.entity.Record;
import org.swunlp.printer.entity.Sharefile;
import org.swunlp.printer.mapper.RecordMapper;
import org.swunlp.printer.service.RecordService;
import org.swunlp.printer.service.SharefileService;
import org.swunlp.printer.constants.RecordState;

import javax.annotation.Resource;
import java.util.Date;

/**
* @author TangXi
* @description 针对表【t_record】的数据库操作Service实现
* @createDate 2023-06-30 13:36:58
*/
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record>
    implements RecordService{

    @Resource
    private SharefileService sharefileService;

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
            }
            return sharefileService.updateById(file);
        }
        return false;
    }
}




