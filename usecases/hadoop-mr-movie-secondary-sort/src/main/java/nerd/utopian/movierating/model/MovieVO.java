package nerd.utopian.movierating.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;

public class MovieVO implements Writable {

  private long userId;
  private long movieId;
  private int rating;

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public long getMovieId() {
    return movieId;
  }

  public void setMovieId(long movieId) {
    this.movieId = movieId;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    dataOutput.writeLong(userId);
    dataOutput.writeLong(movieId);
    dataOutput.writeInt(rating);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    userId = dataInput.readLong();
    movieId = dataInput.readLong();
    rating = dataInput.readInt();
  }

  @Override
  public String toString() {
    return "MovieVO{" +
        "userId=" + userId +
        ", movieId=" + movieId +
        ", rating=" + rating +
        '}';
  }
}
