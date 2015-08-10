package com.bsz.hanyue.hmaptoview.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bsz.hanyue.hlocatormodel.Model.Coordinate;
import com.bsz.hanyue.hlocatormodel.Model.Icon;
import com.bsz.hanyue.hlocatormodel.Model.Map;
import com.bsz.hanyue.hmaptoview.Interface.OnIconClickListener;
import com.bsz.hanyue.hmaptoview.Interface.OnPointInputListener;
import com.bsz.hanyue.hmaptoview.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hanyue on 2015/7/26
 */
public class HMapView extends View {

    public static final int DEVELOP_MODEL = 0x000001;
    public static final int CUSTOM_MODEL = 0x000002;

    private static class AnimationHandler extends Handler{
        private final WeakReference<HMapView> hMapViewWeakReference;

        public AnimationHandler(HMapView hMapView) {
            hMapViewWeakReference = new WeakReference<>(hMapView);
        }

        @Override
        public void handleMessage(Message msg) {
            HMapView hMapView = hMapViewWeakReference.get();
            if(hMapView != null) {
                switch (msg.what) {
                    case 0:
                        hMapView.invalidate();
                        break;
                }
                super.handleMessage(msg);
            }
        }
    }

    private final AnimationHandler animationHandler = new AnimationHandler(this);

    //view model
    int model = CUSTOM_MODEL;
    //监听地图点击事件
    private List<OnPointInputListener> pointInputListeners;//to the develop model
    private List<OnIconClickListener> iconClickListeners;
    //要展示的图片
    private Bitmap bitmapDisplay;
    //地图model
    private Map map;
    //图片缩放比例
    private float bitmapScaleTimes;
    //cavans 左上顶点位置
    private Coordinate pointO;
    //地理坐标在图上位置
    private Coordinate geoInPicPoint;
    //气泡坐标（在图上）
    private Coordinate bubblePoint;
    //是否锁定到定位点
    private boolean mapLock = false;


    public HMapView(Context context) {
        this(context, null);
    }

    public HMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        pointInputListeners = new ArrayList<>();
        iconClickListeners = new ArrayList<>();
//        //解析xml属性设置
//        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HMapView, defStyleAttr, 0);
//        //得到设置的图片
//        Drawable b = a.getDrawable(R.styleable.HMapView_src);
//        if (b != null) {
//
//        }
    }

    //初始化地图
    private void initView() {
        initPoint();
        scaleBitmapToFitScreen();
        invalidate();
        animateTimer.cancel();
    }

    //初始化各点
    private void initPoint() {
        pointO = new Coordinate(0, 0);
        geoInPicPoint = new Coordinate(0, 0);
        bubblePoint = new Coordinate(0, 0);
    }

//    //将得到的drawalbe转化为bitmap
//    private Bitmap drawableToBitmap(Drawable drawable) {
//        int width = drawable.getIntrinsicWidth();
//        int height = drawable.getIntrinsicHeight();
//        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, width, height);
//        drawable.draw(canvas);
//        return bitmap;
//    }

    //缩放bitmap以适应屏幕显示
    private void scaleBitmapToFitScreen() {
        bitmapDisplay = map.getMapimage();
        float times1;
        float times2;
        float w = getWidth();
        float h = getHeight();
        float bW = bitmapDisplay.getWidth();
        float bH = bitmapDisplay.getHeight();
        times1 = w / bW;
        times2 = h / bH;
        if (times1 > times2) {
            bitmapScaleTimes = times1;
        } else {
            bitmapScaleTimes = times2;
        }
        scaleBitmap();
    }

    //缩放缓存图片为显示图片
    private void scaleBitmap() {
        Matrix matrix = new Matrix();
        matrix.postScale(bitmapScaleTimes, bitmapScaleTimes);
        bitmapDisplay = Bitmap.createBitmap(map.getMapimage(),
                0,
                0,
                map.getMapimage().getWidth(),
                map.getMapimage().getHeight(),
                matrix,
                true);
    }

    private float lastX;
    private float lastY;
    private int clickCount = 0;
    private long firClick;

    /**
     * 触摸事件响应
     */
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        switch (event.getAction()) {
            //处理按下事件
            case MotionEvent.ACTION_DOWN:
                //点击次数记录
                clickCount++;
                lastX = event.getRawX();
                lastY = event.getRawY();
                //双击事件判定
                if (clickCount == 1) {
                    firClick = System.currentTimeMillis();
                } else if (clickCount == 2) {
                    if (System.currentTimeMillis() - firClick < 300) {
                        //双击事件
                        Log.v("map", "double click");
                        invalidate();
                        mapLock = true;
                    }
                    clickCount = 0;
                    firClick = 0;
                }
                //非定位模式下，检测点击位置
                if (event.getPointerCount() == 1 && !mapLock) {
                    //解析点击位置为图上坐标
                    getInputPoint(event.getX(0), event.getY(0));
                    invalidate();
                }
                break;
            //处理移动事件
            case MotionEvent.ACTION_MOVE:
                //双指滑动
                if (event.getPointerCount() == 2) {
                    mapLock = false;
                    float dx = event.getX(0) - lastX;
                    float dy = event.getY(0) - lastY;
                    //得到滑动后O点位置
                    pointO.setX(pointO.getX() + dx);
                    pointO.setY(pointO.getY() + dy);
                    invalidate();
                    lastX = event.getX(0);
                    lastY = event.getY(0);
                }
                break;
//            case MotionEvent.ACTION_UP:
//                if (moved){
//                    moved = false;
//                }else {
//                    Log.v("map","input");
//                    hasInputPoint = true;
//                    state = 1;
//                    invalidate();
//                }
        }

        return true;
    }

    //解析点击位置为图上坐标
    private void getInputPoint(float x, float y) {
        float dx = x - pointO.getX();
        float dy = y - pointO.getY();
        Coordinate inputPoint = new Coordinate(dx,dy);
        if (model == CUSTOM_MODEL) {
            isIcon(inputPoint);
        }
        if (dx < 0) {
            inputPoint.setX(0);
        }
        if (dy < 0) {
            inputPoint.setY(0);
        }
        if (dx > bitmapDisplay.getWidth()) {
            inputPoint.setX(bitmapDisplay.getWidth());
        }
        if (dy > bitmapDisplay.getHeight()) {
            inputPoint.setY(bitmapDisplay.getHeight());
        }
        if (model == DEVELOP_MODEL) {
            notifyInput(inputPoint);
        }
    }

    //判断点击位置是否为图标区域
    private void isIcon(Coordinate inputPoint) {
        Coordinate hInputPoint = new Coordinate(
                inputPoint.getX() / bitmapScaleTimes,
                inputPoint.getY() / bitmapScaleTimes);
        if (map.getIconList() != null && map.getIconList().size() != 0) {
            for (Icon icon : map.getIconList()) {
                if ((hInputPoint.getX() - icon.getX()) >= 0) {
                    if ((hInputPoint.getX() - icon.getX()) < icon.getWidth() / 2) {
                        if ((hInputPoint.getY() - icon.getY()) >= 0) {
                            if ((hInputPoint.getY() - icon.getY()) < icon.getHeight() / 2) {
                                notifyIconClick(icon);
                                bubblePoint = new Coordinate(
                                        icon.getX() * bitmapScaleTimes,
                                        icon.getY() * bitmapScaleTimes);
                                mapLock = false;
                            }
                        } else {
                            if ((hInputPoint.getY() - icon.getY()) > (-icon.getHeight() / 2)) {
                                notifyIconClick(icon);
                                bubblePoint = new Coordinate(
                                        icon.getX() * bitmapScaleTimes,
                                        icon.getY() * bitmapScaleTimes);
                                mapLock = false;
                            }
                        }
                    }
                } else {
                    if ((hInputPoint.getX() - icon.getX()) > (-icon.getWidth() / 2)) {
                        if ((hInputPoint.getY() - icon.getY()) >= 0) {
                            if ((hInputPoint.getY() - icon.getY()) < icon.getHeight() / 2) {
                                notifyIconClick(icon);
                                bubblePoint = new Coordinate(
                                        icon.getX() * bitmapScaleTimes,
                                        icon.getY() * bitmapScaleTimes);
                                mapLock = false;
                            }
                        } else {
                            if ((hInputPoint.getY() - icon.getY()) > (-icon.getHeight() / 2)) {
                                notifyIconClick(icon);
                                bubblePoint = new Coordinate(
                                        icon.getX() * bitmapScaleTimes,
                                        icon.getY() * bitmapScaleTimes);
                                mapLock = false;
                            }
                        }
                    }
                }
            }
        }
    }

    //将图上坐标转为地理坐标
    private Coordinate inputPointToGeo(Coordinate inputPoint) {
        return  new Coordinate(
                inputPoint.getX() / bitmapScaleTimes * map.getRuler(),
                inputPoint.getY() / bitmapScaleTimes * map.getRuler());
    }

    //通知点击事件
    private void notifyInput(Coordinate inputPoint) {
        Coordinate geoCoordinate = inputPointToGeo(inputPoint);
        setLocation(geoCoordinate);
        for (OnPointInputListener pointInputListener : pointInputListeners) {
            pointInputListener.onPointInput(geoCoordinate);
        }
    }

    //通知图标点击事件
    private void notifyIconClick(Icon icon) {
        for (OnIconClickListener iconClickListener : iconClickListeners) {
            iconClickListener.onIconClick(icon);
        }
    }

    private Timer animateTimer;
    private boolean animateLock = false;
    private int animateCount = 0;

    //帧动画控制器
    private void doFrameAnimate(int fps) {
        animateLock = true;
        animateTimer = new Timer();
        animateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                animationHandler.sendEmptyMessage(0);
                animateCount++;
            }
        }, 1000 / fps, 1000 / fps);
    }

    //按图层绘制cavans
    private void drawing(Canvas canvas) {
        if (bitmapDisplay != null) {
            setClickable(true);
            drawTranslate(canvas);
            drawMap(canvas);
            if (!(map.getIconList() == null || map.getIconList().size() == 0)) {
                drawIcon(canvas);
            }
            drawPoint(canvas);
            if (bubblePoint != null) {
                if (!(bubblePoint.getX() == 0 && bubblePoint.getY() == 0)) {
                    drawBubble(canvas);
                }
            }
        } else {
            drawEmptyMap(canvas);
            setClickable(false);
        }
    }

    //绘制图片位置
    private void drawTranslate(Canvas canvas) {
        if (mapLock) {
            canvas.translate(getWidth() / 2 - geoInPicPoint.getX(), getHeight() / 2 - geoInPicPoint.getY());
        } else {
            canvas.translate(getWidth() / 2 - bubblePoint.getX(), getHeight() / 2 - bubblePoint.getY());
        }
    }

    //绘制要显示的地图
    private void drawMap(Canvas canvas) {
        if (bitmapDisplay != null) {
            //hCanvas.translate(pointO.getX(), pointO.getY());
            canvas.drawBitmap(bitmapDisplay, 0, 0, null);
        }
    }

    //绘制空地图，并显示加载信息及动画
    private void drawEmptyMap(Canvas canvas) {
        if (!animateLock) {
            doFrameAnimate(1);
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        canvas.drawBitmap(bitmap, getWidth() / 2 - bitmap.getWidth() / 2, getHeight() / 2 - bitmap.getHeight() / 2, null);
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        Rect bounds = new Rect();
        String hint = "正在加载地图 ing...";
        switch (animateCount) {
            case 0:
                hint = "正在加载地图 ing.";
                break;
            case 1:
                hint = "正在加载地图 ing..";
                break;
            case 2:
                hint = "正在加载地图 ing...";
                break;
            case 3:
                hint = "正在加载地图 ing.";
                animateCount = 0;
                break;
            default:
                animateCount = 0;
        }
        paint.getTextBounds(hint, 0, hint.length(), bounds);
        canvas.drawText(hint, getWidth() / 2 - bounds.width() / 2, getHeight() / 2 + bitmap.getHeight() / 2 + bounds.height() / 2 + 10, paint);
//        paint.reset();
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.FILL);
//        Path path = new Path();
    }

    //绘制图标
    private void drawIcon(Canvas canvas) {
        List<Icon> icons = map.getIconList();
        for (Icon icon : icons) {
            Bitmap iconBitmap = BitmapFactory.decodeFile(icon.getIconimageurl());
            canvas.drawBitmap(iconBitmap, icon.getX() * bitmapScaleTimes, icon.getY() * bitmapScaleTimes, null);
        }
    }

    //绘制定位点/输入点（开发用）
    private void drawPoint(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        if (geoInPicPoint != null) {
            if (!(geoInPicPoint.getX()==0&&geoInPicPoint.getY()==0)) {
                canvas.drawCircle(geoInPicPoint.getX(), geoInPicPoint.getY(), 20, paint);
            }
        }
//        if (inputPoint != null) {
//            canvas.drawCircle(inputPoint.getX(), inputPoint.getY(), 10, paint);
//        }
    }

    //绘制气泡
    private void drawBubble(Canvas canvas) {
        Bitmap bubbleBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        canvas.drawBitmap(bubbleBitmap, bubblePoint.getX(), bubblePoint.getY(), null);
    }

    //系统方法，测量控件宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //系统方法，绘制控件
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawing(canvas);

    }

    //注册监听者
    public void setOnPointInputListener(OnPointInputListener pointInputListener) {
        this.pointInputListeners.add(pointInputListener);
    }

    //移除监听者
    public void removeOnPointInputListener(OnPointInputListener pointInputListener) {
        this.pointInputListeners.remove(pointInputListener);
    }

    //注册监听者
    public void setOnIconClickListener(OnIconClickListener iconClickListener) {
        this.iconClickListeners.add(iconClickListener);
    }

    //移除监听者
    public void removeOnIconClickListener(OnIconClickListener iconClickListener) {
        this.iconClickListeners.remove(iconClickListener);
    }

    //输入地理坐标
    public void setLocation(Coordinate geoPoint) {
        getPicPoint(geoPoint);
        invalidate();
    }

    //将地理坐标转为图片坐标（定位用）
    private void getPicPoint(Coordinate geoPoint) {
        geoInPicPoint.setX(geoPoint.getX() / map.getRuler() * bitmapScaleTimes);
        geoInPicPoint.setY(geoPoint.getY() / map.getRuler() * bitmapScaleTimes);
    }

    public void setMap(Map map) {
        this.map = map;
        initView();
    }

    public void lockMap(){
        mapLock = true;
    }

    public void unLockMap(){
        mapLock = false;
    }

    public void setModel(int model){
        this.model = model;
    }

}
