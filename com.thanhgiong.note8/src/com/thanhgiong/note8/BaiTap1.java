package com.thanhgiong.note8;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaiTap1 extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = new LinearLayout(this);
		
	 TextView hoten = new TextView(this);
	 TextView ngaysinh = new TextView(this);
	 TextView gioitinh = new TextView(this);
	 TextView noio = new TextView(this);
	 hoten.setText("Pham Tuan");
	 ngaysinh.setText("08/10/1980");
	 gioitinh.setText("Nam");
	 noio.setText("Hanoi");
	 layout.addView(hoten);
	 layout.addView(ngaysinh);
	 layout.addView(gioitinh);
	 layout.addView(noio);
	 setContentView(layout.getId());
	 
	}
}
