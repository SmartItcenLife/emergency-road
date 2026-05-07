package com.itcen.emergencyroad;

import com.itcen.emergencyroad.external.dto.MessageApiDto;
import com.itcen.emergencyroad.external.dto.MessageApiResponseDto;
import com.itcen.emergencyroad.external.api.MessageApiClient;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.entity.HospitalMessage;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

class MessageOpenApiClientTest {

    private static final String SERVICE_KEY =
            "hw0R4XrvNkWbm0ypSZ1gDLELMzU2gNyadL+va4IgiMyAoZrkb2XF+HpWdcBz3r5kbZvGd+bh0F+NN7mdO9do4w==";

    @Test
    void message_api_raw_response_check_by_sido() {
        RestClient restClient = RestClient.create();

        MessageApiClient client = new MessageApiClient(restClient, SERVICE_KEY);
        System.out.println("serviceKey raw = " + SERVICE_KEY);
        String response = client.getMessagesRawBySido("서울특별시");
        System.out.println(response);
    }

    @Test
    void message_api_raw_response_check_by_hpid() {
        RestClient restClient = RestClient.create();

        MessageApiClient client = new MessageApiClient(restClient, SERVICE_KEY);
        System.out.println("serviceKey raw = " + SERVICE_KEY);
        String response = client.getMessagesRawByHpid("A1100014");
        System.out.println(response);
    }

    // messageAPI Json -> DTO Test
    @Test
    void message_api_json_to_dto_test() throws Exception {
        RestClient restClient = RestClient.create();
        MessageApiClient client = new MessageApiClient(restClient, SERVICE_KEY);

        String json = client.getMessagesRawByHpid("A1100014");
        System.out.println("원본 JSON = " + json);

        ObjectMapper objectMapper = new ObjectMapper();
        MessageApiResponseDto responseDto =
                objectMapper.readValue(json, MessageApiResponseDto.class);

        System.out.println("resultCode = " + responseDto.getResponse().getHeader().getResultCode());
        System.out.println("resultMsg = " + responseDto.getResponse().getHeader().getResultMsg());

        if (responseDto.getResponse().getBody() != null
                && responseDto.getResponse().getBody().getItems() != null
                && responseDto.getResponse().getBody().getItems().getItem() != null
                && !responseDto.getResponse().getBody().getItems().getItem().isEmpty()) {

            System.out.println("첫 병원 hpid = "
                    + responseDto.getResponse().getBody().getItems().getItem().get(0).getHpid());

            System.out.println("첫 메시지 = "
                    + responseDto.getResponse().getBody().getItems().getItem().get(0).getSymBlkMsg());
        }
    }

    // MessageAPI DTO -> Entity Test
    @Test
    void message_dto_to_entity_test() throws Exception {
        RestClient restClient = RestClient.create();
        MessageApiClient client = new MessageApiClient(restClient, SERVICE_KEY);

        String json = client.getMessagesRawByHpid("A1100014");

        ObjectMapper objectMapper = new ObjectMapper();

        MessageApiResponseDto responseDto =
                objectMapper.readValue(json, MessageApiResponseDto.class);

        MessageApiDto dto = responseDto.getResponse()
                .getBody()
                .getItems()
                .getItem()
                .get(0);

        Hospital hospital = Hospital.builder()
                .id(1L)
                .hpid(dto.getHpid())
                .hospitalName(dto.getDutyName())
                .address(dto.getDutyAddr())
                .build();

        HospitalMessage entity = HospitalMessage.builder()
                .hpid(dto.getHpid())
                .dutyName(dto.getDutyName())
                .diseaseCode(dto.getSymTypCod())
                .diseaseName(dto.getSymTypCodMag())
                .messageType(dto.getSymBlkMsgTyp())
                .message(dto.getSymBlkMsg())
                .blockedStart(dto.getSymBlkSttDtm())
                .blockedEnd(dto.getSymBlkEndDtm())
                .displayYn(dto.getSymOutDspYon())
                .displayMethod(dto.getSymOutDspMth())
                .build();

        System.out.println("병원 HPID = " + entity.getHpid());
        System.out.println("질환코드 = " + entity.getDiseaseCode());
        System.out.println("질환명 = " + entity.getDiseaseName());
        System.out.println("메시지타입 = " + entity.getMessageType());
        System.out.println("메시지 = " + entity.getMessage());
    }
}