package com.prajaconnect.repository;

import com.prajaconnect.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, String> {
    @Query("select c.id from Complaint c")
    List<String> findAllIds();
}
