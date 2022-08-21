package nerd.utopian.movielens.second;

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
 * Problem 2: Lists all the Users and the number of ratings they have done for a movie.
 */
public class ListUsersAndRatings {

  public static void main(String[] args) {

    try {

      Configuration configuration = new Configuration();

      Job mapReduceJob = Job.getInstance(configuration, "movielens-second");

      mapReduceJob.setJarByClass(ListUsersAndRatings.class);

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
    private LongWritable userId = new LongWritable();

    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {

      String[] rowValues = value.toString().split("\t");

      userId.set(Long.parseLong(rowValues[0]));

      // set the userId and one.
      context.write(userId, one);
    }
  }

  class ProblemReducer extends
      Reducer<LongWritable, LongWritable, LongWritable, LongWritable> {

    private LongWritable ratingSum = new LongWritable();

    public void reduce(LongWritable userId, Iterable<LongWritable> values, Context context)
        throws IOException, InterruptedException {
      long sum = 0;

      for (LongWritable val : values) {
        sum += val.get();
      }

      ratingSum.set(sum);

      // userId and total ratings count given by this userId
      context.write(userId, ratingSum);
    }
  }
}
