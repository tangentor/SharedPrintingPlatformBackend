package org.swunlp.printer.service.impl;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.swunlp.printer.entity.PrintInfo;
import org.swunlp.printer.entity.PrintServiceInfo;
import org.swunlp.printer.entity.Sharefile;
import org.swunlp.printer.other.RedisKey;
import org.swunlp.printer.service.PDFParingService;
import org.swunlp.printer.service.PrintServiceService;
import org.swunlp.printer.service.RecordService;
import org.swunlp.printer.state.RecordState;
import org.swunlp.printer.util.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PDFParingServiceImpl implements PDFParingService {

    /**
     * 经过测试,dpi为96,100,105,120,150,200中,105显示效果较为清晰,体积稳定,dpi越高图片体积越大,一般电脑显示分辨率为96
     */
    public static final float DEFAULT_DPI = 150;

    /**
     * 默认转换的图片格式为jpg
     */
    public static final String DEFAULT_FORMAT = "jpg";


    @Resource
    private RedisUtil<String> redisUtil;
    @Resource
    private RedisUtil<Sharefile> redisUtil4share;

    @Resource
    private PrintServiceService printServiceService;

    @Resource
    private RecordService recordService;

    @Value("${server.port}")
    private int port;



    /**
     * 处理PDF，将pdf转换成图片,同时对PDF文件以及生成的图片进行保存
     * add:增加对文件基本信息的保存
     * @param file
     * @return 返回该PDF的MD5码，后面的查询当做ID使用
     */
    @Override
    public Object parse(MultipartFile file) {
        String md5;
        try {
            md5 = DigestUtils.md5DigestAsHex(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("解析文件时出错");
        }
        //检查是否有图片缓存
        String key = RedisKey.prefix + ":file:"+md5+":image";
        if(redisUtil.hasKey(key)){
            return md5;
        }
        //没有缓存就去生成相应的图片
        try(PDDocument doc = Loader.loadPDF(file.getInputStream());) {
            //读取pdf文件
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            List<String> stringList = new ArrayList<>(pageCount);
            BufferedImage image;
            for (int i = 0; i < pageCount; i++) {
                // Windows native DPI
                image = renderer.renderImageWithDPI(i, DEFAULT_DPI);
                // BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
                String uid = "pic_"+i+"."+DEFAULT_FORMAT;
                //保存处理之后的图片
                String path = TempPathUtil.generateTempPath(uid,file.getOriginalFilename());
                ImageIO.write(image, DEFAULT_FORMAT, new File(path));
                //处理一下图片链接
                String newPath = path.replace(TempPathUtil.getStorePath(),"").replace("\\","/");
                stringList.add(newPath);
            }
            //PDF文件保存一份
            String pdfPath = TempPathUtil.generateTempPath(file.getOriginalFilename(),file.getOriginalFilename());
            IOUtils.copy(file.getInputStream(),new FileOutputStream(pdfPath));
            //生成图片写入缓存
            redisUtil.lSet(key,stringList);
            //文件链接写入redis
            String key2 = RedisKey.prefix + ":file:"+md5+":origin";
            redisUtil.set(key2,pdfPath);
            //检查是否有文件基本信息缓存
            key = RedisKey.prefix + ":file:"+md5+":base";
            if(!redisUtil4share.hasKey(key)){
                Sharefile sharefile = new Sharefile();
                sharefile.setName(file.getOriginalFilename());
                sharefile.setMd5(md5);
                sharefile.setThumbnail(stringList.get(0));
                redisUtil4share.set(key,sharefile);
            }
            return md5;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> getPreviews(String md5) {
        HashMap<String, Object> res = new HashMap<>();
        //PDF处理之后的图片
        String key = RedisKey.prefix + ":file:"+md5+":image";
        if(redisUtil.hasKey(key)){
            List<String> urls = redisUtil.lGet(key, 0, -1);
            //处理url
            List<String> after = urls.stream()
                    .map(v -> "http://"+IPUtil.getLocalIpAddress()+":"+port+"/files" + v)
                    .collect(Collectors.toList());
            res.put("preview",after);
        }
        //文件的基本信息
        key = RedisKey.prefix + ":file:"+md5+":base";
        if(redisUtil4share.hasKey(key)){
            Sharefile sharefile = redisUtil4share.get(key);
            //处理源文件url
            String url = convertLocalFileToUrl(md5);
            sharefile.setUrl(url);
            sharefile.setThumbnail("http://$ip:"+port+"/files"+sharefile.getThumbnail());
            res.put("base",sharefile);
        }
        return res;
    }

    @Override
    public Object print(PrintInfo printInfo) {
        //查找对应的设备
        String serviceId = printInfo.getService();
        PrintServiceInfo info = printServiceService.findById(serviceId);
        printInfo.setPrintServiceInfo(info);
        //填写对应的文件URL
        String realPath = convertLocalFileToUrl(printInfo.getDocumentId(),IPUtil.getLocalIpAddress(info.getIp()));
        printInfo.setFileUrl(realPath);
        //根据设备的IP进行对应的请求并附带打印信息
        Object o = sendPrintDeviceByIpAndPort(info.getIp(), info.getPort(), printInfo);
        //所在文件更新，打印记录增加
        recordService.add(printInfo.getDocumentId(),UsernameUtil.getLoginUser(), RecordState.PRINT);
        return o;
    }

    private Object sendPrintDeviceByIpAndPort(String ip, int port, PrintInfo printInfo) {
        HttpClient client = HttpClient.newHttpClient();
        //新建一个POST请求包,带上token字段
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://" + ip + ":" + port+"/print"))
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

    private String convertLocalFileToUrl(String id,String ip){
        //填写对应的文件URL
        String s = convertLocalFileToUrl(id);
        return s.replace("$ip",ip);
    }

    private String convertLocalFileToUrl(String id){
        //填写对应的文件URL
        String key = RedisKey.prefix + ":file:"+id+":origin";
        String localPath = redisUtil.get(key);
        //处理本地路径为网络URL
        String URI = localPath.replace("\\", "/").replaceAll(".*/shareFiles", "");
        String realPath;
//        System.out.println(realPath);
        //URL进行Encode
        try {
            realPath = "http://$ip:"+port+"/files" + URLEncoder.encode(URI,"UTF-8");
            realPath = realPath.replace("%2F","/");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URL转码失败");
        }
        return realPath;
    }
}
