package nerd.utopian.moviesmart.metadata;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import nerd.utopian.moviesmart.metadata.MovieMetadata.Builder;
import org.springframework.http.HttpStatus;

public class MovieMetadataResponse {

  private List<MovieMetadata> movies;
  private HttpStatus httpStatus;
  private String message;

  protected MovieMetadataResponse(Collection<MovieMetadata> movies, HttpStatus httpStatus,
      String message) {
    this.movies = new ArrayList<>(requireNonNull(movies));
    this.httpStatus = requireNonNull(httpStatus);
    this.message = requireNonNull(message);
  }

  public List<MovieMetadata> getMovies() {
    return movies;
  }

  public void setMovies(List<MovieMetadata> movies) {
    this.movies = new ArrayList<>(requireNonNull(movies));
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = requireNonNull(httpStatus);
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = requireNonNull(message);
  }

  public static MovieMetadataResponse.Builder builder() {
    return new MovieMetadataResponse.Builder();
  }

  public static MovieMetadataResponse.Builder builder(MovieMetadataResponse data) {
    return new MovieMetadataResponse.Builder(data);
  }

  public static final class Builder {

    private List<MovieMetadata> movies;
    private HttpStatus httpStatus;
    private String message;

  private Builder() {
  }

  private Builder(MovieMetadataResponse initialData) {
    this.movies = requireNonNull(initialData.movies);
    this.httpStatus = requireNonNull(initialData.httpStatus);
    this.message = requireNonNull(initialData.message);
  }

  public Builder setMovies(List<MovieMetadata> movies) {
    this.movies = movies;
    return this;
  }

  public Builder setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
    return this;
  }

  public Builder setMessage(String message) {
    this.message = message;
    return this;
  }

  public MovieMetadataResponse build() {
    return new MovieMetadataResponse(movies, httpStatus, message);
  }
}

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MovieMetadataResponse that = (MovieMetadataResponse) o;

    return Objects.equals(this.getMovies(), that.getMovies()) && Objects.equals(
        this.getHttpStatus(), that.getHttpStatus()) && Objects.equals(this.getMessage(),
        that.getMessage());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getMovies(), getHttpStatus(), getMessage());
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", this.getClass().getSimpleName() + "{", "}")
        .add("movies=" + getMovies())
        .add("httpStatus=" + getHttpStatus())
        .add("message=" + getMessage())
        .toString();
  }
}
