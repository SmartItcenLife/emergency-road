package com.itcen.emergencyroad.pediatric.service;

import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PediatricApiResponseParser {
    /*
      소아 공공 API 응답을 검증하고 실제 데이터 목록(items.item)을 안전하게 추출하기위한 클래스입니다.

      공공 API 응답은 상황에 따라 response, header, body, items가 비어 있거나, resultCode가 성공이 아닌 값으로 내려올 수 있습니다.
      이러한 경우를 처리하기 위한 공통 로직 클래스.
     */
    private static final String SUCCESS_CODE = "00"; // 응답코드 : 00 = 성공
    /*
        API 응답 DTO에서 item 목록을 추출합니다.

        @param responseDto 공공 API 응답 DTO
        @param context 로그에 표시할 호출 맥락
        @param <T> item DTO 타입

     */
    public <T> List<T> extractItemsOrEmpty(
            PediatricRealtimeApiResponseDto<T> responseDto,
            String context
    ){
        /*
            비정상 응답이거나 조회 결과가 없는 경우 빈 리스트를 반환하여,
            호출한 서비스는 반환된 리스트가 비어 있으면 DB 적재를 진행하지 않음.
         */
        if(responseDto == null){
            log.warn("{} API 응답 객체가 NULL 입니다.", context);
            return List.of();
        }

        PediatricRealtimeApiResponseDto.Response<T> response = responseDto.getResponse();

        if(response == null) {
            log.warn("{} API Response 가 없습니다.", context);
            return List.of();
        }

        PediatricRealtimeApiResponseDto.Header header = response.getHeader();

        if (header == null) {
            log.warn("{} API header가 없습니다.", context);
            return List.of();
        }
        String resultCode = header.getResultCode();
        String resultMsg = header.getResultMsg();
        // 응답이 성공이 아니라면, DB 적재를 진행하지 않음
        if (!SUCCESS_CODE.equals(resultCode)) {
            log.warn(
                    "{} API 실패 응답입니다. resultCode={}, resultMsg={}",
                    context,
                    resultCode,
                    resultMsg
            );
            return List.of();
        }

        PediatricRealtimeApiResponseDto.Body<T> body = response.getBody();

        if (body == null) {
            log.warn("{} API body가 없습니다.", context);
            return List.of();
        }

        PediatricRealtimeApiResponseDto.Items<T> items = body.getItems();

        if (items == null) {
            log.info(
                    "{} API 조회 결과가 없습니다. pageNo={}, numOfRows={}, totalCount={}",
                    context,
                    body.getPageNo(),
                    body.getNumOfRows(),
                    body.getTotalCount()
            );
            return List.of();
        }

        List<T> itemList = items.getItem();

        if (itemList == null || itemList.isEmpty()) {
            log.info(
                    "{} API item 목록이 비어 있습니다. pageNo={}, numOfRows={}, totalCount={}",
                    context,
                    body.getPageNo(),
                    body.getNumOfRows(),
                    body.getTotalCount()
            );
            return List.of();
        }

        return itemList;
    }
}
