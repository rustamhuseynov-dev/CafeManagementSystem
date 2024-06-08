package com.inn.cafe.management.rest;

import com.inn.cafe.management.constants.CafeConstants;
import com.inn.cafe.management.service.BillService;
import com.inn.cafe.management.utils.CafeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/bill")
@RequiredArgsConstructor
public class BillRestController {

    private final BillService billService;

    @PostMapping(path = "/generate-report")
    public ResponseEntity<String> generateReport(@RequestBody Map<String ,Object> requestMap){
        try {
            return billService.generateReport(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
