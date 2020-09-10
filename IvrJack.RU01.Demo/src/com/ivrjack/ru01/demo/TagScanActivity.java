package com.ivrjack.ru01.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ivrjack.ru01.IvrJackService;
import com.ivrjack.ru01.IvrJackStatus;

/**
 * Created by ChenRong02 on 2016-04-29.
 */
public class TagScanActivity extends Activity {

    private IvrJackService service;
    private ProgressDialog dialog;
    private View viewTagScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_scan);
        service = MainActivity.service;

        viewTagScan = findViewById(R.id.tag_scan);
        viewTagScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTagScan(v);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onTagScan(View v) {
//        dialog = ProgressDialog.show(this, "", "Reading tag...", true);
    	 dialog = ProgressDialog.show(this, "", getString(R.string.reading_tag), true);
        new Thread() {
            @Override
            public void run() {
                // 连续读4次，只要有3次读到0040即可判断已拆封
                IvrJackService.TagBlock tagBlock = new IvrJackService.TagBlock();
                int tamperCount = 0;
                int ret = 0;
                for (int i=0; i<4; i++) {
                    ret = service.readTag((byte)0, (byte)32, (byte)1, tagBlock);
                    if (ret != 0) break;
                    byte[] data = tagBlock.data;
                    if (data[0] == (byte)0x00 && data[1] == (byte)0x40) {
                        tamperCount++;
                    }
                    if (tamperCount == 3) break;
                }
                final int tamperCount2 = tamperCount;
                final int ret2 = ret;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ret2 != 0) {
//                            showToast("Read tag failed: " + ret2);
                        	showToast(getString(R.string.read_tag_failed) + ret2);
                        } else {
                            if (tamperCount2 == 3) {
                                showTamper();
                            } else {
                                showNoTamper();
                            }
                        }
                        dialog.dismiss();
                    }
                });
//                final IvrJackService.TagBlock tagBlock = new IvrJackService.TagBlock();
//                final int ret = service.readTag((byte)0, (byte)32, (byte)1, tagBlock);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (ret != 0) {
//                            showToast("Read tag failed: " + ret);
//                        } else {
//                            byte[] data = tagBlock.data;
//                            if (data.length == 2 && data[0] == (byte)0x00 && data[1] == (byte)0x40) {
//                                showTamper();
//                            } else {
//                                showNoTamper();
//                            }
//                        }
//                        dialog.dismiss();
//                    }
//                });
            }
        }.start();
    }

    public void showNoTamper() {
        viewTagScan.setOnClickListener(null);
        viewTagScan.setBackgroundResource(R.drawable.xm05);
    }

    public void showTamper() {
        viewTagScan.setOnClickListener(null);
        viewTagScan.setBackgroundResource(R.drawable.xm06);
    }

}
