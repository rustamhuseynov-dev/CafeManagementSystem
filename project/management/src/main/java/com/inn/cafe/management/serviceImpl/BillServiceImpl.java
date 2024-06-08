package com.inn.cafe.management.serviceImpl;

import com.inn.cafe.management.constants.CafeConstants;
import com.inn.cafe.management.dao.BillDao;
import com.inn.cafe.management.entity.Bill;
import com.inn.cafe.management.jwt.JwtAuthFilter;
import com.inn.cafe.management.service.BillService;
import com.inn.cafe.management.utils.CafeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillDao billDao;

    private final JwtAuthFilter jwtAuthFilter;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Inside generateReport");
        try {
            String fileName;
            if (validateRequestMap(requestMap)){
                if (requestMap.containsKey("isGenerate") && !(Boolean)requestMap.get("isGenerate")){
                    fileName = (String) requestMap.get("uuid");
                }else {
                    fileName = CafeUtils.getUUID();
                    requestMap.put("uuid",fileName);
                    insertBill(requestMap);
                }

                String data = "Name : " + requestMap.get("name") + "\n" + "Contact Number : " + requestMap.get("contactNumber") +
                        "\n" + "Email : " + requestMap.get("email") + "\n" + "Payment Method : " + requestMap.get("paymentMethod");

                Document document = new Document();
                PdfWriter.getInstance(document,new FileOutputStream(CafeConstants.STORE_LOCATION + "\\" + fileName + ".pdf"));
                document.open();
                setRectangleInPdf(document);

                Paragraph chunk = new Paragraph("Cafe Management System",getFont("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);

                Paragraph paragraph = new Paragraph(data + "\n \n",getFont("Data"));
                document.add(paragraph);

                PdfPTable pTable = new PdfPTable(5);
                pTable.setWidthPercentage(100);
                addTableHeader(pTable);

                JSONArray jsonArray = CafeUtils.getJsonArrayFromString((String) requestMap.get("productDetail"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    addRows(pTable,CafeUtils.getMapFromJson(jsonArray.getString(i)));
                }
                document.add(pTable);

                Paragraph footer = new Paragraph("Total : " + requestMap.get("totalAmount") + "\n"
                        + "Thank you for visiting.Please visit again.!", getFont("Data")
                );
                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}",HttpStatus.OK);

            }
            return CafeUtils.getResponseEntity("Required data not found. " ,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addRows(PdfPTable pTable, Map<String, Object> data) {
        log.info("Inside addRows");
        pTable.addCell((String) data.get("name"));
        pTable.addCell((String) data.get("category"));
        pTable.addCell((String) data.get("quantity"));
        pTable.addCell(Double.toString((Double) data.get("price")));
        pTable.addCell(Double.toString((Double) data.get("total")));
    }

    private void addTableHeader(PdfPTable pTable) {
        log.info("Inside addTableHeader");
        Stream.of("Name","Category","Quantity","Price","Sub Total")
                .forEach(columnTitle ->{
                    PdfPCell cell = new PdfPCell();
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cell.setBorderWidth(2);
                    cell.setPhrase(new Phrase(columnTitle));
                    cell.setBackgroundColor(BaseColor.YELLOW);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    pTable.addCell(cell);
                });
    }

    private Font getFont(String type) {
        log.info("Inside getFont");
        switch (type){
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Inside setRectangleInPdf");
        Rectangle rectangle = new Rectangle(577,825,18,15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBorderWidth(1);
        document.add(rectangle);
    }

    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill = new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal(Integer.parseInt((String)requestMap.get("totalAmount")));
            bill.setProductDetail((String) requestMap.get("productDetail"));
            bill.setCreatedBy(jwtAuthFilter.getCurrentUser());
            billDao.save(bill);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("productDetail") &&
                requestMap.containsKey("totalAmount");
    }
}
