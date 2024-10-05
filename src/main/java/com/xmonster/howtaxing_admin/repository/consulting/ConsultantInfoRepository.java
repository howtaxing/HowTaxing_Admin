package com.xmonster.howtaxing_admin.repository.consulting;

import com.xmonster.howtaxing_admin.model.ConsultantInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsultantInfoRepository extends JpaRepository<ConsultantInfo, Long> {

    Optional<ConsultantInfo> findByConsultantId(Long consultantId);
}
