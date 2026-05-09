package com.itcen.emergencyroad.pediatric.repository;

import com.itcen.emergencyroad.pediatric.entity.PediatricRealtime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PediatricRealtimeRepository extends JpaRepository<PediatricRealtime,Long> {
}
