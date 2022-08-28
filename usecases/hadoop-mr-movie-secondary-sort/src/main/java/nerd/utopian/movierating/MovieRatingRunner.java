package nerd.utopian.movierating;

import nerd.utopian.movierating.mapper.MovieRatingMapper;
import nerd.utopian.movierating.model.GroupComparator;
import nerd.utopian.movierating.model.Key;
import nerd.utopian.movierating.model.KeyComparator;
import nerd.utopian.movierating.model.MovieRatingPartitioner;
import nerd.utopian.movierating.model.MovieVO;
import nerd.utopian.movierating.reducer.MovieRatingReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Sort based on the userId(ascending) who have rated the movie. If the user has rated more than one
 * movies, then sort against that userId based on the highest rating first(descending).
 */
public class MovieRatingRunner {

  public static void main(String[] args) {

    try {

      Configuration configuration = new Configuration();

      Job mapReduceJob = Job.getInstance(configuration, "movie-rating-job");

      mapReduceJob.setJarByClass(MovieRatingRunner.class);

      mapReduceJob.setMapperClass(MovieRatingMapper.class);
      mapReduceJob.setReducerClass(MovieRatingReducer.class);
      mapReduceJob.setPartitionerClass(MovieRatingPartitioner.class);
      mapReduceJob.setSortComparatorClass(KeyComparator.class);
      mapReduceJob.setGroupingComparatorClass(GroupComparator.class);

      mapReduceJob.setMapOutputKeyClass(Key.class);
      mapReduceJob.setMapOutputValueClass(MovieVO.class);
      mapReduceJob.setOutputKeyClass(NullWritable.class);
      mapReduceJob.setOutputValueClass(MovieVO.class);

      FileInputFormat.addInputPath(mapReduceJob, new Path(args[0]));
      FileOutputFormat.setOutputPath(mapReduceJob, new Path(args[1]));

      System.exit(mapReduceJob.waitForCompletion(true) ? 0 : 1);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
