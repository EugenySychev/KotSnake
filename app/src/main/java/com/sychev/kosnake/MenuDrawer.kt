package com.sychev.kosnake

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.View

class MenuDrawer : View, View.OnTouchListener {

    lateinit var fontPaint: Paint;
    var redPaint: Paint = Paint();
    var text: String = "Test width text";
    var fontSize: Int = 100;
    lateinit var widths: FloatArray;
    var width: Float = 0.0f;
    var selectedItem: SelectedItem? = null;
    var itemList: List<String> = arrayListOf();
    var vertBorder: Float = 0f;
    var heightSplit: Float = 0f;

    interface SelectedItem {
        fun onSelectedItem(item: Int);
    }

    constructor(context: Context) : super(context) {

        redPaint.color = Color.RED;

        fontPaint = Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.textSize = fontSize.toFloat();
        fontPaint.style = Paint.Style.STROKE;
        fontPaint.color = Color.GREEN;


        // ширина текста
        width = fontPaint.measureText(text);

        // посимвольная ширина
        widths = FloatArray(text.length);
        fontPaint.getTextWidths(text, widths);

        setOnTouchListener(this);
    }

    fun setSelectedItemHandler(handler: SelectedItem) {
        selectedItem = handler;
    }

    fun setItemArray(array: List<String>) {
        itemList = array;
        updateSize();
    }

    fun updateSize() {
        var h = context.resources.displayMetrics.heightPixels;
        var count = itemList.count();
        heightSplit = (h / 20).toFloat();
        vertBorder = ((h - (count - 1) * heightSplit) / (count + 2)).toFloat();
        fontPaint.textSize = vertBorder;
    }

    @Override
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
//            canvas.drawARGB(80, -, 204, 255)
            canvas.translate(0F, 0F);

            var w = context.resources.displayMetrics.widthPixels;
            var h: Float;

            if (itemList.count() > 0)
                for (i in 0..itemList.count() - 1) {
                    h = vertBorder * (i + 1.5f) + i * heightSplit;

                    Log.d("PAINT", "Heiht" + h);
                    var xt = (w - fontPaint.measureText(itemList[i])) / 2;
                    canvas.drawText(itemList[i], xt,h, fontPaint);
                }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            Log.d("Menu", event.x.toString() + " and " + event.y.toString())
        };
        return true;
    }


}
