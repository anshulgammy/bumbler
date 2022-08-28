package nerd.utopian.movierating.model;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class KeyComparator extends WritableComparator {

  protected KeyComparator() {
    super(Key.class, true);
  }

  @Override
  public int compare(WritableComparable writableComparableFirst,
      WritableComparable writableComparableSecond) {
    Key keyFirst = (Key) writableComparableFirst;
    Key keySecond = (Key) writableComparableSecond;
    return keyFirst.compareTo(keySecond);
  }
}
