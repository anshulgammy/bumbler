/* (C)2023 */
package com.utopiannerd.techcoaching.dao.repository;

import com.utopiannerd.techcoaching.dao.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {}
