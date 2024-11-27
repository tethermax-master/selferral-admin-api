package io.selferral.admin.api.service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.selferral.admin.api.core.CD.ErrorCode;
import io.selferral.admin.api.core.exception.TetherMaxException;
import io.selferral.admin.api.mapper.UidEnrollMapper;
import io.selferral.admin.api.model.dto.UidEnrollList;
import io.selferral.admin.api.model.entity.UidEnroll;
import io.selferral.admin.api.model.request.UidEnrollListRequest;
import io.selferral.admin.api.model.request.UpdateUidEnrollRequest;
import io.selferral.admin.api.model.response.UidEnrollListResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UidEnrollServiceImpl implements UidEnrollService{
	
	@Autowired
	UidEnrollMapper uidEnrollMapper;
	
	@Override
	public UidEnrollListResponse getUidEnrollList(UidEnrollListRequest req) throws Exception {
		UidEnrollListResponse res = new UidEnrollListResponse();
		res.setTotalCount(uidEnrollMapper.getUidEnrollListCount(req));
		res.setUidEnrollList(uidEnrollMapper.getUidEnrollList(req));
		return res;
	}
	
	@Override
	@Transactional
	public boolean updateUidEnroll(UpdateUidEnrollRequest req) throws Exception {
		if(!uidEnrollMapper.isProccessingUidEnroll(req.enrollNo())) {
			throw new TetherMaxException(ErrorCode.NOT_PROCCESSING_STATUS);
		}
		boolean result = uidEnrollMapper.updateUidEnroll(UidEnroll.builder().build().update(req));
		return result;
	}
	
	@Override
	public void uidEnrollListExcelDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<UidEnrollList> list = uidEnrollMapper.getUidListExcelDownload();
		
		if(list == null) {
			list = new ArrayList<UidEnrollList>();
		}
		
		String sCurTime = new SimpleDateFormat("yyyyMMdd",Locale.KOREA).format(new Date());		
		String excelName =  "UID_" +sCurTime+ ".xls";
		
		String userAgent = request.getHeader("User-Agent");
		if(userAgent.indexOf("MSIE") > -1){
			excelName = URLEncoder.encode(excelName,"UTF-8");
		}else{
			excelName = new String(excelName.getBytes("UTF-8"), "UTF-8");
		}
		
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("UID 리스트");
		Row row = null;
		Cell cell = null;
		int rowNo = 0;
		
		//테이블 헤더용 스타일
		CellStyle headStyle = wb.createCellStyle(); //셀스타일을 위한변수
	    
		//가는 경계선
//		headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
//	    headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//	    headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//	    headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//	    
//	    // 배경색은 노란색
//	    headStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
//	    headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
//	
//	    // 데이터는 가운데 정렬합니다.
//	    headStyle.setAlignment(CellStyle.ALIGN_CENTER);
		
	    // 데이터용 경계 스타일 테두리만 지정
	    CellStyle bodyStyle = wb.createCellStyle();
//	    bodyStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
//	    bodyStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//	    bodyStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//	    bodyStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//	    bodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
	    
	    int listSize = 3 ; // 신청 합계 추가
		//컬럼길이설정
		int columnIndex = 0;
		while (columnIndex <= listSize){
			sheet.setColumnWidth(columnIndex, 2000);
			columnIndex++;
		}
		//헤더 생성
		row = sheet.createRow(rowNo++);
		for(int i = 0; i <= listSize; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyle);
			
			switch (i) {
				case 0: cell.setCellValue("날짜");
						break;
				case 1: cell.setCellValue("거래소명");
						break;
				case 2: cell.setCellValue("UID");
						break;		
				default: cell.setCellValue("");
						break;
			}
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
		ZoneId zoneId = ZoneId.of("Asia/Seoul");
		//각 해당하는 셀에 값과 스타일을 넣음
		for(UidEnrollList uid : list){
			row = sheet.createRow(rowNo++);
			
			for(int j = 0; j <= listSize; j++) {
				cell = row.createCell(j);
				cell.setCellStyle(bodyStyle);
				
				switch (j) {
				case 0: cell.setCellValue(ZonedDateTime.of(uid.getUpdateDt().toLocalDateTime(), zoneId).format(formatter));
						break;
				case 1: cell.setCellValue(uid.getExchangeName());
						break;
				case 2: cell.setCellValue(uid.getUid().toString());
						break;		
				default: cell.setCellValue("");
						break;
					
				}
			}
		}
		
		response.setContentType("ms-vnd/excel");
		response.setHeader("Content-Disposition", "attachement; filename=\"" + URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
		
	    wb.write(response.getOutputStream());
	    wb.close();
	    response.getOutputStream().close();
		
	}
}
