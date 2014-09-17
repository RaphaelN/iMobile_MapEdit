package com.supermap.imb.edit;

import java.text.DecimalFormat;

import com.supermap.data.Point;
import com.supermap.data.Unit;
import com.supermap.imb.appconfig.MyApplication;
import com.supermap.mapping.Action;
import com.supermap.mapping.Layers;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MeasureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.LinearLayout.LayoutParams;

public class MeasurePopup extends PopupWindow implements OnClickListener {
	
	private MapControl     mMapControl       = null;
	private LayoutInflater mInflater         = null;
	private View           mContentView      = null;
	private EditText       edt_measureresult = null;
	
	/**
	 * ���캯��
	 * @param mapcontrol
	 */
	public MeasurePopup(MapControl mapcontrol) {
		mMapControl = mapcontrol;
		mInflater = LayoutInflater.from(mMapControl.getContext());
		loadView();
		setContentView(mContentView);
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * ��ʼ������
	 */
	private void loadView(){
		mContentView = mInflater.inflate(R.layout.measurebar, null);
		mContentView.findViewById(R.id.btn_measurelength).setOnClickListener(this);
		mContentView.findViewById(R.id.btn_measurearea).setOnClickListener(this);
		edt_measureresult = (EditText)mContentView.findViewById(R.id.edt_measureresult);
		mMapControl.addMeasureListener(new MeasureListener() {
			
			@Override
			public void LengthMeasured(double arg0, Point arg1) {
				// TODO Auto-generated method stub
				
				Unit unit =mMapControl.getMap().getPrjCoordSys().getCoordUnit();
				if (unit == Unit.METER) {
					
					DecimalFormat df = new DecimalFormat("0.00");
					if (arg0<1000) {
						edt_measureresult.setText(" "+df.format(arg0)+"��");
					} else {
						edt_measureresult.setText(" "+df.format(arg0/1000)+"����");
					}					
				}				
			}
			
			@Override
			public void AreaMeasured(double arg0, Point arg1) {
				// TODO Auto-generated method stub
				
				Unit unit =mMapControl.getMap().getPrjCoordSys().getCoordUnit();
				if (unit == Unit.METER) {
					
					DecimalFormat df = new DecimalFormat("0.00");
					if (arg0<1000000) {
						edt_measureresult.setText(" "+df.format(arg0)+"ƽ����");
					} else {
						edt_measureresult.setText(" "+df.format(arg0/1000000)+"ƽ������");
					}
				}
			}
		});
	}
	
	/**
	 * ��ʾ����������
	 */
	public void show(){
		reset();
		showAtLocation(mMapControl.getRootView(), Gravity.LEFT|Gravity.TOP,MyApplication.dp2px(10), MyApplication.dp2px(80));
	}
	
	/**
	 * �رչ�����
	 */
	public void dismiss(){
		//���˵�����ʧʱ����ͼ����ΪPAN
		mMapControl.setAction(Action.PAN);
		edt_measureresult.setText("");
		super.dismiss();
	}
	@Override
	public void onClick(View v) {
		RadioButton radio = (RadioButton)v;
		Layers lys = mMapControl.getMap().getLayers();
		switch (radio.getId()) {
		case R.id.btn_measurelength:
			if(radio.isChecked()){
				mMapControl.setAction(Action.MEASURELENGTH);
				((EditText)mContentView.findViewById(R.id.edt_measureresult)).setText("");
			}
			break;
		case R.id.btn_measurearea:
			if(radio.isChecked()){
				mMapControl.setAction(Action.MEASUREAREA);
				((EditText)mContentView.findViewById(R.id.edt_measureresult)).setText("");
			}
			break;
		
		default:
			break;
		}
	}

	/**
	 * ���ð�ť
	 */
	private void reset(){
		edt_measureresult.setText("");
		((RadioButton)mContentView.findViewById(R.id.btn_measurelength)).setChecked(false);;
		((RadioButton)mContentView.findViewById(R.id.btn_measurearea)).setChecked(false);;
		((RadioButton)mContentView.findViewById(R.id.btn_receivefocus4)).setChecked(true);
	}
	
	/**
	 * ����
	 */
	public void cancel() {
		reset();
	}
}