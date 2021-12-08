package com.ankurupadhyay.finaljffadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ankurupadhyay.finaljffadmin.AddOrder.PendingSaveOrderActivity;

public class AddInstructionsActivity extends AppCompatActivity {

    Button submit;

    EditText note;

    ImageView imgBack;

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instructions);


        Intent intent = getIntent();

        submit = findViewById(R.id.submit);
        note = findViewById(R.id.note);
        imgBack = findViewById(R.id.imgBack);
        title = findViewById(R.id.title);



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (intent.hasExtra("type"))

        {
            note.setHint("Enter Table No");
            title.setText("Table No.");
            if (intent.hasExtra("table_no"))
                note.setText(intent.getStringExtra("table_no"));

        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intent.hasExtra("type"))
                {



                    if (Common.isEmpty(note))
                    {
                        note.setError("Enter table no");
                    }
                    else
                    {
                        Intent intent = new Intent(AddInstructionsActivity.this, PendingSaveOrderActivity.class);
                        intent.putExtra("data",note.getText().toString());
                        setResult(104,intent);
                        finish();

                    }
                }
                else
                {
                    if (Common.isEmpty(note))
                    {
                        note.setError("Enter some instructions");
                    }
                    else
                    {
                        Intent intent = new Intent(AddInstructionsActivity.this, PendingSaveOrderActivity.class);
                        intent.putExtra("data",note.getText().toString());
                        setResult(102,intent);
                        finish();

                    }
                }


            }
        });
    }

}