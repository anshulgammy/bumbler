package nerd.utopian.movierating.mapper;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MovieRatingMapper extends Mapper<LongWritable, Text, IntWritable, LongWritable> {

  private static final LongWritable one = new LongWritable(1);
  private IntWritable movieRating = new IntWritable();

  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    String[] rowValues = value.toString().split("\t");

    movieRating.set(Integer.parseInt(rowValues[2]));

    // set the movieRating and one.
    context.write(movieRating, one);
  }
}
