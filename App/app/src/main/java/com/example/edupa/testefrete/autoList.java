package com.example.edupa.testefrete;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class autoList extends AppCompatActivity {
    ListView list;
    Button adicionar;
    String user;
    List<Automoveis> automoveis;
    List<String> autosStrings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_list);
        if(getIntent().getExtras() != null){
            user = getIntent().getStringExtra("user");
        }
        list = (ListView)findViewById(R.id.auto_list);
        adicionar = (Button)findViewById(R.id.adicionar);
        automoveis = new ArrayList<>();
        autosStrings = new ArrayList<>();
        new startTask().execute("192.168.25.18", "1234", "auto_list", user);
        adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAutomovelActivity = new Intent(autoList.this, addAuto.class);
                addAutomovelActivity.putExtra("user", user);
                startActivity(addAutomovelActivity);
            }
        });
    }
    public class startTask extends connectionTask{
        @Override
        protected void onProgressUpdate(String... progress) {
            if(progress[0] != null) {
                try{
                    //object = new JSONObject(progress[0]);
                    //Log.d("JSON", object.toString());
                    JSONArray array = new JSONArray(progress[0]);
                    int size = array.length();
                    for(int i = 0; i<size; i++){
                        JSONObject object = array.getJSONObject(i);
                        Automoveis autos = new Automoveis();
                        autos.setTipo(object.getString("tipoV"));
                        autos.setCarroceria(object.getString("tipoC"));
                        autos.setCargaMaxima(Integer.parseInt(object.getString("carga")));
                        autos.setPlaca(object.getString("placa"));
                        autos.setNome(object.getString("nome"));
                        autos.setMarca(object.getString("marca"));
                        autos.setModelo(object.getString("modelo"));
                        autos.setRastreador(Integer.parseInt(object.getString("rast")));
                        autos.setAno(Integer.parseInt(object.getString("ano")));
                        autos.setCor(object.getString("cor"));
                        automoveis.add(autos);
                    }

                }catch (Exception e){
                    Log.e("JSON", e.toString());
                }
                for(int i = 0; i<automoveis.size(); i++){
                    autosStrings.add(automoveis.get(i).getModelo());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(autoList.this, android.R.layout.simple_list_item_1, autosStrings);
                list.setAdapter(adapter);

        }

            //if(progress[0].equals("True"))loginInfo.setText("Logado !");
            //else loginInfo.setText("Usuário ou senha incorretos ):");
        }

    }
}
