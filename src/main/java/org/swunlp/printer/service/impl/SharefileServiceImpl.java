package org.swunlp.printer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.swunlp.printer.cache.ShareFileCache;
import org.swunlp.printer.cache.UploadFileCache;
import org.swunlp.printer.entity.Sharefile;
import org.swunlp.printer.entity.UploadFile;
import org.swunlp.printer.mapper.SharefileMapper;
import org.swunlp.printer.service.SharefileService;
import org.swunlp.printer.service.UserService;
import org.swunlp.printer.util.UsernameUtil;

import java.util.Date;
import java.util.List;

/**
* @author TangXi
* @description 针对表【t_sharefile】的数据库操作Service实现
* @createDate 2023-06-28 21:03:30
*/
@Service
public class SharefileServiceImpl extends ServiceImpl<SharefileMapper, Sharefile>
    implements SharefileService{

    private final UploadFileCache uploadFileCache;

    private final ShareFileCache shareFileCache;

    private final UserService userService;

    public SharefileServiceImpl(UploadFileCache uploadFileCache, ShareFileCache shareFileCache, UserService userService) {
        this.uploadFileCache = uploadFileCache;
        this.shareFileCache = shareFileCache;
        this.userService = userService;
    }


    @Override
    public boolean newRecord(Sharefile sharefile) {
        Sharefile exist = detail(sharefile.getMd5());
        //检查是否上传过同样的文件
        if(exist != null){
            throw new RuntimeException("该文件已被分享："+exist.getName());
        }
        // 设置上传时间
        sharefile.setUploadTime(new Date());
        // 默认上传打印为0
        sharefile.setDownloadTimes(0);
        sharefile.setPrintingTimes(0);
        // 上传者为当前用户
        sharefile.setUid(UsernameUtil.getLoginUser());
        boolean isSaved = save(sharefile);
        //更新一个文件的基本信息
        UploadFile uploadFile = uploadFileCache.get(sharefile.getMd5());
        //设置为已分享
        uploadFile.setShared(true);
        uploadFileCache.set( sharefile.getMd5(),uploadFile);
        return isSaved;
    }

    @Override
    public Sharefile detail(String md5) {
        //查缓存
        boolean exist = shareFileCache.exist(md5);
        if(exist){
            return shareFileCache.get(md5);
        }
        LambdaQueryWrapper<Sharefile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Sharefile::getMd5,md5);
        Sharefile one = getOne(wrapper);
        shareFileCache.set(md5,one,60);
        return one;
    }

    @Override
    public List<Sharefile> search(String searchKey) {
        String key = "searchKey:"+searchKey;
        // 先查缓存
        if(shareFileCache.exist(key)){
            return shareFileCache.lGetAll(key);
        }
        // 设置上传时间
        LambdaQueryWrapper<Sharefile> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Sharefile::getDesc, searchKey)
                .or()
                .like(Sharefile::getName, searchKey);
        List<Sharefile> list = list(wrapper);
        //处理显示用户
        list.forEach(
                item->{
                    item.setUsername(
                            userService.detail(item.getUid()).getNickname());
                }
        );
        if(list.size()>0)
            shareFileCache.lSet(key,list,60);
        return list;
    }


    @Override
    public Sharefile getByMd5(String documentId) {
        return detail(documentId);
    }

}




