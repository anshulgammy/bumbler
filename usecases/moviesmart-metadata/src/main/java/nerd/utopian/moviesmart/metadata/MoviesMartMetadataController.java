package nerd.utopian.moviesmart.metadata;

import static java.util.Objects.requireNonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/")
public class MoviesMartMetadataController {

  private MoviesMartMetadataService metadataService;

  @Autowired
  public MoviesMartMetadataController(MoviesMartMetadataService metadataService) {
    this.metadataService =
        requireNonNull(metadataService, "metadataService is required");
  }

  @GetMapping("/movie/id/{movieId}")
  public MovieMetadataResponse getMovieById(
      @PathVariable("movieId") String movieId) {
    return metadataService.getMovieById(movieId);
  }

  @GetMapping("/movie/name/{movieTitle}")
  public MovieMetadataResponse getMovieByTitle(
      @PathVariable("movieTitle") String movieTitle) {
    return metadataService.getMovieByTitle(movieTitle);
  }

  @PostMapping("/movie")
  public MovieMetadataResponse saveMovie(@RequestBody MovieMetadata movie) {
    return metadataService.saveMovie(movie);
  }

  @PutMapping("/movie")
  public MovieMetadataResponse updateMovie(@RequestBody MovieMetadata movie) {
    return metadataService.updateMovie(movie);
  }

  @DeleteMapping("/movie/id/{movieId}")
  public MovieMetadataResponse updateMovie(@PathVariable("movieId") String movieId) {
    return metadataService.deleteMovie(movieId);
  }
}
