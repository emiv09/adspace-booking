package com.generatik.adspace.repository;

import com.generatik.adspace.entity.AdSpace;
import com.generatik.adspace.entity.AdSpaceStatus;
import com.generatik.adspace.entity.AdSpaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdSpaceRepository extends JpaRepository<AdSpace, Long> {

    List<AdSpace> findByStatus(AdSpaceStatus status);

    List<AdSpace> findByStatusAndType(AdSpaceStatus status, AdSpaceType type);

    List<AdSpace> findByStatusAndCity(AdSpaceStatus status, String city);

    List<AdSpace> findByStatusAndTypeAndCity(AdSpaceStatus status, AdSpaceType type, String city);
}

