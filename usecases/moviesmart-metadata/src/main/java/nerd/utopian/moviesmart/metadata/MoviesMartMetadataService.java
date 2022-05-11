package nerd.utopian.moviesmart.metadata;

import static java.util.Objects.requireNonNull;

import com.google.common.base.Preconditions;
import io.micrometer.core.instrument.util.StringUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service class for performing Metadata Service related operations, for the MoviesMart
 * application.
 */
@Service
public class MoviesMartMetadataService {

  private MoviesMartMetadataDao metadataDao;

  public MoviesMartMetadataService(MoviesMartMetadataDao metadataDao) {
    this.metadataDao = requireNonNull(metadataDao, "metadataDao is required");
  }

  /**
   * Fetches MovieMetadataResponse for the provided movieId.
   *
   * @param movieId - String
   * @return - {@link MovieMetadataResponse}
   */
  public MovieMetadataResponse getMovieById(String movieId) {

    Preconditions.checkArgument(StringUtils.isNotEmpty(movieId), "movieId cannot be empty");

    Optional<MovieMetadata> movie = metadataDao.findById(Long.valueOf(movieId));

    if (movie.isPresent()) {

      return MovieMetadataResponse.builder()
          .setMovies(Arrays.asList(movie.get()))
          .setHttpStatus(HttpStatus.OK)
          .setMessage(Constants.SUCCESS_MESSAGE)
          .build();

    } else {

      return MovieMetadataResponse.builder()
          .setHttpStatus(HttpStatus.NO_CONTENT)
          .setMovies(Collections.EMPTY_LIST)
          .setMessage(Constants.MOVIE_NOT_FOUND)
          .build();
    }
  }

  /**
   * Fetches MovieMetadataResponse for the provided movieTitle.
   *
   * @param movieTitle - String
   * @return - {@link MovieMetadataResponse}
   */
  public MovieMetadataResponse getMovieByTitle(String movieTitle) {

    Preconditions.checkArgument(StringUtils.isNotEmpty(movieTitle), "movieTitle cannot be empty");

    Optional<List<MovieMetadata>> movieList = metadataDao.findByTitleContaining(movieTitle);

    if (movieList.isPresent()) {

      return MovieMetadataResponse.builder()
          .setMovies(movieList.get())
          .setHttpStatus(HttpStatus.OK)
          .setMessage(Constants.SUCCESS_MESSAGE)
          .build();

    } else {

      return MovieMetadataResponse.builder()
          .setMovies(Collections.EMPTY_LIST)
          .setHttpStatus(HttpStatus.NO_CONTENT)
          .setMessage(Constants.MOVIE_NOT_FOUND)
          .build();
    }
  }

  /**
   * Saves the provided MovieMetadata object
   *
   * @param movie - {@link MovieMetadata}
   * @return - {@link MovieMetadataResponse}
   */
  public MovieMetadataResponse saveMovie(MovieMetadata movie) {

    requireNonNull(movie, "movie cannot be null");

    metadataDao.save(movie);

    return MovieMetadataResponse.builder()
        .setHttpStatus(HttpStatus.CREATED)
        .setMovies(Arrays.asList(movie))
        .setMessage(Constants.SUCCESS_MESSAGE)
        .build();
  }

  /**
   * Updates the existing MovieMetadata as per provided one.
   *
   * @param movie - {@link MovieMetadata}
   * @return - {@link MovieMetadataResponse}
   */
  public MovieMetadataResponse updateMovie(MovieMetadata movie) {

    requireNonNull(movie, "movie cannot be null");

    metadataDao.save(movie);

    return MovieMetadataResponse.builder()
        .setHttpStatus(HttpStatus.ACCEPTED)
        .setMovies(Arrays.asList(movie))
        .setMessage(Constants.SUCCESS_MESSAGE)
        .build();
  }

  /**
   * Deletes the {@link MovieMetadata} based on provided movieId.
   *
   * @param movieId - String
   * @return - {@link MovieMetadataResponse}
   */
  public MovieMetadataResponse deleteMovie(String movieId) {

    Preconditions.checkArgument(StringUtils.isNotEmpty(movieId), "movieId cannot be empty");

    metadataDao.deleteById(Long.valueOf(movieId));

    return MovieMetadataResponse.builder()
        .setHttpStatus(HttpStatus.ACCEPTED)
        .setMovies(Collections.EMPTY_LIST)
        .setMessage(Constants.SUCCESS_MESSAGE)
        .build();
  }
}
