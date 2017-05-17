package com.example.verticalseekbar;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/5/2.
 */

public class VerticalSeekBar extends View {
    private int width;
    private int height;
    private Paint paint;
    private int strokeColor;//描边的颜色
    private int progressedColor;//已进行的进度条颜色
    private int progress;//进度条的值
    private int max;//最大值
    private int gear;//档位
    private int backgroundColor;//设置的背景色
    private boolean isGear;//是否要设置档位
    private int thumbImage;//滑动块图片
    private int thumbImagePressed;//滑动块被按下时的图片
    private int imageId;//设置的滑动块的图片
    private OnTouchListener onTouchListener;//一个监听函数
    public VerticalSeekBar(Context context){
        super(context);
    }
    public VerticalSeekBar(Context context, AttributeSet attrs){
        super(context,attrs);
        paint=new Paint();
        //设置一些默认的属性
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        strokeColor=Color.GRAY;
        progressedColor=Color.BLUE;
        progress=20;//the default progress
        max=100;
        thumbImage=R.mipmap.ball;
        thumbImagePressed=R.mipmap.ball_select;
        imageId=thumbImage;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
        height=h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int progressValue=progress*height/max;//获得progress对应的实际数值
        if(progressValue<width*2/5) progressValue=width*2/5;//限制滑动块的下边界
        if(progressValue>(height-width*2/5)) progressValue=height-width*2/5;//限制滑动块的上边界
        canvas.drawColor(backgroundColor);
        //画出总的进度条，描边
        RectF rectF=new RectF((width/2)-(width/6),10,(width/2)+(width/6),height-10);//数字10是给上下边缘留一定空隙，不至于看不到边缘
        RectF progressArea=new RectF((width/2)-(width/6),height-progressValue,(width/2)+(width/6),height-10);
        paint.setColor(strokeColor);
        paint.setStyle(Paint.Style.STROKE);//描边
        canvas.drawRoundRect(rectF,width/3,20,paint);
        //画出档位线
        if(isGear){
            for(int i=gear-2;i>0;i--){
                canvas.drawLine(0,height*i/(gear-1),width/2-width/6,height*i/(gear-1),paint);
                canvas.drawLine(width/2+width/6,height*i/(gear-1),width,height*i/(gear-1),paint);
            }
        }
        //画出已经进行过的进度条，填充
        paint.setColor(progressedColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(progressArea,width/3,20,paint);
        //画出滑动块
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),imageId);
        Rect src=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        //滑动块显示的区域
        Rect dst=new Rect((width/2)-width*4/10,height-progressValue-width*4/10,width/2+width*4/10,height-progressValue+width*4/10);
        canvas.drawBitmap(bitmap,src,dst,new Paint());


    }
    public void setStrokeColor(int color){
        this.strokeColor=color;
        //invalidate();
    }
    public void setProgressedColor(int color){
        this.progressedColor=color;
        //invalidate();
    }
    public void setProgress(int progress){
        if(progress<0)progress=0;
        if(progress>max)progress=max;
        this.progress=progress;
        //invalidate();
    }
    public void setGearedProgress(int myGear){
        if(myGear<0)myGear=0;
        if(myGear>gear)myGear=gear;
        if(isGear){
            int x=max/(gear-1);
            setProgress(myGear*x);
        }
    }
    public void setMax(int max){
        if(max>0){
            this.max=max;
            //invalidate();
        }
    }
    public void setGears(int gear){
        if(gear<=2){
            isGear=false;
        }else{
            isGear=true;
        }
        this.gear=gear;
    }
    public void setBackgroundColor(int backgroundColor){
        this.backgroundColor=backgroundColor;
    }
    public void setThumb(int image){
        this.thumbImage=image;
    }
    public void setThumbImagePressed(int imagePressed){
        this.thumbImagePressed=imagePressed;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int viewYDown=(int)event.getY();
                progress=max-viewYDown*max/height;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                int viewYMove=(int)event.getY();
                progress=max-viewYMove*max/height;
                if(progress<0)progress=0;//限制progress的界限
                if(progress>max)progress=max;
                imageId=thumbImagePressed;
                invalidate();
                onTouchListener.onTouchMove(progress);
                break;
            case MotionEvent.ACTION_UP:
                int viewYUp=(int)event.getY();
                progress=max-viewYUp*max/height;
                if(progress<0)progress=0;//限制progress的界限
                if(progress>max)progress=max;
                imageId=thumbImage;
                invalidate();
                if(isGear){
                    int x=max/(gear-1);//最小刻度
                    int index=progress/x;//progress对x取模
                    int extra=progress%x;//progress对x取余，如果余数已经超出了最小刻度的一半，就让index++
                    if(extra>x/2)index++;
                    progress=index*x;
                    onTouchListener.onTouchUp(index);//如果有刻度的，就返回刻度值，index从0开始
                }else{
                    onTouchListener.onTouchUp(progress);//如果没有刻度，返回progress
                }
                setProgress(progress);
                break;
        }
        return true;
    }
    public void setOnTouchListener(OnTouchListener listener){
        this.onTouchListener=listener;
    }
    interface OnTouchListener{
        void onTouchMove(int progress);
        void onTouchUp(int progress);
    }
}