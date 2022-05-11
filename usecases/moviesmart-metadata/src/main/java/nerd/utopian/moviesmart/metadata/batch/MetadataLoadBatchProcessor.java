package nerd.utopian.moviesmart.metadata.batch;

import nerd.utopian.moviesmart.metadata.MovieMetadata;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class MetadataLoadBatchProcessor implements ItemProcessor<MovieMetadata, MovieMetadata> {

  @Override
  public MovieMetadata process(MovieMetadata movieMetadata) throws Exception {
    return movieMetadata;
  }
}
