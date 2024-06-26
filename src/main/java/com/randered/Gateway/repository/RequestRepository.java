package com.randered.Gateway.repository;

import com.randered.Gateway.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    boolean existsByRequestId(String requestId);
}
