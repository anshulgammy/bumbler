package nerd.utopian.movierating.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.WritableComparable;

public class Key implements WritableComparable<Key> {

  private long userId;
  private int rating;

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
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
    dataOutput.writeInt(rating);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    userId = dataInput.readLong();
    rating = dataInput.readInt();
  }

  @Override
  public int compareTo(Key key) {
    if (userId == key.getUserId()) {
      // userId is same. rating given by userId will be compared.
      if (rating == key.getRating()) {
        return 0;
      } else if (rating < key.getRating()) {
        return 1;
      } else {
        return -1;
      }
    } else if (userId < key.getUserId()) {
      return 1;
    } else {
      return -1;
    }
  }
}
