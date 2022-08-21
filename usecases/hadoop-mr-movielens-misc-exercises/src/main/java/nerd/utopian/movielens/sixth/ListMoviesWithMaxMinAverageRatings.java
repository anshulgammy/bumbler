package nerd.utopian.movielens.sixth;

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
 * Problem 6: List all the movies with the max, min and average ratings given by any user
 */
public class ListMoviesWithMaxMinAverageRatings {

  public static void main(String[] args) {

    try {

      Configuration configuration = new Configuration();

      Job mapReduceJob = Job.getInstance(configuration, "movielens-fifth");

      mapReduceJob.setJarByClass(ListMoviesWithMaxMinAverageRatings.class);

      mapReduceJob.setMapperClass(ProblemMapper.class);
      //mapReduceJob.setCombinerClass(ProblemReducer.class);
      mapReduceJob.setReducerClass(ProblemReducer.class);

      mapReduceJob.setMapOutputKeyClass(LongWritable.class);
      mapReduceJob.setMapOutputValueClass(LongWritable.class);
      mapReduceJob.setOutputKeyClass(LongWritable.class);
      mapReduceJob.setOutputValueClass(Text.class);

      FileInputFormat.addInputPath(mapReduceJob, new Path(args[0]));
      FileOutputFormat.setOutputPath(mapReduceJob, new Path(args[1]));

      System.exit(mapReduceJob.waitForCompletion(true) ? 0 : 1);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static class ProblemMapper extends Mapper<LongWritable, Text, LongWritable, LongWritable> {

    private LongWritable rating = new LongWritable(1);
    private LongWritable movieId = new LongWritable();

    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {

      String[] rowValues = value.toString().split("\t");

      movieId.set(Long.parseLong(rowValues[1]));
      rating.set(Long.parseLong(rowValues[2]));

      // set the movieId and rating given.
      context.write(movieId, rating);
    }
  }

  public static class ProblemReducer extends
      Reducer<LongWritable, LongWritable, LongWritable, Text> {

    private Text outputText = new Text();

    public void reduce(LongWritable movieId, Iterable<LongWritable> values, Context context)
        throws IOException, InterruptedException {
      long min = 10;
      long max = -1;
      long sumOfAllRatings = 0;
      long count = 0;

      for (LongWritable val : values) {

        if (val.get() < min) {
          min = val.get();
        }

        if (val.get() > max) {
          max = val.get();
        }

        sumOfAllRatings += val.get();
        count++;
      }

      long average = sumOfAllRatings / count;

      outputText.set("max=" + max + ", min=" + min + ", average" + average);

      // movieId and max/min/average ratings given to this movieId
      context.write(movieId, outputText);
    }
  }
}
