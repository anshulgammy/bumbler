package nerd.utopian.movierating.reducer;

import java.io.IOException;
import nerd.utopian.movierating.model.Key;
import nerd.utopian.movierating.model.MovieVO;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class MovieRatingReducer extends
    Reducer<Key, MovieVO, NullWritable, MovieVO> {

  public void reduce(Key mapperKey, Iterable<MovieVO> movieVOIterable, Context context)
      throws IOException, InterruptedException {

    for (MovieVO val : movieVOIterable) {
      NullWritable nullKey = NullWritable.get();
      context.write(nullKey, val);
    }
  }
}
