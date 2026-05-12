package com.itcen.emergencyroad.recommend.dto.projection;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.entity.HospitalDetail;

public interface HospitalDataProjection {
   Hospital getHospital();
   HospitalDetail getDetail();
}
