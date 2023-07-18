package org.swunlp.printer.cache;

import org.springframework.stereotype.Component;
import org.swunlp.printer.entity.Sharefile;

@Component
public class ShareFileCache extends BaseCache<Sharefile> {
    @Override
    protected String domain() {
        return "shareFile";
    }
}
