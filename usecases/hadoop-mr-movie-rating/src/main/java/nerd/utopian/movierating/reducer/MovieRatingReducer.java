package nerd.utopian.movierating.reducer;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class MovieRatingReducer extends
    Reducer<IntWritable, LongWritable, IntWritable, LongWritable> {

  private LongWritable result = new LongWritable();

  public void reduce(IntWritable movieRating, Iterable<LongWritable> values, Context context)
      throws IOException, InterruptedException {

    long sum = 0;

    for (LongWritable val : values) {
      sum += val.get();
    }

    result.set(sum);
    context.write(movieRating, result);
  }
}
