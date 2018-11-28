package alina.com.rms.activities;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import alina.com.rms.R;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.util.CallType;
import alina.com.rms.util.Util;

public class RecoveryPasswodActivity extends AppCompatActivity implements AsyncCompleteListner{
    ImageButton imageButton;
    EditText editText;
    TextView textView3;
    Dialog openDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_passwod);
        imageButton =(ImageButton)findViewById(R.id.imageButton);
        editText=(EditText)findViewById(R.id.editText);
        textView3=(TextView)findViewById(R.id.textView3);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(Util.isValidEmail(editText.getText()))
                {
                    setRecoveryPassword();
                }
                else {
                    Util.showShortToast(RecoveryPasswodActivity.this,"Enter valid email");
                }
            }
        });

    }

    private void setRecoveryPassword()
    {
        setProgressDialoug(true);
        String email=editText.getText().toString();

        AsyncController asyncController=new AsyncController(RecoveryPasswodActivity.this,this, CallType.RECOVERY_PASSWORD,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.recoveryPasswordRequest(email);
    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
    if(response!=null)
    {
        Log.e("response",response);
        try {
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.has("response_code"))
            {
                if(Integer.parseInt(jsonObject.getString("response_code"))==0)
                {
                    Util.showLongToast(RecoveryPasswodActivity.this,jsonObject.getString("message"));

                }
                else {
/*                    LoginDB.setLoginResponseAsJSON(RecoveryPasswodActivity.this,jsonObject.getString("response"));
                    finish();
                    Intent intent=new Intent(RecoveryPasswodActivity.this,MainActivitySuperAdmin.class);
                    startActivity(intent);*/
                    JSONArray jsonArray=new JSONArray(jsonObject.getString("response"));
                    JSONObject jsonObject1=jsonArray.getJSONObject(0);

                   new AlertDialog.Builder(RecoveryPasswodActivity.this)
                            .setTitle("Your password is")
                            .setMessage(jsonObject1.getString("password"))
                            .setIcon(R.drawable.logo)
                            .setPositiveButton("OK", null)
                            .show();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    }

    public void setProgressDialoug(boolean flag)
    {

        if(openDialog==null)
        {

            openDialog= new Dialog(RecoveryPasswodActivity.this);
            openDialog.setContentView(R.layout.progress_dialoug1);
           // openDialog.setTitle("Custom Dialog Box");
            openDialog.setCancelable(false);
            // disable scroll on touch


        }
        if(flag)
        {
            openDialog.show();
        }
        else
        {
            openDialog.dismiss();
        }

    }
}
