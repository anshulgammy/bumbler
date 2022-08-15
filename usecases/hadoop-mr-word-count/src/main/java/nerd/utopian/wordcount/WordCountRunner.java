package nerd.utopian.wordcount;

import nerd.utopian.wordcount.mapper.WordCountMapper;
import nerd.utopian.wordcount.reducer.WordCountReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCountRunner {

  public static void main(String[] args) {

    try {

      Configuration configuration = new Configuration();

      Job mapReduceJob = Job.getInstance(configuration, "word-count-job");

      mapReduceJob.setJarByClass(WordCountRunner.class);

      mapReduceJob.setMapperClass(WordCountMapper.class);
      mapReduceJob.setCombinerClass(WordCountReducer.class);

      mapReduceJob.setReducerClass(WordCountReducer.class);

      mapReduceJob.setOutputKeyClass(Text.class);
      mapReduceJob.setOutputValueClass(IntWritable.class);

      FileInputFormat.addInputPath(mapReduceJob, new Path(args[0]));
      FileOutputFormat.setOutputPath(mapReduceJob, new Path(args[1]));

      System.exit(mapReduceJob.waitForCompletion(true) ? 0 : 1);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
