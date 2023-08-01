package com.utopiannerd.techcoaching.service;

import com.utopiannerd.techcoaching.dao.model.Batch;
import com.utopiannerd.techcoaching.dao.repository.BatchRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatchService {

  private BatchRepository batchRepository;

  @Autowired
  public BatchService(BatchRepository batchRepository) {
    this.batchRepository = batchRepository;
  }

  public Batch addBatch(Batch batch) {
    return batchRepository.save(batch);
  }

  public List<Batch> addBatches(List<Batch> batchList) {
    return batchRepository.saveAll(batchList);
  }

  public Batch updateBatch(Batch batch) {
    return batchRepository.save(batch);
  }

  public void deleteBatch(Long batchId) {
    batchRepository.deleteById(batchId);
  }

  public Batch getBatch(Long batchId) {
    return batchRepository.findById(batchId).orElseThrow();
  }
}
