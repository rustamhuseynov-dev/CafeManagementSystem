package com.inn.cafe.management.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface BillService {

    ResponseEntity<String> generateReport(Map<String, Object> requestMap);
}
