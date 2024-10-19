package com.xmonster.howtaxing_admin.repository.consulting;

import com.xmonster.howtaxing_admin.model.ConsultingScheduleId;
import com.xmonster.howtaxing_admin.model.ConsultingScheduleManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ConsultingScheduleManagementRepository extends JpaRepository<ConsultingScheduleManagement, ConsultingScheduleId> {

     @Query(value = "SELECT * FROM consulting_schedule_management c WHERE (c.consultant_id = :consultantId AND c.reservation_date >= :today)", nativeQuery = true)
     List<ConsultingScheduleManagement> findByConsultantIdAfterToday(@Param("consultantId") Long consultantId, @Param("today") LocalDate today);

     @Query(value = "SELECT * FROM consulting_schedule_management c WHERE (c.consultant_id = :consultantId AND c.reservation_date BETWEEN :startDate AND :endDate)", nativeQuery = true)
     List<ConsultingScheduleManagement> findByConsultantIdFromStartDateToEndDate(@Param("consultantId") Long consultantId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

     ConsultingScheduleManagement findByConsultingScheduleId(ConsultingScheduleId consultingScheduleId);
}
