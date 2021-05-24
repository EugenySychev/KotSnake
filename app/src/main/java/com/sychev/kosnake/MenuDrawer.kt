package com.sychev.kosnake

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.View

class MenuDrawer(context: Context) : View(context), View.OnTouchListener {

    var fontPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var fontSize: Int = 100
    var selectionHandler: SelectedItem? = null
    var itemList: List<String> = arrayListOf()
    var vertBorder: Float = 0f
    var heightSplit: Float = 0f
    var rectList: MutableList<Rect> = mutableListOf()

    interface SelectedItem {
        fun onSelectedItem(item: Int)
    }

    init {
        fontPaint.textSize = fontSize.toFloat()
        fontPaint.style = Paint.Style.FILL
        fontPaint.color = Color.GREEN
        setOnTouchListener(this)
    }

    fun setSelectedItemHandler(handler: SelectedItem) {
        this.selectionHandler = handler
    }

    fun setItemArray(array: List<String>) {
        itemList = array
        updateSize()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updateSize()
    }

    private fun updateSize() {
        val h = context.resources.displayMetrics.heightPixels
        val w = context.resources.displayMetrics.widthPixels
        val count = itemList.count()
        heightSplit = (h / 20).toFloat()
        vertBorder = ((h - (count - 1) * heightSplit) / (count + 2)).toFloat()
        fontPaint.textSize = vertBorder / 3
        for (i in 0 until itemList.count()) {
            val y = vertBorder * (i + 1.5f) + i * heightSplit
            val rectangle: Rect = Rect(((w - fontPaint.measureText(itemList[i])) / 2).toInt(),
                (y - vertBorder).toInt(),
                ((w + fontPaint.measureText(itemList[i])) / 2).toInt(),
                y.toInt())

            rectList.add(rectangle)
        }
    }

    @Override
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {

            if (itemList.count() > 0 && rectList.count() > 0)
                for (i in 0 until itemList.count()) {
                    canvas.drawText(itemList[i], rectList[i].left.toFloat(),
                        rectList[i].top.toFloat() + vertBorder, fontPaint)
                }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                for (i in 0 until rectList.count()) {
                    if (rectList[i].contains(event.x.toInt(),
                            event.y.toInt()) && selectionHandler != null
                    ) {
                        Log.d("Menu", "Select " + itemList[i])
                        selectionHandler!!.onSelectedItem(i)
                        // TODO: add some display change on select item menu
                    }
                }
            }
        }
        return true
    }
}
