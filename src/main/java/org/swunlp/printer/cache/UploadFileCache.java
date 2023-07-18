package org.swunlp.printer.cache;

import org.springframework.stereotype.Component;
import org.swunlp.printer.entity.UploadFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class UploadFileCache extends BaseCache<UploadFile>{

    @Override
    protected String domain() {
        return "uploadFile";
    }


    public List<UploadFile> list(String patten) {
        String newKey = getKey() + (patten == null ? "" : ":"+patten);
        ArrayList<UploadFile> res = new ArrayList<>();
        Iterator<String> iterator = redisUtil.keys(newKey).iterator();
        while(iterator.hasNext()){
            res.add(redisUtil.get(iterator.next()));
        }
        return res;
    }
}
