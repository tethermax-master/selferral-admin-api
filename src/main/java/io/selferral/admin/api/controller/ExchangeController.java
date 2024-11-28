package io.selferral.admin.api.controller;

import java.util.List;

import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.selferral.admin.api.core.APIPath;
import io.selferral.admin.api.model.dto.ExchangeDto;
import io.selferral.admin.api.model.request.DeleteReviewRequest;
import io.selferral.admin.api.model.request.ExchangeReivewRequest;
import io.selferral.admin.api.model.response.APIResponse;
import io.selferral.admin.api.model.response.SelferralReviewListResponse;
import io.selferral.admin.api.service.ExchangeReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exchange")
public class ExchangeController {

	private final ExchangeReviewService exchangeReviewService;
	
	@GetMapping(path=APIPath.GET_EXCHANGE_REVIEW_LIST)
	@Description("거래소 후기 리스트 조회(거래소 별 검색 기능, 태깅 1~3개 조회)")
	public APIResponse getExchangeReviewList(@Valid ExchangeReivewRequest req) throws Exception{
		SelferralReviewListResponse res = exchangeReviewService.getExchangeReviewList(req);
		 return new APIResponse(HttpStatus.OK.value(),res, "S032") ;
	}
	
//	@GetMapping(path=APIPath.GET_EXCHANGE_REVIEW_LIST)
//	@Description("거래소 후기 리스트 조회(거래소 별 검색 기능, 태깅 1~3개 조회)")
//	public APIResponse getExchangeReviewList()throws Exception{
//		 List<ExchangeReviewDto> res = exchangeReviewService.getExchangeReviewList();
//		 return new APIResponse(HttpStatus.OK.value(),res, "S200") ;
//	}
	
	@GetMapping(path=APIPath.GET_EXCHANGE_LIST)
	@Description("거래소 목록 조회")
	public APIResponse getExchangeList() {
		List<ExchangeDto> res = exchangeReviewService.getExchangeList();
		System.out.println("## 들어왔다.");
		return new APIResponse(HttpStatus.OK.value(),res, "S030") ;
	}
	
	@PutMapping(path=APIPath.DELETE_EXCHANGE_REVIEW)
	@Description("거래소 후기 삭제")
	public APIResponse deleteExchangeReview (@RequestBody @Valid DeleteReviewRequest req) throws Exception {
		boolean isUpdate = exchangeReviewService.deleteExchangeReview(req);
		return new APIResponse(HttpStatus.OK.value(), isUpdate, "S031");
	}
}
