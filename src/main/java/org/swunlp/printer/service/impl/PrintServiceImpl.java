package org.swunlp.printer.service.impl;

import lombok.SneakyThrows;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import org.springframework.stereotype.Service;
import org.swunlp.printer.entity.PrintInfo;
import org.swunlp.printer.entity.PrintServiceInfo;
import org.swunlp.printer.service.PrintService;
import org.swunlp.printer.util.RedisUtil;

import javax.annotation.Resource;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrintServiceImpl implements PrintService {


    @Resource
    private RedisUtil<String> redisUtil;

    @Override
    public Object print(PrintInfo printInfo) {
        //查找对应的设备
        PrintServiceInfo info = printInfo.getPrintServiceInfo();
        //加载设备
        PrinterJob job = null;
        try {
            job = getPrintServiceByName(info.getName());
            //加载文档
            String key = "sharePrinter:file:"+printInfo.getDocumentId()+":origin";
            String realPath = redisUtil.get(key);
            File file = new File(realPath);
            PDDocument document = Loader.loadPDF(file);
            //文档处理
            document = handleDocument(document,printInfo.getPages(),printInfo.getNum());
//            System.out.println(document.getNumberOfPages());
            //设置打印参数
            setPageStyle(document, job,document.getNumberOfPages());
            // 开始打印
            job.print();
        } catch (PrinterException e) {
            return "打印失败："+info.getName()+"未找到";
        } catch (IOException e) {
            return "打印失败："+e.getMessage();
        }
        return "任务执行成功";
    }

    @SneakyThrows
    private PDDocument handleDocument(PDDocument document, List<Integer> pages,int num) {
        //获取页码对应的Page
        ArrayList<PDPage> pdPages = new ArrayList<>();
        //需要打印的页面
        for(int index:pages){
            pdPages.add(document.getPage(index-1));
        }
        int numberOfPages = document.getNumberOfPages();
        //页面全部删除
        for(int i=0;i<numberOfPages;i++){
            document.removePage(0);
        }
        //添加上我们需要打印的页面
        //打印份数
        for(int i=0;i<num;i++)
            for(PDPage page:pdPages){
                document.addPage(page);
            }
        //输出一下PDF
//        document.save("test.pdf");
        return document;
    }

    //直接设置A4格式
    public static void setPageStyle(PDDocument document, PrinterJob job,int num) {
        job.setPageable(new PDFPageable(document));
        Paper paper = new Paper();
        int width = 595;
        int height = 842;
        // 设置打印纸张大小
        paper.setSize(width,height);
        int marginLeft = 0;
        int marginRight = 0;
        int marginTop = 0;
        int marginBottom = 0;
        // 设置打印位置 坐标
        paper.setImageableArea(marginLeft, marginRight, width - (marginLeft + marginRight), height - (marginTop + marginBottom));
        // custom page format
        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);
        // override the page format
        Book book = new Book();
        // append all pages 设置一些属性 是否缩放 打印张数等
        book.append(new PDFPrintable(document, Scaling.ACTUAL_SIZE), pageFormat, num);
        job.setPageable(book);
    }

    public static PrinterJob getPrintServiceByName(String printerName) throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        // 遍历查询打印机名称
        boolean flag = false;
        for (javax.print.PrintService ps : PrinterJob.lookupPrintServices()) {
            String psName = ps.toString();
//            System.out.println(psName.contains("Hewlett-Packard HP LaserJet Pro MFP M126a"));
            // 选用指定打印机，需要精确查询打印机就用equals，模糊查询用contains
            if (psName.contains(printerName)) {
                flag = true;
                job.setPrintService(ps);
                break;
            }
        }
        if(!flag){
            throw new RuntimeException("打印失败，未找到名称为" + printerName + "的打印机，请检查。");
        }
        return job;
    }
}
