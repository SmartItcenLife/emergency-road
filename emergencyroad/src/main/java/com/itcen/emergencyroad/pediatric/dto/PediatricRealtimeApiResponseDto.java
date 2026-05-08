package com.itcen.emergencyroad.pediatric.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PediatricRealtimeApiResponseDto {
    private Response response;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private Header header;
        private Body body;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Body {
        private Items items;
        private Integer numOfRows;
        private Integer pageNo;
        private Integer totalCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Items {
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        private List<PediatricRealtimeDto> item;
    }
}
