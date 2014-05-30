package com.mingrisoft;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class RabbitView extends View {
	public float bitmapX; // 兔子显示位置的X坐标
	public float bitmapY; // 兔子显示位置的Y坐标

	public RabbitView(Context context) { // 重写构造方法
		super(context);//
		bitmapX = 750; // 设置兔子的默认显示位置的X坐标
		bitmapY = 500; // 设置兔子的默认显示位置的Y坐标

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint paint = new Paint(); // 创建并实例化Paint的对象
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.rabbit); // 根据图片生成位图对象
		canvas.drawBitmap(bitmap, bitmapX, bitmapY, paint); // 绘制小兔子
		if (bitmap.isRecycled()) { // 判断图片是否回收
			bitmap.recycle(); // 强制回收图片
		}
	}
}
