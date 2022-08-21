package nerd.utopian.movierating;

import nerd.utopian.movierating.mapper.MovieRatingMapper;
import nerd.utopian.movierating.reducer.MovieRatingReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Outputs the movieId list based on their popularity. Most highly rated movies are shown first;
 * descending order of the highest ratingCount.
 */
public class MovieRatingRunner {

  public static void main(String[] args) {

    try {

      Configuration configuration = new Configuration();

      Job mapReduceJob = Job.getInstance(configuration, "movie-rating-job");

      mapReduceJob.setJarByClass(MovieRatingRunner.class);

      mapReduceJob.setMapperClass(MovieRatingMapper.class);
      mapReduceJob.setCombinerClass(MovieRatingReducer.class);
      mapReduceJob.setReducerClass(MovieRatingReducer.class);

      mapReduceJob.setOutputKeyClass(LongWritable.class);
      mapReduceJob.setOutputValueClass(LongWritable.class);

      FileInputFormat.addInputPath(mapReduceJob, new Path(args[0]));
      FileOutputFormat.setOutputPath(mapReduceJob, new Path(args[1]));

      System.exit(mapReduceJob.waitForCompletion(true) ? 0 : 1);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
