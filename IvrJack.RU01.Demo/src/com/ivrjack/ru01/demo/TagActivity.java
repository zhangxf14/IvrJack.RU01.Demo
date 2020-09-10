package com.ivrjack.ru01.demo;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;






import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.ivrjack.ru01.IvrJackService;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;

/**
 * Created by ChenRong1 on 2015/9/24 0024.
 */
@SuppressLint("ValidFragment")
@SuppressWarnings("deprecation")
public class TagActivity extends TabActivity {

    private Tab1 tab1;
    private Tab2 tab2;
    private Tab3 tab3;
    private Tab4 tab4;
    private Tab5 tab5;
    private Tab6 tab6;
    private ProductInfo mData =new ProductInfo();// 请把Tag读出来的内容set进这里
//    private ArrayList<TagTemperature> mDatas = new ArrayList<TagTemperature>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabHost tabHost = getTabHost();

        LayoutInflater.from(this).inflate(R.layout.tag, tabHost.getTabContentView(), true);

//        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Read").setContent(R.id.tab1));
//        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Write").setContent(R.id.tab2));
//        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Lock").setContent(R.id.tab3));
//        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("Kill").setContent(R.id.tab4));
//        tabHost.addTab(tabHost.newTabSpec("tab5").setIndicator("Temperature").setContent(R.id.tab5));
//        tabHost.addTab(tabHost.newTabSpec("tab6").setIndicator("Diagram").setContent(R.id.tab6));
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(getString(R.string.read)).setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(getString(R.string.write)).setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator(getString(R.string.lock)).setContent(R.id.tab3));
        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator(getString(R.string.kill)).setContent(R.id.tab4));
        tabHost.addTab(tabHost.newTabSpec("tab5").setIndicator(getString(R.string.temperature)).setContent(R.id.tab5));
        tabHost.addTab(tabHost.newTabSpec("tab6").setIndicator(getString(R.string.diagram)).setContent(R.id.tab6));

        tab1 = new Tab1();
        tab2 = new Tab2();
        tab3 = new Tab3();
        tab4 = new Tab4();
        
        tab5 = new Tab5();
        tab6 = new Tab6();

        tab1.init();
        tab2.init();
        tab3.init();
        tab4.init();
//        tab5.init();
//        tab6.init();
    }

    private void updateUi(boolean editing) {
        tab1.buttonRead.setEnabled(!editing);
        tab2.buttonWrite.setEnabled(!editing);
        tab3.buttonLock.setEnabled(!editing);
        tab4.buttonKill.setEnabled(!editing);
    }

    private int convertPassword(String parm, String str) {
        if (str.length() != 8) {
            throw new NumberFormatException(parm + " must be 8 bytes!");
        }
        try {
            return Integer.parseInt(str, 16);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(parm + " error format!");
        }
    }

    private byte convertByte(String parm, String str) {
        try {
            return (byte) Integer.parseInt(str, 16);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(parm + " error format!");
        }
    }

    private byte[] convertBytes(String parm, String str, int expectLen) {
        str = str.replace(" ", "");
        if (str.length() % 2 != 0) {
            throw new NumberFormatException(parm + " error length!");
        }
        if (expectLen > 0 && str.length() != expectLen) {
            throw new NumberFormatException(parm + " error length!");
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i=0; i<str.length(); i+=2) {
            try {
                bytes[i/2] = (byte) Integer.parseInt(str.substring(i, i + 2), 16);
            } catch (NumberFormatException e) {
                throw new NumberFormatException(parm + " error format!");
            }
        }
        return bytes;
    }

    class Tab1 {

        private TextView viewStatus;
        private TextView viewEpc;
        private EditText viewPwd;
        private EditText viewAddress;
        private RadioGroup viewBlocks;
        private RadioButton blockEpc;
        private RadioButton blockUser;
        private RadioButton blockRFU;
        private RadioButton blockTID;
        private EditText viewLength;
        private EditText viewData;
        private Button buttonRead;
        
        private byte[] blockData;
      
        
        public void init() {
            View v = findViewById(R.id.tab1);
            viewStatus = (TextView) v.findViewById(R.id.status);
            viewEpc = (TextView) v.findViewById(R.id.epc);
            viewPwd = (EditText) v.findViewById(R.id.accpwd);
            viewAddress = (EditText) v.findViewById(R.id.address);
            viewBlocks = (RadioGroup) v.findViewById(R.id.blockGroup);
            blockEpc=(RadioButton) v.findViewById(R.id.blockEpc);
            blockUser=(RadioButton) v.findViewById(R.id.blockUser);
            blockRFU=(RadioButton) v.findViewById(R.id.blockRFU);
            blockTID=(RadioButton) v.findViewById(R.id.blockTID);
            viewLength = (EditText) v.findViewById(R.id.length);
            viewData = (EditText) v.findViewById(R.id.data);
            buttonRead = (Button) v.findViewById(R.id.read);

            viewStatus.setText("");
            viewEpc.setText(getIntent().getStringExtra("epc"));
            mData.setUid(viewEpc.getText().toString());
            
            blockEpc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	 viewAddress.setText("2");
                	 viewLength.setText("6");
                	}
            });
            blockUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	 viewAddress.setText("0");
                	 viewLength.setText("20");
                	}
            });
            blockRFU.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	 viewAddress.setText("2");
                	 viewLength.setText("6");
                	}
            });
            blockTID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	 viewAddress.setText("2");
                	 viewLength.setText("6");
                	}
            });
            buttonRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    	
                        ReadTask task = new ReadTask();
                        task.epc = convertBytes("epc", viewEpc.getText().toString(), 0);
                        task.accpwd = convertPassword("accpwd", viewPwd.getText().toString());
                        task.address = convertByte("address", viewAddress.getText().toString());
                        task.block = getBlock();
                        task.length = convertByte("length", viewLength.getText().toString());

//                        viewStatus.setText("Reading...");
                        viewStatus.setText(R.string.reading);
                        viewData.setText("");
                        
                        updateUi(true);                       
                        
//                        if(blockUser.isChecked()){
//                        	for(int i=0;i<3;i++){
//                        		task.address=(byte)(20*i);
//                        		new Thread(task).start();
//                        	}
//                        }else{
//                        	task.address = convertByte("address", viewAddress.getText().toString());
                        	new Thread(task).start();
//                        }
                        
                        
                    } catch (NumberFormatException e) {
                        Toast.makeText(TagActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        private byte getBlock() {
            switch (viewBlocks.getCheckedRadioButtonId()) {
                case R.id.blockEpc:
                    return 0;
                case R.id.blockUser:
                    return 1;
                case R.id.blockRFU:
                    return 2;
                default:
                    return 3;
            }
        }

        class ReadTask implements Runnable {

            byte[] epc;
            int accpwd;
            byte address;
            byte block;
            byte length;

            @Override
            public void run() {
                DoneTask task = new DoneTask();
                int ret = MainActivity.service.selectTag(accpwd, epc);
                if (ret == 0) {
                    IvrJackService.TagBlock result = new IvrJackService.TagBlock();
                    ret = MainActivity.service.readTag(block, address, length, result);
                    if (ret == 0) {
                        task.data = result.data;
//                        task.msg = "Read tag success";
                        task.msg = getString(R.string.read_tag_success);
                    } else {
//                        task.msg = "Read tag failed: " + ret;
                    	task.msg = getString(R.string.read_tag_failed) + ret;
                    }
                } else {
                    task.msg = getString(R.string.select_tag_failed) + ret;
                }
                task.ret = ret;
                new Handler(Looper.getMainLooper()).post(task);
                
            }
        }

        class DoneTask implements Runnable {

            byte[] data;
            int ret;
            String msg;

            @Override
            public void run() {
                viewStatus.setText(msg);
                if (ret == 0) {
                    StringBuilder builder = new StringBuilder();
                    for (byte b : data) {
                        builder.append(String.format("%02X ", b));
                    }
                    viewData.setText(builder.toString());
//                    blockData=data; 
                    if(blockUser.isChecked()){                    		
	                    blockData=data;                    
//	                    System.arraycopy(data,0, blockData, 0, data.length);
	                    int iLength= Integer.parseInt(viewLength.getText().toString(),16);
	                    double[] totalTemperatures=new double[iLength];
	
	                    byte[] originData=blockData;
	                    int[] temps = new int[iLength];
	                    for(int i=0,j=0;i<originData.length;i+=2){
	                    	/*Dense Form -Only Internal Temperaature Sensor 压缩算法解析
	                    	temps[0]=  (((int)originData[0]<<2) | ((int)(originData[1]& 0xC0)>>6)) & 0x03FF;       //temp1
	                    	temps[1]=  ((((int)originData[1]& 0x3F) <<4) | (((int)originData[2]& 0xF0)>>4))& 0x03FF;//temp2
	                    	temps[2]=  (((((int)originData[2]& 0x0F)<<6) | (((int)originData[3]& 0xF3)>>2)))& 0x03FF;//temp3
	                    	temps[3]=  (((((int)originData[3]& 0x03)<<8) | ((int)originData[4])))& 0x03FF;//temp4
	                    	temps[4]=  ((((int)originData[5]<<2) | ((int)(originData[6]& 0x30)>>6)))& 0x03FF;//temp5
	                    	temps[5]=  (((((int)originData[6]& 0x3F)<<4) | (((int)originData[7]& 0xF0)>>4)))& 0x03FF;//temp6
	                    	temps[6]=  (((((int)originData[7]& 0x0F)<<6) | (((int)originData[8]& 0xFC)>>2)))& 0x03FF;//temp7
	                    	temps[7]=  (((((int)originData[8]& 0x03)<<8) | ((int)originData[9])))& 0x03FF;//temp8
	                    	*/
	                    	
	                    	temps[j++]=  (((((int)originData[i]& 0x03)<<8) | ((int)originData[i+1])))& 0x03FF;
	                    	if(j>iLength-1) break;
	                    }
	                    
	                	for(int i=0;i<temps.length;i++){                    		
	                		totalTemperatures[i]=convertTemperatureCodeToCelsius(temps[i]);
//	                    		totalTemperatures[i]=5.0+i;//test data
	                	}
	                	// 按照格式封装温度
	        			ArrayList<TagTemperature> mTemperatrues=new ArrayList<TagTemperature>();
	        			mData.setAllTemperature(totalTemperatures);
	        			mData.setInterval(60);
	        			Date datetime=new Date();  
//	        			long l1=datetime.getTime(),l2,l3;
//	        			l2=mData.getInterval()*iLength*1000;
//	        			l3=l1-l2;
	        			mData.setStartTime(new Date(datetime.getTime()-mData.getInterval()*iLength*1000));
	        			mData.setTemperatureMax(8.0);
	        			mData.setTemperatureMin(2.0);
	        			
	    				// 按照格式封装温度
	    				 mTemperatrues = packageTemperature(mData);
	    				//先删除历史数据
	    				 TagTemperatureDao.getInstance(getApplicationContext()).delete(mData.getUid());
	        			// 温度列表保存到数据库
	    				 TagTemperatureDao.getInstance(getApplicationContext()).insert(mTemperatrues);
	    				//数据读取完之后，再次初始化。。。
	    				 tab5.init();
	    			     tab6.init();
                    }	
                }
                updateUi(false);

            }
        }
         
        public double convertTemperatureCodeToCelsius(int temperatureCode) {
    		return temperatureCode * 0.18 - 89.3;
    	}
	    
        
        private ArrayList<TagTemperature> packageTemperature(ProductInfo mData) {
    		ArrayList<TagTemperature> mTagTemperatureInfo = packageTagTemperatureInfo();
//    		ArrayList<TagTemperature> mPackageMockInterval = packageMockInterval(mTagTemperatureInfo);
//    		if (mockIntervalMask != 0) {
//    			mData.setInterval(mData.getInterval() / (mockIntervalMask + 1));
//    		}
//    		ArrayList<TagTemperature> mMockTemperatrues = packageMockTemperature(mPackageMockInterval);
    		return mTagTemperatureInfo;//mMockTemperatrues;
    	}
	
        /**
    	 * 封装温度信息，完整记录格式对应数据库：
    	 * Id-time-temperature-status-temp Max-temp Min
    	 * 
    	 * @param tagInfo
    	 * @return
    	 */
    	private ArrayList<TagTemperature> packageTagTemperatureInfo() {

    		ArrayList<TagTemperature> temperatureList = new ArrayList<TagTemperature>();
    		int total = mData.getAllTemperature().length;
    		
    		for (int i = 0; i < total; i++) {
    			
    			TagTemperature tag = new TagTemperature();
    			tag.setTagId(mData.getUid());				
    				tag.setDate(new Date(mData.getStartTime().getTime()
    						+ mData.getInterval() * 1000 
    					    + mData.getInterval() * 1000 * i));    			

    			tag.setTemperature(mData.getAllTemperature()[i]);
    			tag.setTemperatureMax(mData.getTemperatureMax());
    			tag.setTemperatureMin(mData.getTemperatureMin());
    			tag.setExceededStatus(Constants.STATUS_NORMAL);
    			if (tag.getTemperature() > tag.getTemperatureMax()) {
    				tag.setExceededStatus(tag.getExceededStatus() | Constants.STATUS_EXCEEDED_MAX);
    			} else if (tag.getTemperature() < tag.getTemperatureMin()){
    				tag.setExceededStatus(tag.getExceededStatus() | Constants.STATUS_EXCEEDED_MIN);
    			} else {
    			}
    			temperatureList.add(tag);
    		}
    		return temperatureList;
    	}
    }

    class Tab2 {

        private TextView viewStatus;
        private TextView viewEpc;
        private EditText viewPwd;
        private EditText viewAddress;
        private RadioGroup viewBlocks;
        private EditText viewLength;
        private EditText viewData;
        private Button buttonWrite;

        public void init() {
            View v = findViewById(R.id.tab2);
            viewStatus = (TextView) v.findViewById(R.id.status);
            viewEpc = (TextView) v.findViewById(R.id.epc);
            viewPwd = (EditText) v.findViewById(R.id.accpwd);
            viewAddress = (EditText) v.findViewById(R.id.address);
            viewBlocks = (RadioGroup) v.findViewById(R.id.blockGroup);
            viewLength = (EditText) v.findViewById(R.id.length);
            viewData = (EditText) v.findViewById(R.id.data);
            buttonWrite = (Button) v.findViewById(R.id.write);

            viewStatus.setText("");
            viewEpc.setText(getIntent().getStringExtra("epc"));
            buttonWrite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        WriteTask task = new WriteTask();
                        task.epc = convertBytes("epc", viewEpc.getText().toString(), 0);
                        task.accpwd = convertPassword("accpwd", viewPwd.getText().toString());
                        task.address = convertByte("address", viewAddress.getText().toString());
                        task.block = getBlock();
                        task.length = convertByte("length", viewLength.getText().toString());
                        task.data = convertBytes("data", viewData.getText().toString(), task.length * 4);

//                        viewStatus.setText("Writing...");
                        viewStatus.setText(R.string.writing);
                        updateUi(true);
                        new Thread(task).start();
                    } catch (NumberFormatException e) {
                        Toast.makeText(TagActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        private byte getBlock() {
            switch (viewBlocks.getCheckedRadioButtonId()) {
                case R.id.blockEpc:
                    return 0;
                case R.id.blockUser:
                    return 1;
                default:
                    return 2;
            }
        }

        class WriteTask implements Runnable {

            byte[] epc;
            int accpwd;
            byte address;
            byte block;
            byte length;
            byte[] data;

            @Override
            public void run() {
                DoneTask task = new DoneTask();
                int ret = MainActivity.service.selectTag(accpwd, epc);
                if (ret == 0) {
                    ret = MainActivity.service.writeTag(block, address, length, data);
                    if (ret == 0) {
//                        task.msg = "Write tag success";
                        task.msg = getString(R.string.write_tag_success);
                    } else {
//                        task.msg = "Write tag failed: " + ret;
                    	task.msg = getString(R.string.writr_tag_failed) + ret;
                    }
                } else {
//                    task.msg = "Select tag failed: " + ret;
                	task.msg = getString(R.string.select_tag_failed) + ret;
                }
                new Handler(Looper.getMainLooper()).post(task);
            }
        }

        class DoneTask implements Runnable {

            String msg;

            @Override
            public void run() {
                viewStatus.setText(msg);
                updateUi(false);
            }
        }

    }

    class Tab3 {

        private TextView viewStatus;
        private TextView viewEpc;
        private EditText viewPwd;
        private RadioGroup viewLockObjects;
        private RadioGroup viewLockActions;
        private Button buttonLock;

        public void init() {
            View v = findViewById(R.id.tab3);
            viewStatus = (TextView) v.findViewById(R.id.status);
            viewEpc = (TextView) v.findViewById(R.id.epc);
            viewPwd = (EditText) v.findViewById(R.id.accpwd);
            viewLockObjects = (RadioGroup) v.findViewById(R.id.lockObjectGroup);
            viewLockActions = (RadioGroup) v.findViewById(R.id.lockActionGroup);
            buttonLock = (Button) v.findViewById(R.id.lock);

            viewStatus.setText("");
            viewEpc.setText(getIntent().getStringExtra("epc"));
            buttonLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        LockTask task = new LockTask();
                        task.epc = convertBytes("epc", viewEpc.getText().toString(), 0);
                        task.accpwd = convertPassword("accpwd", viewPwd.getText().toString());
                        task.lockObject = getLockObject();
                        task.lockAction = getLockAction();

//                        viewStatus.setText("Locking...");
                        viewStatus.setText(R.string.locking);
                        updateUi(true);
                        new Thread(task).start();
                    } catch (NumberFormatException e) {
                        Toast.makeText(TagActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        private byte getLockObject() {
            switch (viewLockObjects.getCheckedRadioButtonId()) {
                case R.id.objectEpc:
                    return 0;
                case R.id.objectUser:
                    return 1;
                case R.id.objectAPWD:
                    return 2;
                default:
                    return 3;
            }
        }

        private byte getLockAction() {
            switch (viewLockActions.getCheckedRadioButtonId()) {
                case R.id.actionUnlock:
                    return 0;
                case R.id.actionLock:
                    return 1;
                default:
                    return 2;
            }
        }

        class LockTask implements Runnable {

            byte[] epc;
            int accpwd;
            byte lockObject;
            byte lockAction;

            @Override
            public void run() {
                DoneTask task = new DoneTask();
                int ret = MainActivity.service.selectTag(accpwd, epc);
                if (ret == 0) {
                    ret = MainActivity.service.lockTag(accpwd, lockObject, lockAction);
                    if (ret == 0) {
//                        task.msg = "Lock tag success";
                    	task.msg = getString(R.string.lock_tag_success);
                    } else {
//                        task.msg = "Lock tag failed: " + ret;
                        task.msg = getString(R.string.lock_tag_failed) + ret;
                    }
                } else {
//                    task.msg = "Select tag failed: " + ret;
                	task.msg = getString(R.string.select_tag_failed) + ret;
                }
                new Handler(Looper.getMainLooper()).post(task);
            }
        }

        class DoneTask implements Runnable {

            String msg;

            @Override
            public void run() {
                viewStatus.setText(msg);
                updateUi(false);
            }
        }
    }

    class Tab4 {

        private TextView viewStatus;
        private TextView viewEpc;
        private EditText viewAccpwd;
        private EditText viewKillpwd;
        private Button buttonKill;

        public void init() {
            View v = findViewById(R.id.tab4);
            viewStatus = (TextView) v.findViewById(R.id.status);
            viewEpc = (TextView) v.findViewById(R.id.epc);
            viewAccpwd = (EditText) v.findViewById(R.id.accpwd);
            viewKillpwd = (EditText) v.findViewById(R.id.killpwd);
            buttonKill = (Button) v.findViewById(R.id.kill);

            viewStatus.setText("");
            viewEpc.setText(getIntent().getStringExtra("epc"));
            buttonKill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KillTask task = new KillTask();
                    task.epc = convertBytes("epc", viewEpc.getText().toString(), 0);
                    task.accpwd = convertPassword("accpwd", viewAccpwd.getText().toString());
                    task.killpwd = convertPassword("killpwd", viewKillpwd.getText().toString());

//                    viewStatus.setText("Killing...");
                    viewStatus.setText(R.string.killing);
                    updateUi(true);
                    new Thread(task).start();
                }
            });

        }

        class KillTask implements Runnable {

            byte[] epc;
            int accpwd;
            int killpwd;

            @Override
            public void run() {
                DoneTask task = new DoneTask();
                int ret = MainActivity.service.selectTag(accpwd, epc);
                if (ret == 0) {
                    ret = MainActivity.service.killTag(killpwd);
                    if (ret == 0) {
//                        task.msg = "Kill tag success";
                    	task.msg = getString(R.string.kill_tag_success);
                    } else {
//                        task.msg = "Kill tag failed: " + ret;
                    	task.msg = getString(R.string.kill_tag_failed) + ret;
                    }
                } else {
//                    task.msg = "Select tag failed: " + ret;
                	task.msg = getString(R.string.select_tag_failed) + ret;
                }
                new Handler(Looper.getMainLooper()).post(task);
            }
        }

        class DoneTask implements Runnable {

            String msg;

            @Override
            public void run() {
                viewStatus.setText(msg);
                updateUi(false);
            }
        }
    }

    
    
    //
    class Tab5 {
    	private ListView listView;
    	private ArrayList<TagTemperature> mDatas=new ArrayList<TagTemperature>();    	
    	private ArrayList<TagTemperature> mDatasEx=new ArrayList<TagTemperature>();
    	private ArrayList<TagTemperature> mDatasNor=new ArrayList<TagTemperature>();
    	private ExceededAdpt adapter;
    	private String uid;
    	private TextView tvTag,tvSubmit;
    	private boolean isNor=true;   	

    	protected void initViews() {
    		View v = findViewById(R.id.tab5);
    		listView=(ListView) v.findViewById(R.id.listView);
//    		headerView=LayoutInflater.from(getActivity()).inflate(R.layout.include_list_exceeded_head, null);
    		tvTag=(TextView) v.findViewById(R.id.tvTag);
    		tvSubmit=(TextView) v.findViewById(R.id.tvSubmit);
    	
    	}

    	protected void initEvents() {
    		tvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            		isNor=!isNor;            		
            		setContent(); 
                }
            });
    		
    	}

    	protected void init() {
    		this.uid=mData.getUid();
    		initViews();
    		initEvents();
//    		mDatas.addAll(TagTemperatureDao.getInstance(getApplicationContext()).queryAll(uid, "desc"));
    		loadData();
    		adapter=new ExceededAdpt(mDatas);
//    		listView.addHeaderView(headerView);
    		listView.setAdapter(adapter);
    		setContent();    		
    	}

    	
    	private void loadData(){
    		mDatasEx.clear();
    		mDatasNor.clear();
    		//查询出该uid对应的所有温度
    		mDatasEx.addAll(TagTemperatureDao.getInstance(getApplicationContext()).queryExceededAll(uid));
    		mDatasNor.addAll(TagTemperatureDao.getInstance(getApplicationContext()).queryAll(uid, "desc"));
    		
    	}
    	private void setContent(){
    		mDatas.clear();
    		if(isNor){
    			mDatas.addAll(mDatasNor);
    			tvTag.setText(getString(R.string.exceeded_nor, mDatasNor.size()));
    		}
    		else{
    			mDatas.addAll(mDatasEx);
    			tvTag.setText(getString(R.string.exceeded_ex, mDatasEx.size()));
    		}
    		adapter.notifyDataSetChanged();
    	}
    }
    
    class Tab6 {    	
    	
    	private String uid;
    	private ArrayList<TagTemperature> mDatas = new ArrayList<TagTemperature>();
    	private LinearLayout layout;
    	private EditText tvMin,tvMax;
    	private TextView tvSubmit;
    	boolean isClick=true;
    	private Button btnChange;
    	
    	public void initViews() {
    		View v = findViewById(R.id.tab6);
    		layout = (LinearLayout) v.findViewById(R.id.loContent);
    		tvMin=(EditText) v.findViewById(R.id.tvMin);
    		tvMax=(EditText) v.findViewById(R.id.tvMax);
    		tvSubmit=(TextView) v.findViewById(R.id.tvSubmit);
    		btnChange=(Button) v.findViewById(R.id.btnChange);
    		/*		
    		mDatas = TestData.getInstance(getActivity()).addData4();
    		TagTemperatureDao.getInstance(getActivity()).insert(mDatas); */
    	}   	
    	
    	public void initEvents(){
    		tvSubmit.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ShowToast")
				@Override
                public void onClick(View v) {
                	int min=Integer.valueOf(tvMin.getText().toString());
        			int max=Integer.valueOf(tvMax.getText().toString());
        			if(max>min){
        			    mDatas.clear();
        			    mDatas.addAll(TagTemperatureDao.getInstance(
        					getApplicationContext()).queryBetweenCount(uid,min,max));
        			    setContent(false);
        			}
        			else{
        			Toast.makeText(getApplicationContext(),R.string.mmError,Toast.LENGTH_LONG ).show();	;
        			}
                }
            });
    		
    		btnChange.setOnClickListener(new View.OnClickListener() {
               
				@Override
                public void onClick(View v) {
                	setContent(isClick);
    		    	isClick=!isClick;
                }
            });
    	}

    	public void init() {
    		this.uid=mData.getUid();
    		initViews();
    		initEvents();
    		
    		mDatas.clear();
    		mDatas.addAll(TagTemperatureDao.getInstance(
    				getApplicationContext()).queryAll(uid));
    		tvMin.setText("0");
    		tvMax.setText(String.valueOf(mDatas.size()));
    		setContent(false);
    	}


    	private void setContent(boolean enable){
    		layout.removeAllViews();
    		if(mDatas.isEmpty())return;
    		/*
    		 * use Date as x axis label
    		 */
    		GraphViewData[] dataTemperature = new GraphViewData[mDatas.size()];		
    		GraphViewData[] dataTemperatureMax = new GraphViewData[mDatas.size()];
    		GraphViewData[] dataTemperatureMin = new GraphViewData[mDatas.size()];
    		for (int count = 0; count < mDatas.size(); count++) {
    			 
    			dataTemperature[count] = new GraphViewData(count, mDatas.get(count)
    					.getTemperature());			
    			dataTemperatureMax[count] = new GraphViewData(count, mDatas.get(
    					count).getTemperatureMax());
    			dataTemperatureMin[count] = new GraphViewData(count, mDatas.get(
    					count).getTemperatureMin());
    		}
    		GraphViewSeries exampleSeriesTemperature = new GraphViewSeries(
    				getString(R.string.exceeded_list_head_temperature),
    				new GraphViewSeriesStyle(Color.rgb(137, 190, 34), 3),
    				dataTemperature);		

    		GraphViewSeries exampleSeriesTemperatureMax = new GraphViewSeries(
    				DataUtil.formatTemperature(Integer.parseInt(new DecimalFormat("0").format(mDatas.get(0).getTemperatureMax()))), new GraphViewSeriesStyle(
    						Color.rgb(255, 0, 0), 5), dataTemperatureMax);
    		GraphViewSeries exampleSeriesTemperatureMin = new GraphViewSeries(
    				DataUtil.formatTemperature(Integer.parseInt(new DecimalFormat("0").format(mDatas.get(0).getTemperatureMin()))), new GraphViewSeriesStyle(
    						Color.rgb(10, 35, 58), 5), dataTemperatureMin);
    		//LineGraphView graphView = new LineGraphView(getActivity(), "温度曲线图");
    		LineGraphView graphView = new LineGraphView(getApplicationContext(),getString(R.string.temperature_diagram));
    		// ((LineGraphView) graphView).setDrawBackground(true);
    		graphView.addSeries(exampleSeriesTemperature); // data
    		graphView.addSeries(exampleSeriesTemperatureMax); // data
    		graphView.addSeries(exampleSeriesTemperatureMin); // data
    		/*
    		 * date as label formatter
    		 */
    		graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
    			@Override
    			public String formatLabel(double value, boolean isValueX) {
    				if (isValueX) {
    					if (value >= mDatas.size()) {
    						return null;
    					} else {
    						return TimeUtil.getTimeWithoutYear(mDatas.get((int) value)
    								.getDate());// 底部时间
    					}
    				}
    				return null; // let graphview generate Y-axis label for us
    			}
    		});
    		// set legend #89BE22,#2B7EDE,#0A233A
    		graphView.setShowLegend(true);
    		graphView.setLegendAlign(LegendAlign.TOP);// 标识是在哪个位置
    		graphView.getGraphViewStyle().setLegendBorder(20);
    		graphView.getGraphViewStyle().setLegendSpacing(30);
    		graphView.getGraphViewStyle().setLegendWidth(150);// 标识背景长度
//    		graphView.getGraphViewStyle().setLegendMarginBottom(TRIM_MEMORY_BACKGROUND);

    		graphView.getGraphViewStyle().setNumVerticalLabels(7);
    		// graphView.setVerticalLabels(new String[] {"50", "40", "30",
    		// "20","10","0"});
    		graphView.getGraphViewStyle().setNumHorizontalLabels(3);
    		
    		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);  
    		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
    		int len = mDatas.size();
    		if (len <= 10) {
    			graphView.setViewPort(0, 0);// 前者与从第几页开始显示，后者与视图一页有多少数据有关
    			graphView.setScrollable(false);// 可拖动，跟上面关联			
    		} else {
    			graphView.setViewPort(0, 10);
    			graphView.setScrollable(true);// 可拖动，跟上面关联			
    		}
    		//是否画出小圆圈
//    		graphView.setDrawDataPoints(true);
    		if(enable){
    			graphView.setDrawDataPoints(true);
    		}else{
    			graphView.setDrawDataPoints(false);
    		}
    		//
    		graphView.setScalable(true);// 可缩放
    		layout.addView(graphView);
    	}    	
    	
    	
    }
}
