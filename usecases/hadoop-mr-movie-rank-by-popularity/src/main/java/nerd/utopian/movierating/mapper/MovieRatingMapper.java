package nerd.utopian.movierating.mapper;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MovieRatingMapper extends Mapper<LongWritable, Text, LongWritable, LongWritable> {

  private static final LongWritable one = new LongWritable(1);
  private LongWritable movieId = new LongWritable();

  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    String[] rowValues = value.toString().split("\t");

    movieId.set(Long.parseLong(rowValues[1]));

    // set the movieRating and one.
    context.write(movieId, one);
  }
}
