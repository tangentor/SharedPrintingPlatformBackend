package org.swunlp.printer.util;

import java.io.*;

public class IOUtil {


    // 将输出流转换为输入流
    public static InputStream convertOutputStreamToInputStream(OutputStream outputStream) throws IOException {
        // 创建一个临时文件
        File tempFile = File.createTempFile("outputToInput", ".tmp");
        // 将输出流的数据写入临时文件
        outputStream.close();
        InputStream inputStream = new FileInputStream(tempFile);
        // 删除临时文件
        tempFile.delete();
        return inputStream;
    }

    // 将输入流转换为输出流
    public static OutputStream convertInputStreamToOutputStream(InputStream inputStream) throws IOException {
        // 创建一个临时文件
        File tempFile = File.createTempFile("inputToOutput", ".tmp");
        // 将输入流的数据写入临时文件
        OutputStream outputStream = new FileOutputStream(tempFile);
        int readData;
        while ((readData = inputStream.read()) != -1) {
            outputStream.write(readData);
        }

        inputStream.close();
        outputStream.close();

        // 删除临时文件
        tempFile.delete();

        return new FileOutputStream(tempFile);
    }
}
