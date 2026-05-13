package com.itcen.emergencyroad.recommend.dto.projection;

import com.itcen.emergencyroad.pediatric.entity.PediatricMkioskty;
import com.itcen.emergencyroad.pediatric.entity.PediatricRealtime;
import com.itcen.emergencyroad.pediatric.entity.PediatricStandard;

public interface PediatricHospitalProjection extends HospitalDataProjection {

    PediatricMkioskty getPediatricMkioskty();
    PediatricRealtime getPediatricRealtime();
    PediatricStandard getPediatricStandard();
}
