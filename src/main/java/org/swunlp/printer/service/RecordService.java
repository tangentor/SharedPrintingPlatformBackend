package org.swunlp.printer.service;

import org.swunlp.printer.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import org.swunlp.printer.constants.RecordState;
import org.swunlp.printer.entity.Sharefile;

import java.util.List;

/**
* @author TangXi
* @description 针对表【t_record】的数据库操作Service
* @createDate 2023-06-30 13:36:58
*/
public interface RecordService extends IService<Record> {

    boolean add(String documentId, String loginUser, RecordState recordState);

    List<Sharefile> popular();
}
