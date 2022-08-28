package nerd.utopian.movierating.model;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class MovieRatingPartitioner extends Partitioner<Key, NullWritable> {

  @Override
  public int getPartition(Key key, NullWritable value, int numPartitions) {
    // multiply by 127 to perform some mixing
    return (int) (Math.abs(key.getUserId() * 127) % numPartitions);
  }
}