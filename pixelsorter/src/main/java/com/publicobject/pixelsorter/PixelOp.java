package com.publicobject.pixelsorter;

import java.util.Random;

enum PixelOp {
  SHUFFLE {
    @Override boolean run(PixelGrid grid, Random random) {
      int available = 0;
      int required = grid.rowCount * grid.columnCount;

      for (int r = 0; r < grid.rowCount; r++) {
        for (int c = 0; c < grid.columnCount; c++) {
          if (grid.pixels[r][c] == 1) available++;
        }
      }

      for (int r = 0; r < grid.rowCount; r++) {
        for (int c = 0; c < grid.columnCount; c++) {
          if (random.nextInt(required) < available) {
            available--;
            grid.pixels[r][c] = 1;
          } else {
            grid.pixels[r][c] = 0;
          }
          required--;
        }
      }

      return false;
    }
  },

  SORT_SWAP_AND_FLIP_A_ROW {
    @Override public boolean run(PixelGrid grid, Random random) {
      int startRow = random.nextInt(grid.rowCount);

      for (int rn = grid.rowCount - 1; rn >= 0; rn--) {
        int r1 = (rn + startRow) % grid.rowCount;
        int r2 = r1 + 1;
        if (r2 == grid.rowCount) continue;

        int count1 = 0;
        int count2 = 0;
        for (int c = 0; c < grid.columnCount; c++) {
          if (grid.pixels[r1][c] == 1) count1++;
          if (grid.pixels[r2][c] == 1) count2++;
        }

        if (count1 > count2) {
          for (int c = 0; c < grid.columnCount; c++) {
            int tmp = grid.pixels[r2][grid.columnCount - 1 - c];
            grid.pixels[r2][grid.columnCount - 1 - c] = grid.pixels[r1][c];
            grid.pixels[r1][c] = tmp;
          }
          return true;
        }
      }

      return false;
    }
  },

  SORT_SWAP_MIN_MAX {
    @Override public boolean run(PixelGrid grid, Random random) {
      int startRow = random.nextInt(grid.rowCount);
      for (int rn = 0; rn < grid.rowCount; rn++) {
        int r = (rn + startRow) % grid.rowCount;

        int min1 = Integer.MAX_VALUE;
        int max0 = Integer.MIN_VALUE;
        for (int c = 0; c < grid.columnCount; c++) {
          if (grid.pixels[r][c] == 1 && c < min1) min1 = c;
          if (grid.pixels[r][c] == 0 && c > max0) max0 = c;
        }
        if (min1 != Integer.MAX_VALUE && max0 != Integer.MIN_VALUE && min1 < max0) {
          grid.pixels[r][min1] = 0;
          grid.pixels[r][max0] = 1;
          return true;
        }
      }

      return false;
    }
  },

  SORT_FLIP_SLICE_4 {
    @Override public boolean run(PixelGrid grid, Random random) {
      int startRow = random.nextInt(grid.rowCount);
      int startColumn = random.nextInt(grid.rowCount);

      for (int rn = 0; rn < grid.rowCount; rn++) {
        int r = (rn + startRow) % grid.rowCount;
        for (int cn = 0; cn < grid.columnCount; cn++) {
          int c = (cn + startColumn) % grid.columnCount;
          if (c + 3 < grid.columnCount) {
            int p0 = grid.pixels[r][c];
            int p1 = grid.pixels[r][c + 1];
            int p2 = grid.pixels[r][c + 2];
            int p3 = grid.pixels[r][c + 3];
            if (p0 > p3) {
              grid.pixels[r][c + 3] = p0;
              grid.pixels[r][c + 2] = p1;
              grid.pixels[r][c + 1] = p2;
              grid.pixels[r][c] = p3;
              return true;
            }
          }
        }
      }

      return false;
    }
  },

  SORT_FLIP_SLICE_5 {
    @Override boolean run(PixelGrid grid, Random random) {
      int startRow = random.nextInt(grid.rowCount);
      int startColumn = random.nextInt(grid.rowCount);

      for (int rn = grid.rowCount - 1; rn >= 0; rn--) {
        int r = (rn + startRow) % grid.rowCount;
        for (int cn = 0; cn < grid.columnCount; cn++) {
          int c = (cn + startColumn) % grid.columnCount;
          if (c + 4 < grid.columnCount) {
            int p0 = grid.pixels[r][c];
            int p1 = grid.pixels[r][c + 1];
            int p2 = grid.pixels[r][c + 2];
            int p3 = grid.pixels[r][c + 3];
            int p4 = grid.pixels[r][c + 4];
            if (p0 + p1 > p3 + p4) {
              grid.pixels[r][c + 4] = p0;
              grid.pixels[r][c + 3] = p1;
              grid.pixels[r][c + 2] = p2;
              grid.pixels[r][c + 1] = p3;
              grid.pixels[r][c] = p4;
              return true;
            }
          }
        }
      }
      return false;
    }
  };

  /** Returns false if additional executions are not necessary. */
  abstract boolean run(PixelGrid grid, Random random);
}
