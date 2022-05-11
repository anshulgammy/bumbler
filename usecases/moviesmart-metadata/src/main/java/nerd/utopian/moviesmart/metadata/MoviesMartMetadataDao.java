package nerd.utopian.moviesmart.metadata;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviesMartMetadataDao extends JpaRepository<MovieMetadata, Long> {

  Optional<List<MovieMetadata>> findByTitleContaining(String movieTitle);
}
