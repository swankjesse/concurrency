package com.publicobject.pixelsorter

import android.app.Activity
import android.os.Bundle
import android.widget.Button

class PixelSorterActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val layout = layoutInflater.inflate(R.layout.pixelsorter, null)
    setContentView(layout)

    val pixelGrid = layout.findViewById(R.id.pixelgrid) as PixelGridView

    (layout.findViewById(R.id.shuffle) as Button).setOnClickListener {
      pixelGrid.grid().shuffle()
    }

    (layout.findViewById(R.id.sort1) as Button).setOnClickListener {
      pixelGrid.grid().sort1()
    }

    (layout.findViewById(R.id.sort10) as Button).setOnClickListener {
      pixelGrid.grid().sort10()
    }
  }
}
