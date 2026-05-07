package com.itcen.emergencyroad.external.service;

import com.itcen.emergencyroad.external.api.MessageApiClient;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.entity.HospitalMessage;
import com.itcen.emergencyroad.hospital.dto.MessageApiResponseDto;
import com.itcen.emergencyroad.hospital.dto.MessageDto;
import com.itcen.emergencyroad.hospital.repository.HospitalMessageRepository;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageSyncService {
    private final MessageApiClient messageApiClient;
    private final HospitalRepository hospitalRepository;
    private final HospitalMessageRepository hospitalMessageRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void syncBySido(String sido, int page, int rows) throws Exception {
        String json = messageApiClient.getMessagesRawBySido(sido, page, rows); // 9번 API Endpoint 호출 - Q0 요청 파라미터만 사용

        // ObjectMapper를 이용하여 json(호출결과) 값을 읽어 DTO(MessageApiResponseDto) 객체로 변환
        MessageApiResponseDto responseDto = objectMapper.readValue(json, MessageApiResponseDto.class);

        // DTO 객체가 담고 있는 값 가운데 사용할 Items.item 을 가져옴.
        List<MessageDto> items = responseDto.getResponse()
                .getBody()
                .getItems()
                .getItem();
        /*
         Message API 호출 결과를 DB에 적재
         단, 현재는 테스트 과정이기때문에 마스터 테이블에 없는 병원 모두 테이블에 적재하도로고 함. ( 예외처리 따로 구현 X )
         */
        for (MessageDto dto : items) {
            Hospital hospital = hospitalRepository.findByHpid(dto.getHpid())
                    .orElseThrow(() -> new IllegalArgumentException("병원 Code 없음, 코드명 : " + dto.getHpid()));
            HospitalMessage hospitalMessage = HospitalMessage.builder()
                    .hospital(hospital)
                    .diseaseName(dto.getSymTypCodMag())
                    .messageType(dto.getSymBlkMsgTyp())
                    .message(dto.getSymBlkMsg())
                    .blockedStart(dto.getSymBlkSttDtm())
                    .blockedEnd(dto.getSymBlkEndDtm())
                    .build();

            hospitalMessageRepository.save(hospitalMessage);
        }

    }


}
