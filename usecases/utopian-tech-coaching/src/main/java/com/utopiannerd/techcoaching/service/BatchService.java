package com.utopiannerd.techcoaching.service;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.utopiannerd.techcoaching.dao.model.Batch;
import com.utopiannerd.techcoaching.dao.repository.BatchRepository;
import com.utopiannerd.techcoaching.util.CoachingUtil;
import java.text.ParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

  public Mono<ServerResponse> queryBatches(ServerRequest serverRequest) {

    Flux<Batch> batchFlux = Flux.fromIterable(batchRepository.findAll());

    return ServerResponse.ok()
        .body(
            fromPublisher(
                batchFlux
                    .filter(
                        batch -> {
                          try {
                            return batch
                                .getBatchStartDate()
                                .after(
                                    CoachingUtil.toDate(
                                        serverRequest.queryParam("batchStartDate").get()));
                          } catch (ParseException e) {
                            throw new RuntimeException(e);
                          }
                        })
                    .filter(
                        batch -> {
                          try {
                            return batch
                                .getBatchEndDate()
                                .after(
                                    CoachingUtil.toDate(
                                        serverRequest.queryParam("batchEndDate").get()));
                          } catch (ParseException e) {
                            throw new RuntimeException(e);
                          }
                        })
                    .filter(
                        batch ->
                            batch.getTotalSeatsAvailable()
                                > Integer.parseInt(
                                    serverRequest.queryParam("totalSeatsAvailable").get())),
                Batch.class));
  }
}
