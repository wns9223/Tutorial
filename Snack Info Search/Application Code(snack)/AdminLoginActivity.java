package edu.sfsu.cs.orange.ocr.admin_login_pkg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.CharacterPickerDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ip.cureturn.pkg.IPReturn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import data_resume_pkg.DataBaseAdminResume;
import edu.sfsu.cs.orange.ocr.R;

public class AdminLoginActivity extends Activity {
    private TextView forgetpwView;
    private Button admin_check;
    private EditText login_id,login_passwd;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        forgetpwView = (TextView) findViewById(R.id.login_forget);
        login_id = (EditText)findViewById(R.id.log_id);
        login_passwd = (EditText) findViewById(R.id.log_passwd);

        admin_check = (Button) findViewById(R.id.admin_check);


        forgetpwView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });


        login_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(login_id.getText().length()>0 & login_passwd.getText().length() > 0 )
                    admin_check.setEnabled(true);
                else
                    admin_check.setEnabled(false);
            }
        });

        login_passwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(login_id.getText().length()>0 & login_passwd.getText().length() > 0 )
                    admin_check.setEnabled(true);
                else
                    admin_check.setEnabled(false);
            }
        });



        admin_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String idValue = login_id.getText().toString();
                String PWvalue = login_passwd.getText().toString();

                    /*Cursor cursor;
                    cursor = db.rawQuery("select name, password from trip where name='" + idValue + "' and password ='" + PWvalue + "';", null);


                    while (cursor.moveToNext()) {

                        if (idValue.compareTo(cursor.getString(0)) == 0 && PWvalue.compareTo(cursor.getString(1)) == 0) {
                            Toast.makeText(MainActivity.this, "logining", Toast.LENGTH_SHORT).show();
                            StrictMode.enableDefaults();
*/
                IPReturn ipreturn  = new IPReturn();
                try {
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
                    Socket socket = new Socket(ipreturn.getIP(), 1);
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                    String data = "select max(code),name,password from admin where name='" + idValue + "' and password ='" + PWvalue + "';";
                    out.writeUTF(data);

                    String code = in.readUTF();
                    String name = in.readUTF();
                    String password = in.readUTF();

                    if(Integer.parseInt(code)==0) {
                        Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        in.close();
                        out.close();
                        socket.close();
                    }
                    if(Integer.parseInt(code)==1){                                                          
                        Toast.makeText(getApplicationContext(), in.readUTF(), Toast.LENGTH_SHORT).show();
                        in.close();
                        out.close();
                        socket.close();
                        startActivity(new Intent(AdminLoginActivity.this, DataBaseAdminResume.class));

                    }





                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Please contact e-mail")
                .setMessage("wns9223@naver.com");        // message setting

        AlertDialog dialog = builder.create();    // alarm view create
        dialog.show();    // alarm only view

        return super.onCreateDialog(id);
    }


}
