package org.swunlp.printer.service;

import org.swunlp.printer.entity.PrintServiceInfo;

import java.util.List;

public interface PrintServiceService {
    String register(PrintServiceInfo printServiceInfo);

    List<PrintServiceInfo> listAll();

    PrintServiceInfo findById(String serviceId);
}
