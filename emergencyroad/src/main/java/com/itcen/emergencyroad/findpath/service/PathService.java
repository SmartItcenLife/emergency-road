package com.itcen.emergencyroad.findpath.service;

import com.itcen.emergencyroad.findpath.dto.LocationRequestDto;
import com.itcen.emergencyroad.findpath.dto.PathResponseDto;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PathService {
    private final HospitalRepository hospitalRepository;
    private final RestTemplate restTemplate = new RestTemplate();


}
