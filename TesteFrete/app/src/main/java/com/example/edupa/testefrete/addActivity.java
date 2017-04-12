package com.example.edupa.testefrete;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by edupa on 22/02/2017.
 */

public class addActivity extends AppCompatActivity {

    EditText carga, peso;
    Button ok;

    //String[] statuses = {"Status: "};

    @Override
    public void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.add_layout);
        carga = (EditText)findViewById(R.id.carga);
        peso = (EditText)findViewById(R.id.peso);
        ok = (Button)findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Main2Activity.data.newFreteCard("Status: pedido solicidado",
                        nome.getText().toString(), peso.getText().toString(),
                        carga.getText().toString(), 1);*/
                finish();
            }
        });
    }

}
