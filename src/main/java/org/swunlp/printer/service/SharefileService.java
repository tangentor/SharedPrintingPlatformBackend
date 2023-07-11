package org.swunlp.printer.service;

import org.swunlp.printer.entity.Sharefile;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author TangXi
* @description 针对表【t_sharefile】的数据库操作Service
* @createDate 2023-06-28 21:03:30
*/
public interface SharefileService extends IService<Sharefile> {

    boolean newRecord(Sharefile sharefile);

    Sharefile detail(String md5);

    List<Sharefile> search(String searchKey);

    Sharefile getByMd5(String documentId);

    List<Sharefile> handleURLtoReal(List<Sharefile> res);
}
