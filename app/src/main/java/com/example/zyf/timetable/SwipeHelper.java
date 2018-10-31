package com.example.zyf.timetable;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class SwipeHelper implements View.OnTouchListener {
    ArrayList<WeekItem> lit;//0=未点亮的，1=已点亮的
    int itemWidth, itemHeight;
    int maxColumn, touchWidth, touchHeight;
    Rect[] rects = new Rect[DateHelper.endWeek];
    View item;
    boolean isItemMeasured=false;
    boolean inTouchArea=false;
    boolean isMultiple;
    int isSelected = -1;
    RecyclerView.LayoutManager manager;
    Context context;

    public SwipeHelper(RecyclerView.LayoutManager manager, Context context, boolean isMultiple) {
        this.manager = manager;
        this.context = context;
        this.isMultiple = isMultiple;
    }

    public void setStatusList(ArrayList<Boolean> list){
        lit = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            lit.add(new WeekItem((i+""), list.get(i)));
        }
    }

    public void setLayout(int column, int width, int height){
        maxColumn=column;
        touchWidth = width;
        touchHeight = height;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        item = manager.findViewByPosition(0);
        if (!isItemMeasured) {
            itemWidth = item.getWidth();
            itemHeight = item.getHeight();
            initRects(maxColumn, touchWidth, touchHeight);
            isItemMeasured=true;
        }

        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                for (int i = 0;i<lit.size();i++){
                    //如果触点进入第i个矩形
                    if (rects[i].contains((int)event.getX(), (int)event.getY())) {
                        //该格未被点亮
                        if (isMultiple) {
                            if (!inTouchArea && !lit.get(i).isLit()) {
                                ObjectAnimator litAnim = ObjectAnimator.ofArgb(manager.findViewByPosition(i), "backgroundColor", 0x01e1bee7, 0xffe1bee7);
                                litAnim.setDuration(200)
                                        .start();//点亮该区域
                                lit.get(i).setLit(true);//标示该周被选中
                                inTouchArea = true;//设置已进入区域
                            }
                            //该格已被点亮
                            else if (!inTouchArea && lit.get(i).isLit()) {
                                ObjectAnimator dimAnim = ObjectAnimator.ofArgb(manager.findViewByPosition(i), "backgroundColor", 0xffe1bee7, 0x01e1bee7);
                                dimAnim.setDuration(200)
                                        .start();//熄灭该区域
                                lit.get(i).setLit(false);//标示该周被取消选中
                                inTouchArea = true;//设置已进入区域
                            } else if (inTouchArea) {
                                //仍在区域内
                            }
                            //已知触点在某区域内，MotionEvent利用完毕
                            return true;
                            //触点不在第i个矩形内，继续遍历，不作处理，也不用写
                        }else{
                            //单选模式下
                            if (!inTouchArea) {
                                ObjectAnimator litAnim = ObjectAnimator.ofArgb(manager.findViewByPosition(i), "backgroundColor", 0x01e1bee7, 0xffe1bee7);
                                litAnim.setDuration(200)
                                        .start();//点亮该区域
                                lit.get(i).setLit(true);//标示该周被选中
                                isSelected=i;

                                dimOthers(i);
                                inTouchArea = true;//设置已进入区域
                            }
                            return true;
                        }
                    }
                }
                //若执行至此处只能是遍历完成后发现这次滑动不在任一矩形内
                inTouchArea=false;
                return true;
            case MotionEvent.ACTION_UP:
                //若已抬起则肯定不在区域内
                inTouchArea=false;
                //if(!isMultiple)dimOthers(isSelected);
                return true;
            default:
        }
        return true;
    }

    private void dimOthers(int litPosition){
        for (int i=0;i<lit.size();i++)
            if (i != litPosition && lit.get(i).isLit()){
                ObjectAnimator dimAnim = ObjectAnimator.ofArgb(manager.findViewByPosition(i), "backgroundColor", 0xffe1bee7, 0x01e1bee7);
                dimAnim.setDuration(200)
                        .start();//熄灭该区域
                lit.get(i).setLit(false);//标示该周被取消选中
            }
    }

    private void initRects(int maxColumn, int touchWidth, int touchHeight){
        int row, column;//第几行和第几列
        int paddingHorizontal = (int)(itemWidth/2 -dp2px(context,touchWidth/2));//item view中检测触摸区域横向边界距item view横向边界的宽度
        int paddingVertical = (int)(itemHeight/2-dp2px(context,touchHeight/2));//item view中检测触摸区域纵向边界距item view纵向边界的高度
        for (int i = 0;i<lit.size();i++){
            row = i/maxColumn+1;
            column = ((i+1)%maxColumn==0)?maxColumn:((i+1)%maxColumn);
            //MotionEvent的getX/getY方法均返回相对于监听View左上角的坐标，单位为px。
            rects[i]=new Rect(paddingHorizontal+(column-1)*(int)itemWidth,
                    paddingVertical+(row-1)*(int)itemHeight,
                    paddingHorizontal+(column-1)*(int)itemWidth+dp2px(context, touchWidth),
                    paddingVertical+(row-1)*(int)itemHeight+dp2px(context, touchHeight));
        }
    }

    private int dp2px(Context context,int dpValue){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,context.getResources().getDisplayMetrics());
    }

    public ArrayList<WeekItem> getLit() {
        return lit;
    }

    public void setLit(int index, boolean isLit){
        lit.get(index).setLit(isLit);
    }

    public void initLit(){
        for (WeekItem item:lit) {
            if(item.isLit()) {
                manager.findViewByPosition(lit.indexOf(item)).setBackgroundColor(0xffe1bee7);
                //点亮该区域
            }
        }
    }
}
