package com.itcen.emergencyroad.recommend.dto.projection;

import com.itcen.emergencyroad.general.entity.GeneralRealTimeAndStandard;
import com.itcen.emergencyroad.general.entity.GeneralSrsIll;

public interface GeneralHospitalProjection extends HospitalDataProjection{

    GeneralSrsIll getGeneral();
    GeneralRealTimeAndStandard getGeneralRealTimeAndStandard();

}
