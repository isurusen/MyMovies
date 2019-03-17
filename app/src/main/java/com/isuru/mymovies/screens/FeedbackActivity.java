package com.isuru.mymovies.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.isuru.mymovies.R;

/**
 * Created by Isuru Senanayake on 17/03/2019.
 *
 * -- This class send the feedback through the default email client of the Android OS
 */

public class FeedbackActivity extends AppCompatActivity {

    EditText txtTo, txtSubject, txtBody;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        txtTo = findViewById(R.id.txt_to);
        txtSubject = findViewById(R.id.txt_subject);
        txtBody = findViewById(R.id.txt_message);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String to = txtTo.getText().toString();
                String subject = txtSubject.getText().toString();
                String body = txtBody.getText().toString();

                if(TextUtils.isEmpty(body)){
                    Toast.makeText(getApplicationContext(), "Please enter the message", Toast.LENGTH_SHORT).show();
                }else if(!validateEmail(to)){
                    Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    txtTo.requestFocus();
                }
                else
                {
                    if (TextUtils.isEmpty(to) || (!TextUtils.isEmpty(to) && validateEmail(to))) {
                        sendEmail(to, subject, body);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    /*
     * Sending the email.
     */
    private void sendEmail(String to, String subject, String body){

            String[] recipients = to.split(",");

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, body);

            // Sending the parameters to the selected email client
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

    /*
     * Validating the input parameters.
     */
    public boolean validateEmail(CharSequence target) {
        if (target == null || target.equals("")) {
            return true;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}
