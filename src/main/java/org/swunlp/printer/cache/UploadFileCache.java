package org.swunlp.printer.cache;

import org.springframework.stereotype.Component;
import org.swunlp.printer.entity.UploadFile;

@Component
public class UploadFileCache extends BaseCache<UploadFile>{

    @Override
    protected String domain() {
        return "uploadFile";
    }


}
