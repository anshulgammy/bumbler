package nerd.utopian.moviesmart.metadata.batch;

import javax.persistence.EntityManagerFactory;
import nerd.utopian.moviesmart.metadata.MovieMetadata;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class MetadataLoadBatchConfiguration {

  private static final String[] COLUMN_NAMES =
      new String[] {
        "Id",
        "Title",
        "Genre",
        "Director",
        "Actors",
        "Year",
        "Runtime",
        "Rating",
        "Votes",
        "Revenue",
        "Metascore"
      };

  @Bean
  public Job moviesDataLoadBatchJob(
      JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory,
      ItemReader<MovieMetadata> itemReader,
      // ItemProcessor<T, U> T refers to the Raw Type as present while reading from the source. U
      // refers to the
      // mapped class into which read data will be initialized.
      ItemProcessor<MovieMetadata, MovieMetadata> itemProcessor,
      ItemWriter<MovieMetadata> itemWriter,
      MetadataLoadBatchNotificationListener moviesDataLoadBatchNotificationListener) {
    // creating spring batch job step below.
    final Step movieDataLoadStep =
        stepBuilderFactory
            .get("movie-data-load-step")
            .<MovieMetadata, MovieMetadata>chunk(10)
            .reader(itemReader)
            .processor(itemProcessor)
            .writer(itemWriter)
            .build();

    // creating and returning the job configured with spring batch, with the step created above.
    return jobBuilderFactory
        .get("movie-data-load-job")
        .incrementer(new RunIdIncrementer())
        .listener(moviesDataLoadBatchNotificationListener)
        // we can use .flow() in case we have more than one
        // step for our ETL job. eg: .flow(step1).next(step2).next(step3) and so on.
        // for this particular use case, we have only one step, that is why we used .start(step)
        .start(movieDataLoadStep)
        .build();
  }

  @Bean
  public ItemReader<MovieMetadata> createFileItemReader() {
    return new FlatFileItemReaderBuilder<MovieMetadata>()
        .name("movie-data-reader")
        .resource(new ClassPathResource("movies-data\\movies.csv"))
        .strict(false)
        .linesToSkip(1)
        .delimited()
        .names(COLUMN_NAMES)
        .fieldSetMapper(
            new BeanWrapperFieldSetMapper<MovieMetadata>() {
              {
                setTargetType(MovieMetadata.class);
              }
            })
        .build();
  }

  @Bean
  public ItemWriter<MovieMetadata> createItemWriter(EntityManagerFactory entityManagerFactory) {
    return new JpaItemWriterBuilder<MovieMetadata>().entityManagerFactory(entityManagerFactory).build();
  }
}
