package com.publicobject.pixelsorter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public final class PixelGridView extends View {
  static final int pixelWidthDp = 8;
  static final int pixelStrideDb = 10;

  private final Paint[] paints;
  private PixelGrid grid;

  public PixelGridView(Context context, AttributeSet attrs) {
    super(context, attrs);

    paints = new Paint[3];

    paints[0] = new Paint();
    paints[0].setColor(getResources().getColor(R.color.blue));
    paints[0].setTextAlign(Paint.Align.LEFT);
    paints[0].setTextSize(96);

    paints[1] = new Paint();
    paints[1].setColor(getResources().getColor(R.color.red));
    paints[1].setTextAlign(Paint.Align.RIGHT);
    paints[1].setTextSize(96);

    paints[2] = new Paint();
    paints[2].setColor(Color.WHITE);
    paints[2].setTextAlign(Paint.Align.CENTER);
    paints[2].setTextSize(96);

    // This view draws a ton of small boxes and that's much more efficient in software.
    setLayerType(LAYER_TYPE_SOFTWARE, null);
  }

  public PixelGrid grid() {
    return grid;
  }

  @Override protected void onDraw(Canvas canvas) {
    int availableHeight = (getHeight() - (int) paints[0].getTextSize()) / pixelStrideDb;
    int availableWidth = getWidth() / pixelStrideDb;

    if (grid == null || grid.rowCount != availableHeight || grid.columnCount != availableWidth) {
      grid = new PixelGrid(availableHeight, availableWidth);
    }

    int blueCount = 0;
    int redCount = 0;

    for (int r = 0; r < grid.rowCount; r++) {
      for (int c = 0; c < grid.columnCount; c++) {
        int color = grid.pixels[r][c];

        if (color == 0) {
          blueCount++;
        } else {
          redCount++;
        }

        int left = pixelStrideDb * c;
        int top = pixelStrideDb * r;
        canvas.drawRect(left, top, left + pixelWidthDp, top + pixelWidthDp, paints[color]);
      }
    }

    canvas.drawText(Integer.toString(blueCount), 0, getHeight(), paints[0]);
    canvas.drawText(Integer.toString(redCount),
        (grid.columnCount - 1) * pixelStrideDb + pixelWidthDp, getHeight(), paints[1]);
    int delta = Math.abs(redCount - blueCount);
    if (delta > 1) {
      canvas.drawText(Integer.toString(delta), getWidth() / 2, getHeight(), paints[2]);
    }

    invalidate(); // Repaint on every frame!
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = MeasureSpec.getSize(widthMeasureSpec);
    int height = MeasureSpec.getSize(heightMeasureSpec);
    setMeasuredDimension(width, height);
  }
}
