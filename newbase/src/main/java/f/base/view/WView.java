package f.base.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import f.base.R;

/**
 * Created by DN on 2018/6/15.
 */

public class WView extends View {

    private float controlWidth;
    private float controlHeight;
    private boolean isScrolling = false;
    private ArrayList<ItemObject> itemList = new ArrayList<ItemObject>();
    private ArrayList<String> dataList = new ArrayList<String>();
    private int downY;
    private long downTime = 0;
    private long goonTime = 200;
    private int goonDistence = 100;
    private Paint linePaint;
    private int lineColor = 0xff000000;
    private float lineWidth = 2f;
    private float normalFont = 14.0f;
    private float selectedFont = 22.0f;
    private int unitHeight = 50;
    private int itemNumber = 7;
    private int normalColor = 0xff000000;
    private int selectedColor = 0xffff0000;
    private float maskHight = 48.0f;
    private OnSelectListener onSelectListener;
    private boolean isEnable = true;
    private static final int REFRESH_VIEW = 0x001;
    private static final int MOVE_NUMBER = 5;
    private boolean noEmpty = true;
    private boolean isClearing = false;

    public WView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
        initData();
    }

    public WView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        initData();
    }

    public WView(Context context) {
        super(context);
        initData();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnable)
            return true;
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isScrolling = true;
                downY = (int) event.getY();
                downTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(y - downY);
                onSelectListener();
                break;
            case MotionEvent.ACTION_UP:
                int move = Math.abs(y - downY);
                if (System.currentTimeMillis() - downTime < goonTime&& move > goonDistence) {
                    goonMove(y - downY);
                } else {
                    actionUp(y - downY);
                }
                noEmpty();
                isScrolling = false;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLine(canvas);
        drawList(canvas);
        drawMask(canvas);
    }

    private synchronized void drawList(Canvas canvas) {
        if (isClearing)
            return;
        try {
            for (ItemObject itemObject : itemList) {
                itemObject.drawSelf(canvas, getMeasuredWidth());
            }
        } catch (Exception e) {
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        controlWidth = getWidth();
        if (controlWidth != 0) {
            setMeasuredDimension(getWidth(), itemNumber * unitHeight);
            controlWidth = getWidth();
        }

    }

    private synchronized void goonMove(final int move) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int distence = 0;
                while (distence < unitHeight * MOVE_NUMBER) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    actionThreadMove(move > 0 ? distence : distence * (-1));
                    distence += 10;
                }
                actionUp(move > 0 ? distence - 10 : distence * (-1) + 10);
                noEmpty();
            }
        }).start();
    }

    private void noEmpty() {
        if (!noEmpty)
            return;
        if(null==itemList||itemList.size()<=0)return;
        for (ItemObject item : itemList) {
            if (item.isSelected())
                return;
        }
        int move = (int) itemList.get(0).moveToSelected();
        if (move < 0) {
            defaultMove(move);
        } else {
            defaultMove((int) itemList.get(itemList.size() - 1).moveToSelected());
        }
        for (ItemObject item : itemList) {
            if (item.isSelected()) {
                if (onSelectListener != null)
                    onSelectListener.endSelect(item.id, item.itemText);
                break;
            }
        }
    }

    private void initData() {
        isClearing = true;
        itemList.clear();
        for (int i = 0; i < dataList.size(); i++) {
            ItemObject itmItemObject = new ItemObject();
            itmItemObject.id = i;
            itmItemObject.itemText = dataList.get(i);
            itmItemObject.x = 0;
            itmItemObject.y = i * unitHeight;
            itemList.add(itmItemObject);
        }
        isClearing = false;
    }

    private void actionMove(int move) {
        for (ItemObject item : itemList) {
            item.move(move);
        }
        invalidate();
    }

    private void actionThreadMove(int move) {
        for (ItemObject item : itemList) {
            item.move(move);
        }
        Message rMessage = new Message();
        rMessage.what = REFRESH_VIEW;
        handler.sendMessage(rMessage);
    }

    private void actionUp(int move) {
        int newMove = 0;
        if (move > 0) {
            for (int i = 0; i < itemList.size(); i++) {
                if (itemList.get(i).isSelected()) {
                    newMove = (int) itemList.get(i).moveToSelected();
                    if (onSelectListener != null)
                        onSelectListener.endSelect(itemList.get(i).id,itemList.get(i).itemText);
                    break;
                }
            }
        } else {
            for (int i = itemList.size() - 1; i >= 0; i--) {
                if (itemList.get(i).isSelected()) {
                    newMove = (int) itemList.get(i).moveToSelected();
                    if (onSelectListener != null)
                        onSelectListener.endSelect(itemList.get(i).id,itemList.get(i).itemText);
                    break;
                }
            }
        }
        for (ItemObject item : itemList) {
            item.newY(move + 0);
        }
        slowMove(newMove);
        Message rMessage = new Message();
        rMessage.what = REFRESH_VIEW;
        handler.sendMessage(rMessage);

    }

    private synchronized void slowMove(final int move) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int m = move > 0 ? move : move * (-1);
                int i = move > 0 ? 1 : (-1);
                int speed = 1;
                while (true) {
                    m = m - speed;
                    if (m <= 0) {
                        for (ItemObject item : itemList) {
                            item.newY(m * i);
                        }
                        Message rMessage = new Message();
                        rMessage.what = REFRESH_VIEW;
                        handler.sendMessage(rMessage);
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    for (ItemObject item : itemList) {
                        item.newY(speed * i);
                    }
                    Message rMessage = new Message();
                    rMessage.what = REFRESH_VIEW;
                    handler.sendMessage(rMessage);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (ItemObject item : itemList) {
                    if (item.isSelected()) {
                        if (onSelectListener != null)
                            onSelectListener.endSelect(item.id, item.itemText);
                        break;
                    }
                }

            }
        }).start();
    }

    private void defaultMove(int move) {
        for (ItemObject item : itemList) {
            item.newY(move);
        }
        Message rMessage = new Message();
        rMessage.what = REFRESH_VIEW;
        handler.sendMessage(rMessage);
    }

    private void onSelectListener() {
        if (onSelectListener == null)
            return;
        if(null==itemList||itemList.size()<=0)return;
        for (ItemObject item : itemList) {
            if (item.isSelected()) {
                onSelectListener.selecting(item.id, item.itemText);
            }
        }
    }

    private void drawLine(Canvas canvas) {

        if (linePaint == null) {
            linePaint = new Paint();
            linePaint.setColor(lineColor);
            linePaint.setAntiAlias(true);
            linePaint.setStrokeWidth(lineWidth);
        }

        canvas.drawLine(0, controlHeight / 2 - unitHeight / 2 + 2,controlWidth, controlHeight / 2 - unitHeight / 2 + 2, linePaint);
        canvas.drawLine(0, controlHeight / 2 + unitHeight / 2 - 2,controlWidth, controlHeight / 2 + unitHeight / 2 - 2, linePaint);
    }

    private void drawMask(Canvas canvas) {
        LinearGradient lg = new LinearGradient(0, 0, 0, maskHight, 0x00f2f2f2,0x00f2f2f2, Shader.TileMode.MIRROR);
        Paint paint = new Paint();
        paint.setShader(lg);
        canvas.drawRect(0, 0, controlWidth, maskHight, paint);

        LinearGradient lg2 = new LinearGradient(0, controlHeight - maskHight,0, controlHeight, 0x00f2f2f2, 0x00f2f2f2, Shader.TileMode.MIRROR);
        Paint paint2 = new Paint();
        paint2.setShader(lg2);
        canvas.drawRect(0, controlHeight - maskHight, controlWidth,controlHeight, paint2);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray attribute = context.obtainStyledAttributes(attrs, R.styleable.WView);
        unitHeight = (int) attribute.getDimension(R.styleable.WView_unitHight, 32);
        normalFont = attribute.getDimension( R.styleable.WView_normalTextSize, 14.0f);
        selectedFont = attribute.getDimension(R.styleable.WView_selectedTextSize, 22.0f);
        itemNumber = attribute.getInt(R.styleable.WView_itemNumber, 7);
        normalColor = attribute.getColor(R.styleable.WView_normalTextColor, 0xff000000);
        selectedColor = attribute.getColor( R.styleable.WView_selectedTextColor, 0xffff0000);
        lineColor = attribute.getColor(R.styleable.WView_lineColor,0xff000000);
        lineWidth = attribute.getDimension(R.styleable.WView_lineHeight, 2f);
        maskHight = attribute.getDimension(R.styleable.WView_maskHight,48.0f);
        noEmpty = attribute.getBoolean(R.styleable.WView_noEmpty, true);
        isEnable = attribute.getBoolean(R.styleable.WView_isEnable, true);
        attribute.recycle();
        controlHeight = itemNumber * unitHeight;

    }

    public void setData(ArrayList<String> data) {
        this.dataList = data;
        initData();
    }

    public void resetData(ArrayList<String> data) {
        setData(data);
        postInvalidate();
    }

    public int getSelected() {
        for (ItemObject item : itemList) {
            if (item.isSelected())
                return item.id;
        }
        return -1;
    }

    public String getSelectedText() {
        for (ItemObject item : itemList) {
            if (item.isSelected())
                return item.itemText;
        }
        return "";
    }

    public boolean isScrolling() {
        return isScrolling;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public void setDefault(int index) {
        if (index > itemList.size() - 1)
            return;
        float move = itemList.get(index).moveToSelected();
        defaultMove((int) move);
    }

    public int getListSize() {
        if (itemList == null)
            return 0;
        return itemList.size();
    }

    public String getItemText(int index) {
        if (itemList == null)
            return "";
        return itemList.get(index).itemText;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    invalidate();
                    break;
                default:
                    break;
            }
        }

    };


    class ItemObject {
        public int id = 0;
        public String itemText = "";
        public int x = 0;
        public int y = 0;
        public int move = 0;
        private TextPaint textPaint;
        private Rect textRect;

        public ItemObject() {
            super();
        }

        public void drawSelf(Canvas canvas, int containerWidth) {

            if (textPaint == null) {
                textPaint = new TextPaint();
                textPaint.setAntiAlias(true);
            }
            if (textRect == null)
                textRect = new Rect();
            if (isSelected()) {
                textPaint.setColor(selectedColor);
                float moveToSelect = moveToSelected();
                moveToSelect = moveToSelect > 0 ? moveToSelect : moveToSelect
                        * (-1);
                float textSize = (float) normalFont+ ((float) (selectedFont - normalFont) * (1.0f - (float) moveToSelect/ (float) unitHeight));
                textPaint.setTextSize(textSize);
            } else {
                textPaint.setColor(normalColor);
                textPaint.setTextSize(normalFont);
            }
            itemText = (String) TextUtils.ellipsize(itemText, textPaint, containerWidth,TextUtils.TruncateAt.END);
            textPaint.getTextBounds(itemText, 0, itemText.length(), textRect);
            if (!isInView())
                return;
            canvas.drawText(itemText, x + controlWidth / 2 - textRect.width()/ 2, y + move + unitHeight / 2 + textRect.height() / 2,textPaint);

        }

        public boolean isInView() {
            if (y + move > controlHeight|| (y + move + unitHeight / 2 + textRect.height() / 2) < 0)
                return false;
            return true;
        }

        public void move(int _move) {
            this.move = _move;
        }

        public void newY(int _move) {
            this.move = 0;
            this.y = y + _move;
        }

        public boolean isSelected() {
            if ((y + move) >= controlHeight / 2 - unitHeight / 2 + 2&& (y + move) <= controlHeight / 2 + unitHeight / 2 - 2)
                return true;
            if ((y + move + unitHeight) >= controlHeight / 2 - unitHeight / 2+ 2 && (y + move + unitHeight) <= controlHeight / 2+ unitHeight / 2 - 2)
                return true;
            if ((y + move) <= controlHeight / 2 - unitHeight / 2 + 2&& (y + move + unitHeight) >= controlHeight / 2+ unitHeight / 2 - 2)
                return true;
            return false;
        }

        public float moveToSelected() {
            return (controlHeight / 2 - unitHeight / 2) - (y + move);
        }
    }


    public interface OnSelectListener {
        void endSelect(int id, String text);

        void selecting(int id, String text);
    }


}
