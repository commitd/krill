package io.committed.krill.extraction.support.test;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ThrowOnReadInputStream extends FilterInputStream {

  public ThrowOnReadInputStream(InputStream in) {
    super(in);
  }

  @Override
  public int read(byte[] bytes, int off, int len) throws IOException {
    throw new IOException("Failure");
  }
}
