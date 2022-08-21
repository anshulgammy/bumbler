package nerd.utopian.movierating.reducer;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class MovieRatingReducer extends
    Reducer<LongWritable, LongWritable, LongWritable, LongWritable> {

  // This Map stores ratingCount-movieId map in descending order of popularity.
  private Map<Long, Long> ratingCountForMovieIdMap = new TreeMap<>(Collections.reverseOrder());

  public void reduce(LongWritable movieId, Iterable<LongWritable> values, Context context)
      throws IOException, InterruptedException {
    long sum = 0;

    for (LongWritable val : values) {
      sum += val.get();
    }

    ratingCountForMovieIdMap.put(new Long(sum), movieId.get());
  }

  @Override
  protected void cleanup(Context context) throws IOException, InterruptedException {

    LongWritable totalRatingCountLongWritable = new LongWritable();
    LongWritable movieIdLongWritable = new LongWritable();

    ratingCountForMovieIdMap.forEach((totalRatingCount, movieId) -> {
      totalRatingCountLongWritable.set(totalRatingCount);
      movieIdLongWritable.set(movieId);

      try {
        context.write(movieIdLongWritable, totalRatingCountLongWritable);

      } catch (IOException e) {
        e.printStackTrace();

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
  }
}
