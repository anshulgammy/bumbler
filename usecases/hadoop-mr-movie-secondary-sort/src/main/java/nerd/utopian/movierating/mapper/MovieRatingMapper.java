package nerd.utopian.movierating.mapper;

import java.io.IOException;
import nerd.utopian.movierating.model.Key;
import nerd.utopian.movierating.model.MovieVO;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MovieRatingMapper extends Mapper<LongWritable, Text, Key, MovieVO> {

  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    String[] rowValues = value.toString().split("\t");
    long userid = Long.parseLong(rowValues[0]);
    long movieId = Long.parseLong(rowValues[1]);
    int rating = Integer.parseInt(rowValues[2]);

    Key mapperKey = new Key();
    mapperKey.setUserId(userid);
    mapperKey.setRating(rating);

    MovieVO mapperValue = new MovieVO();
    mapperValue.setUserId(userid);
    mapperValue.setMovieId(movieId);
    mapperValue.setRating(rating);

    context.write(mapperKey, mapperValue);
  }
}
