package com.nemislimus.tratometr.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.nemislimus.tratometr.R
import java.math.BigDecimal
import java.math.RoundingMode

class RingChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val rect = RectF()
    val data = mutableListOf<BigDecimal>() // Значения для диаграммы
    private val colors = mutableListOf<Int>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawRingChart(canvas)
    }

    @SuppressLint("DefaultLocale")
    private fun drawRingChart(canvas: Canvas) {
        val total = data.sumOf { it }
        var startAngle = 0f                 // Начальный угол
        val halfOffset = 2.5f                 // Отступы между секторами 5 градусов
        val ratio = 0.12f                   // Толщина диаграммы 0,16 от диаметра
        // Прямоугольник, в который вписывается внутренний круг диаграммы
        val rectInternal = createInnerRectF(rect, width.toFloat() * ratio, height.toFloat() * ratio)

        for (i in data.indices) {
            val sweepAngle = data[i].divide(total, 5, RoundingMode.HALF_UP).multiply(BigDecimal("360")).toFloat()     // Вычисление угла сегмента
            paint.color = colors[i]
            canvas.drawArc(rect, startAngle + halfOffset, sweepAngle - halfOffset, true, paint) // Рисуем сегмент
            startAngle += sweepAngle                    // Обновляем начальный угол для следующего сегмента
        }

        // Рисуем отверстие в центре
        val color = getAppBackgroundColor(context)
        paint.color = color
        paint.style = Paint.Style.FILL
        canvas.drawArc(rectInternal, 0f, 360f, true, paint) // Рисуем
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect.set(0f, 0f, w.toFloat(), h.toFloat()) // Устанавливаем размеры диаграммы
    }

    private fun createInnerRectF(outerRect: RectF, offsetHorizontal: Float, offsetVertical: Float): RectF {
        // Вычисление координат нового RectF с учетом отступов
        val left = outerRect.left + offsetHorizontal
        val right = outerRect.right - offsetHorizontal
        val top = outerRect.top + offsetVertical
        val bottom = outerRect.bottom - offsetVertical
        return RectF(left, top, right, bottom)
    }

    private fun getAppBackgroundColor(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.appBackgroundColor, typedValue, true)
        return typedValue.data
    }

    fun setData(colorsList: List<Int>, sumList: List<BigDecimal> ) {
        data.clear() // Очистить старые данные
        colors.clear()
        data.addAll(sumList) // Добавить новые данные
        colors.addAll(colorsList)
        invalidate()
    }


}