package co.riiid.sledge.lab.utilview

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.text.style.StrikethroughSpan
import android.util.AttributeSet
import android.view.*
import android.widget.TextView
import co.riiid.rxsample.R
import java.util.*

/**
 * Created by sangcomz on 6/7/16.
 */
class Part7TextWithMetaDataView : TextView, ActionMode.Callback {


    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }


    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        params?.height = ViewGroup.LayoutParams.MATCH_PARENT
        super.setLayoutParams(params)
    }


//    fun initText(context: Context,
//                 align: String,
//                 metaData: ArrayList<MetaData>) {
//        var completedStr = SpannableString("")
//        metaData.forEach {
//            var str = SpannableString(it.text)
//            str = setColor(str, it.color)
//            it.styles?.forEach {
//                str = getStyleSpan(it, str)
//            }
//            completedStr = SpannableString(TextUtils.concat(completedStr, str))
//        }
//        text = completedStr
//        this.gravity = getAlign(align)
//        setLineSpacing(resources.getDimension(R.dimen.lab_passage_spacing), 1f)
//        typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
//    }

    fun getAlign(align: String): Int {
        if (align == "left")
            return Gravity.START
        else if (align == "center")
            return Gravity.CENTER
        else
            return Gravity.START
    }

    fun getStyleSpan(style: String,
                     str: SpannableString,
                     begin: Int,
                     offset: Int): SpannableString {
        if (style == "bold")
            return setBold(str, begin, offset)
        else if (style == "italic")
            return setItalic(str, begin, offset)
        else if (style == "underline")
            return setUnderline(str, begin, offset)
        else if (style == "strike")
            return setStrike(str, begin, offset)
        else
            return str
    }


    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setTextIsSelectable(true)
        customSelectionActionModeCallback = this
//        movementMethod = LinkMovementMethod.getInstance()
        setOnTouchListener(null)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        // FIXME simple workaround to https://code.google.com/p/android/issues/detail?id=191430
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            val text = text
            setText(null)
            setText(text)
        }
        return super.dispatchTouchEvent(event)
    }

    private fun setUnderline() {
        val str = SpannableString(text)

        str.setSpan(UnderlineText(),
                0,
                text.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        text = str
    }

    inner class UnderlineText : CharacterStyle() {
        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = true
        }
    }

    fun setColor(str: SpannableString,
                 rgbs: ArrayList<Int>,
                 begin: Int,
                 length: Int): SpannableString {
        str.setSpan(setColorText(rgbs[0], rgbs[1], rgbs[2]),
                begin,
                begin + length,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE)

        return str
    }

    fun setBold(str: SpannableString,
                begin: Int,
                length: Int): SpannableString {
        str.setSpan(BoldText(),
                begin,
                begin + length,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE)

        return str
    }

    fun setSize(str: SpannableString,
                size: Float,
                begin: Int,
                length: Int): SpannableString {
        str.setSpan(SizeText(size),
                begin,
                begin + length,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE)

        return str
    }


    fun setItalic(str: SpannableString,
                  begin: Int,
                  length: Int): SpannableString {
        str.setSpan(ItalicText(),
                begin,
                begin + length,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE)

        return str
    }

    fun setStrike(str: SpannableString,
                  begin: Int,
                  length: Int): SpannableString {
        str.setSpan(StrikethroughSpan(),
                begin,
                begin + length,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE)

        return str
    }


    private fun setUnderline(str: SpannableString,
                             begin: Int,
                             length: Int): SpannableString {
        str.setSpan(UnderlineText(),
                begin,
                begin + length,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE)

        return str
    }


    fun setHighlight(start: Int, end: Int) {
        val str = SpannableString(text)
        str.setSpan(HighLightText(),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE)

        text = str
    }

    fun setUnderlineText(start: Int, end: Int) {
        val str = SpannableString(text)

        str.setSpan(UnderlineText(),
                start,
                end,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        text = str
    }

    fun clearHighlightText(start: Int, end: Int) {
        val str = SpannableString(text)
        val styleSpans = str.getSpans(start, end, HighLightText::class.java)
        for (span in styleSpans) {
            str.removeSpan(span)
        }
        text = str
    }

    inner class HighLightText : ClickableSpan() {

        override fun updateDrawState(ds: TextPaint) {
            ds.color = context.resources.getColor(R.color.abc_background_cache_hint_selector_material_dark)

            ds.bgColor = context.resources.getColor(R.color.accent_material_dark)
//            ds.setARGB(255, 255, 255, 255)
//            performClick()
        }

        override fun onClick(widget: View) {
            val tv = widget as TextView
            val s = tv.text as Spanned
            val start = s.getSpanStart(this)
            val end = s.getSpanEnd(this)
            val theWord = s.subSequence(start, end).toString()
            clearHighlightText(start, end)
        }
    }


    inner class UnHighLightText : CharacterStyle() {
        override fun updateDrawState(ds: TextPaint) {
            ds.color = ds.linkColor
            ds.bgColor = 0
            ds.color = context.resources.getColor(R.color.abc_background_cache_hint_selector_material_dark)
        }
    }

    inner class ItalicText : CharacterStyle() {

        override fun updateDrawState(ds: TextPaint) {
            ds.typeface = Typeface.create(ds.typeface, Typeface.ITALIC);
        }
    }

    inner class BoldText : CharacterStyle() {

        override fun updateDrawState(ds: TextPaint) {
            ds.typeface = Typeface.DEFAULT_BOLD

            isClickable = false
        }
    }

    inner class SizeText(val size: Float) : CharacterStyle() {

        override fun updateDrawState(ds: TextPaint) {
            ds.textSize = size
            ds.typeface = Typeface.DEFAULT_BOLD
        }
    }


    inner class setColorText(val r: Int, val g: Int, val b: Int) : CharacterStyle() {

        override fun updateDrawState(ds: TextPaint) {
            ds.color = Color.rgb(r, g, b)
        }
    }


    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        println("onPrepareActionMode")

        val selStart = selectionStart
        val selEnd = selectionEnd
//        handler.post { setHighlight(selStart, selEnd) }
        return true
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        println("onActionItemClicked")
        when (item.itemId) {
            1 -> {
                if (isFocused) {
                    val selStart = selectionStart
                    val selEnd = selectionEnd
                    setHighlight(selStart, selEnd)
                }
                mode.finish()
                return true
            }
            2 -> {
                if (isFocused) {
                    val selStart = selectionStart
                    val selEnd = selectionEnd
                    clearHighlightText(selStart, selEnd)
                }
                mode.finish()
                return true
            }
            3 -> {
                if (isFocused) {
                    val selStart = selectionStart
                    val selEnd = selectionEnd
                    setUnderlineText(selStart, selEnd)
                }
                mode.finish()
                return true
            }
            else -> {
                mode.finish()
            }
        }
        return true
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        val selStart = selectionStart
        val selEnd = selectionEnd
//        handler.post { setHighlight(selStart, selEnd) }
        println("onCreateActionMode")

        // Remove the "select all" option
//        menu.removeItem(android.R.id.selectAll)
//        // Remove the "cut" option
//        menu.removeItem(android.R.id.cut)
//        // Remove the "copy all" option
//        menu.removeItem(android.R.id.copy)
        return true

    }

    override fun onDestroyActionMode(mode: ActionMode?) {
    }
}
