package com.publicobject.pixelsorter;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public final class PixelGrid {
  private final ExecutorService executor1 = Executors.newFixedThreadPool(1, new ThreadFactory() {
    @Override public Thread newThread(Runnable runnable) {
      Thread result = new Thread(runnable);
      result.setPriority(Thread.MIN_PRIORITY);
      return result;
    }
  });

  private final ExecutorService executor10 = Executors.newFixedThreadPool(10, new ThreadFactory() {
    @Override public Thread newThread(Runnable runnable) {
      Thread result = new Thread(runnable);
      result.setPriority(Thread.MIN_PRIORITY);
      return result;
    }
  });

  public final int rowCount;
  public final int columnCount;
  public final int[][] pixels;

  public PixelGrid(int rowCount, int columnCount) {
    this.rowCount = rowCount;
    this.columnCount = columnCount;

    int p = 0;
    int half = (rowCount * columnCount) / 2;

    pixels = new int[rowCount][];
    for (int r = 0; r < rowCount; r++) {
      pixels[r] = new int[columnCount];
      for (int c = 0; c < columnCount; c++) {
        pixels[r][c] = p++ < half ? 1 : 0;
      }
    }
  }

  public void shuffle() {
    runOp(executor10, 1, PixelOp.SHUFFLE);
  }

  public void sort1() {
    sort(executor1);
  }

  public void sort10() {
    sort(executor10);
  }

  private void sort(ExecutorService executor) {
    runOp(executor, 2, PixelOp.SORT_FLIP_SLICE_4);
    runOp(executor, 2, PixelOp.SORT_FLIP_SLICE_5);
    runOp(executor, 2, PixelOp.SORT_SWAP_A_ROW);
    runOp(executor, 2, PixelOp.SORT_SWAP_MIN_MAX);
  }

  private void runOp(ExecutorService executorService, int count, final PixelOp op) {
    for (int i = 0; i < count; i++) {
      executorService.execute(new Runnable() {
        @Override public void run() {
          Random random = new Random();
          while (op.run(PixelGrid.this, random)) {
          }
        }
      });
    }
  }
}
