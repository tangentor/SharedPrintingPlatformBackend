package org.swunlp.printer.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import org.swunlp.printer.cache.UploadFileCache;
import org.swunlp.printer.constants.RecordState;
import org.swunlp.printer.entity.PrintInfo;
import org.swunlp.printer.entity.PrintServiceInfo;
import org.swunlp.printer.entity.UploadFile;
import org.swunlp.printer.service.PrintService;
import org.swunlp.printer.service.PrintServiceService;
import org.swunlp.printer.service.RecordService;
import org.swunlp.printer.util.JSONUtil;
import org.swunlp.printer.util.TokenUtil;
import org.swunlp.printer.util.UsernameUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class PrintServiceImpl implements PrintService {


    private final UploadFileCache uploadFileCache;

    private final RecordService recordService;

    private final PrintServiceService printServiceService;

    public PrintServiceImpl(UploadFileCache uploadFileCache, RecordService recordService, PrintServiceService printServiceService) {
        this.uploadFileCache = uploadFileCache;
        this.recordService = recordService;
        this.printServiceService = printServiceService;
    }

    @Override
    public Object print(PrintInfo printInfo) {
        //查找对应的设备
        String serviceId = printInfo.getService();
        PrintServiceInfo info = printServiceService.findById(serviceId);
        printInfo.setPrintServiceInfo(info);
        //设置PDF文件URL
        UploadFile uploadFile = uploadFileCache.get(printInfo.getDocumentId());
        String encodedUrl = UriUtils.encodePath(uploadFile.getPdfUrl(), "UTF-8");
        printInfo.setFileUrl(encodedUrl);
        //断言文件的URL不为空
        assert printInfo.getFileUrl() != null;
        //根据设备的IP进行对应的请求并附带打印信息
        Object o = sendPrintDeviceByIpAndPort(info.getIp(), info.getPort(), printInfo);
        //所在文件更新，打印记录增加
        recordService.add(printInfo.getDocumentId(), UsernameUtil.getLoginUser(), RecordState.PRINT);
        return o;
    }


    private Object sendPrintDeviceByIpAndPort(String ip, int port, PrintInfo printInfo) {
        HttpClient client = HttpClient.newHttpClient();
        //新建一个POST请求包,带上token字段
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://" + ip + ":" + port + "/print"))
                .POST(HttpRequest.BodyPublishers.ofString(JSONUtil.toJSON(printInfo)))
                .setHeader("content-type", "application/json")
                .setHeader("token", TokenUtil.getToken())
                .build();
        //给对应的设备发送数据包
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
