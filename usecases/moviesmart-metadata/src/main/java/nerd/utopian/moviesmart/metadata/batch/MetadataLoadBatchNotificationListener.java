package nerd.utopian.moviesmart.metadata.batch;

import java.util.Optional;
import nerd.utopian.moviesmart.metadata.MovieMetadata;
import nerd.utopian.moviesmart.metadata.MoviesMartMetadataDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MetadataLoadBatchNotificationListener extends JobExecutionListenerSupport {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(MetadataLoadBatchNotificationListener.class);

  private MoviesMartMetadataDao metadataDao;

  @Autowired
  public MetadataLoadBatchNotificationListener(MoviesMartMetadataDao metadataDao) {
    this.metadataDao = metadataDao;
  }

  @Override
  public void afterJob(JobExecution jobExecution) {

    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      LOGGER.info("BatchStatus.COMPLETED");
      final Optional<MovieMetadata> movieMetadataLastRow = metadataDao.findById(100L);

      if (movieMetadataLastRow.isPresent()) {
        LOGGER.info("Last Data Row Ingested is: {}", movieMetadataLastRow.get());
      }
    }
  }
}
