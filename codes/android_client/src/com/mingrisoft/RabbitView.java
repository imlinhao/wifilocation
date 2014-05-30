package com.mingrisoft;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class RabbitView extends View {
	public float bitmapX; // ������ʾλ�õ�X����
	public float bitmapY; // ������ʾλ�õ�Y����

	public RabbitView(Context context) { // ��д���췽��
		super(context);//
		bitmapX = 750; // �������ӵ�Ĭ����ʾλ�õ�X����
		bitmapY = 500; // �������ӵ�Ĭ����ʾλ�õ�Y����

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint paint = new Paint(); // ������ʵ����Paint�Ķ���
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.rabbit); // ����ͼƬ����λͼ����
		canvas.drawBitmap(bitmap, bitmapX, bitmapY, paint); // ����С����
		if (bitmap.isRecycled()) { // �ж�ͼƬ�Ƿ����
			bitmap.recycle(); // ǿ�ƻ���ͼƬ
		}
	}
}
