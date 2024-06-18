package com.inn.cafe.management.rest;

import com.inn.cafe.management.constants.CafeConstants;
import com.inn.cafe.management.entity.Bill;
import com.inn.cafe.management.service.BillService;
import com.inn.cafe.management.utils.CafeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping(path = "/get-all")
    public ResponseEntity<List<Bill>> getBills(){
        try {
            return billService.getBills();
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    @PostMapping(path = "/get-pdf")
    public ResponseEntity<byte[]> getPdf(@RequestBody Map<String, Object> requestMap){
        try {
            return billService.getPdf(requestMap);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @PostMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable Integer id){
        try {
            return billService.deleteBill(id);
        }
        catch (Exception ex ){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
