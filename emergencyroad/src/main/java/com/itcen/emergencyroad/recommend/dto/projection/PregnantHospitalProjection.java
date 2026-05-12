package com.itcen.emergencyroad.recommend.dto.projection;

import com.itcen.emergencyroad.pregnant.entity.Pregnant;
import com.itcen.emergencyroad.pregnant.entity.PregnantRealtime;
import com.itcen.emergencyroad.pregnant.entity.PregnantStandard;

public interface PregnantHospitalProjection extends HospitalDataProjection {
    Pregnant getPregnant();
    PregnantRealtime getRealtime();
    PregnantStandard getStandard();
}
