package com.ruby.teamone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.view.View;

public class PathEffectView extends View {

    public PathEffectView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        canvas.drawColor(Color.WHITE);
        //创建画笔
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        //创建路径效果
        PathEffect pathEffect = new DashPathEffect(new float[]{10f, 2f}, 0);
        paint.setPathEffect(pathEffect);
        //创建路径
        Path path = new Path();
        path.moveTo(0, 200);//起点
        path.lineTo(200, 0);//画一条线

        canvas.drawPath(path, paint);
        path.reset();
        //绘制一个矩形框
        path.moveTo(200, 200);
        path.lineTo(400, 200);
        path.lineTo(400, 400);
        path.lineTo(200, 400);
        path.lineTo(200, 200);
        paint.setPathEffect(pathEffect);
        canvas.drawPath(path, paint);

    }
}
