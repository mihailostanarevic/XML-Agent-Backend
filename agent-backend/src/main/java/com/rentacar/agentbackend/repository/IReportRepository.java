package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IReportRepository extends JpaRepository<Report, UUID> {

    Report findOneById(UUID id);

    Report findOnByRequestAd_Id(UUID id);

//    List<Report> findAllByRequestAd_Ad_Id(UUID id);
}
