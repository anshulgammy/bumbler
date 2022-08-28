package nerd.utopian.movierating.model;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class GroupComparator extends WritableComparator {

  protected GroupComparator() {
    super(Key.class, true);
  }

  @Override
  public int compare(WritableComparable writableComparableFirst,
      WritableComparable writableComparableSecond) {
    Key keyFirst = (Key) writableComparableFirst;
    Key keySecond = (Key) writableComparableSecond;
    return keyFirst.getUserId() == keySecond.getUserId() ? 0
        : keyFirst.getUserId() < keySecond.getUserId() ? 1 : 0;
  }

}
