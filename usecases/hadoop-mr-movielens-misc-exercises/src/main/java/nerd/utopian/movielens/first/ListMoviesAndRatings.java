package nerd.utopian.movielens.first;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Problem 1: Lists all the movies and the number of ratings given to them.
 */
public class ListMoviesAndRatings {

  public static void main(String[] args) {

    try {

      Configuration configuration = new Configuration();

      Job mapReduceJob = Job.getInstance(configuration, "movielens-first");

      mapReduceJob.setJarByClass(ListMoviesAndRatings.class);

      mapReduceJob.setMapperClass(ProblemMapper.class);
      mapReduceJob.setCombinerClass(ProblemReducer.class);
      mapReduceJob.setReducerClass(ProblemReducer.class);

      mapReduceJob.setOutputKeyClass(LongWritable.class);
      mapReduceJob.setOutputValueClass(LongWritable.class);

      FileInputFormat.addInputPath(mapReduceJob, new Path(args[0]));
      FileOutputFormat.setOutputPath(mapReduceJob, new Path(args[1]));

      System.exit(mapReduceJob.waitForCompletion(true) ? 0 : 1);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  class ProblemMapper extends Mapper<LongWritable, Text, LongWritable, LongWritable> {

    private LongWritable one = new LongWritable(1);
    private LongWritable movieId = new LongWritable();

    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {

      String[] rowValues = value.toString().split("\t");

      movieId.set(Long.parseLong(rowValues[1]));

      // set the movieId and one.
      context.write(movieId, one);
    }
  }

  class ProblemReducer extends
      Reducer<LongWritable, LongWritable, LongWritable, LongWritable> {

    private LongWritable ratingSum = new LongWritable();

    public void reduce(LongWritable movieId, Iterable<LongWritable> values, Context context)
        throws IOException, InterruptedException {
      long sum = 0;

      for (LongWritable val : values) {
        sum += val.get();
      }

      ratingSum.set(sum);

      // movieId and total ratings count to this movieId
      context.write(movieId, ratingSum);
    }
  }
}
