package org.swunlp.printer.test;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import org.swunlp.printer.util.IPUtil;
import org.swunlp.printer.util.TempPathUtil;

import javax.print.PrintService;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Test {
    public static void main(String[] args) {
        System.out.println("本机IP:" + getIpAddress());
    }

    public static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();

                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                } else {
                    for (InterfaceAddress interfaceAddress : netInterface.getInterfaceAddresses()) {
                        InetAddress address = interfaceAddress.getAddress();
                        if(address instanceof Inet4Address){
                            System.out.println(IPUtil.isInRange("192.168.0.88",address.getHostAddress()+"/"+interfaceAddress.getNetworkPrefixLength()));
//                            System.out.println(interfaceAddress.getNetworkPrefixLength());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("IP地址获取失败" + e.toString());
        }
        return "";
    }

    public static void setPageStyle(PDDocument document, PrinterJob job) {
        job.setPageable(new PDFPageable(document));
        Paper paper = new Paper();
        int width = 215;
        int height = 170;
        // 设置打印纸张大小
        paper.setSize(width,height); // 1/72 inch
        // 设置边距，单位是像素，10mm边距，对应 28px
        int marginLeft = 1;
        int marginRight = 0;
        int marginTop = 10;
        int marginBottom = 0;
        // 设置打印位置 坐标
        paper.setImageableArea(marginLeft, marginRight, width - (marginLeft + marginRight), height - (marginTop + marginBottom));
        // custom page format
        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);
        // override the page format
        Book book = new Book();
        // append all pages 设置一些属性 是否缩放 打印张数等
        book.append(new PDFPrintable(document, Scaling.ACTUAL_SIZE), pageFormat, 1);
        job.setPageable(book);
    }

    public static PDDocument printPdf(String pdfPath, String printerName) throws Exception {
        File file = new File(pdfPath);
        PDDocument document = Loader.loadPDF(file);
        PrinterJob job = getPrintServiceByName(printerName);
        setPageStyle(document, job);
        // 开始打印
        job.print();
        return document;
    }

    public static PrinterJob getPrintServiceByName(String printerName) throws Exception{
        PrinterJob job = PrinterJob.getPrinterJob();
        // 遍历查询打印机名称
        boolean flag = false;
        for (PrintService ps : PrinterJob.lookupPrintServices()) {
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
