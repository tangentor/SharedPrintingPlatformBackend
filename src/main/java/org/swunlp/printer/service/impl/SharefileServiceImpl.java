package org.swunlp.printer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.swunlp.printer.entity.Sharefile;
import org.swunlp.printer.mapper.SharefileMapper;
import org.swunlp.printer.mapper.UserMapper;
import org.swunlp.printer.other.RedisKey;
import org.swunlp.printer.service.SharefileService;
import org.swunlp.printer.util.RedisUtil;
import org.swunlp.printer.util.UsernameUtil;

import javax.annotation.Resource;
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

    @Resource
    private RedisUtil<Sharefile> redisUtil;

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean newRecord(Sharefile sharefile) {
        Sharefile exist = detail(sharefile.getMd5());
        //检查是否上传过同样的文件
        if(exist != null){
            throw new RuntimeException("该文件已被上传："+exist.getName());
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
        String key = RedisKey.prefix + ":file:"+sharefile.getMd5()+":base";
        redisUtil.set(key,sharefile);
        return isSaved;
    }

    @Override
    public Sharefile detail(String md5) {
        LambdaQueryWrapper<Sharefile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Sharefile::getMd5,md5);
        return getOne(wrapper);
    }

    @Override
    public List<Sharefile> search(String searchKey) {
        String key = RedisKey.prefix + ":searchKey:"+searchKey;
        // 先查缓存
        if(redisUtil.hasKey(key)){
            return redisUtil.lGet(key,0,-1);
        }
        // 设置上传时间
        LambdaQueryWrapper<Sharefile> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Sharefile::getDesc, searchKey)
                .or()
                .like(Sharefile::getName, searchKey);
        List<Sharefile> list = list(wrapper);
        //处理显示的URL
//        list = handleURLtoReal(list);
        if(list.size()>0)
            redisUtil.lSet(key,list,60);
        return list;
    }


    @Override
    public Sharefile getByMd5(String documentId) {
        return detail(documentId);
    }
}




