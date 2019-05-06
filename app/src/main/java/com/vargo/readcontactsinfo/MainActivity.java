package com.vargo.readcontactsinfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.UserHandle;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvDemo1;
    private Button mBtnDemo1;
    private Button mBtnDemo2;
    private Button mBtnDemo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvDemo1 = (TextView)findViewById(R.id.tvDemo1);
        mBtnDemo1 = (Button)findViewById(R.id.btnDemo1);
        mBtnDemo2 = (Button)findViewById(R.id.btnDemo2);
        mBtnDemo3 = (Button)findViewById(R.id.btnDemo3);
        mBtnDemo1.setOnClickListener(this);
        mBtnDemo2.setOnClickListener(this);
        mBtnDemo3.setOnClickListener(this);

        if(PermissionUtil.isGrantReadContactsPermission(this)){
            mTvDemo1.setText("Permission allowed!");
        }else{

            mTvDemo1.setText("Permission requested!");
            PermissionUtil.reqestReadContactsPermission(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PermissionUtil.isGrantReadContactsPermission(this)){
            mTvDemo1.setText("Permission allowed!");
        }else{

            mTvDemo1.setText("Permission denied!");
        }
    }

    @Override
    public void onClick(View v) {
        if(v == mBtnDemo1){
            Uri uri = Uri.parse("content://com.android.contacts/contacts");
            Log.d("zzzMainActivity", "onClick uri="+uri.toString());
            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);
            StringBuilder sblist = new StringBuilder();
            while (cursor.moveToNext()) {
                int contractID = cursor.getInt(0);
                StringBuilder sb = new StringBuilder("contractID=");
                sb.append(contractID);
                uri = Uri.parse("content://com.android.contacts/contacts/" + contractID + "/data");
                Cursor cursor1 = resolver.query(uri, new String[]{"mimetype", "data1", "data2"}, null, null, null);
                while (cursor1.moveToNext()) {
                    String data1 = cursor1.getString(cursor1.getColumnIndex("data1"));
                    String mimeType = cursor1.getString(cursor1.getColumnIndex("mimetype"));
                    if ("vnd.android.cursor.item/name".equals(mimeType)) { //是姓名
                        sb.append(",name=" + data1);
                    } else if ("vnd.android.cursor.item/email_v2".equals(mimeType)) { //邮箱
                        sb.append(",email=" + data1);
                    } else if ("vnd.android.cursor.item/phone_v2".equals(mimeType)) { //手机
                        sb.append(",phone=" + data1);
                    }
                }
                cursor1.close();
                sblist.append(sb.toString()).append("\n");
            }
            cursor.close();
            mTvDemo1.setText(sblist.toString());
        }else if(v == mBtnDemo3){
            try {
                int userId = (int)UserHandle.class.getMethod("myUserId").invoke(null);
                Class<?> cls = Class.forName("android.os.SystemProperties");
                java.lang.reflect.Method m = cls.getDeclaredMethod("get",String.class);
                String username = (String)m.invoke(null, "persist.sys.user.nickname"+userId);
                Log.d("zzzMainActivity", "zzz userId="+userId+",userName="+username);

            }catch (Exception e){
                Log.e("zzzMainActivity", "zzz onClick exception "+e+","+Log.getStackTraceString(e));
            }
        }

    }
}
