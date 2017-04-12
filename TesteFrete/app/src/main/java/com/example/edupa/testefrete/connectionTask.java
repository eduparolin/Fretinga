package com.example.edupa.testefrete;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by edupa on 09/03/2017.
 */

public class connectionTask extends AsyncTask<String, String, String>{
    private String TAG = "connectionTask";
    private String[] titlesAdd = {"id", "user", "email", "pass", "tipo", "cpf", "rg", "dataN", "nome"};
    private String[] titlesLogin = {"user", "pass"};
    private String[] titlesAutos = {"user", "tipo", "carroceria", "carga", "placa", "nome", "rast", "marca", "modelo", "ano", "cor"};
    private int task = 0;
    @Override
    protected String doInBackground(String... params) {
        try {
            Log.d("Cliente", "conectanto");
            Socket socket = new Socket(params[0], Integer.parseInt(params[1]));
            OutputStream out = socket.getOutputStream();
            if(params[2].equals("login"))task = 1;
            else if(params[2].equals("auto"))task = 2;
            JSONObject object = new JSONObject();
            try{
                int size = params.length;
                for (int i = 3; i < size; i++) {
                    if(task == 0) {
                        //Log.e(TAG, "task 0");
                        object.put(titlesAdd[i - 3], params[i]);
                        Log.d(TAG, titlesAdd[i - 3]+" : "+params[i]);
                    }else if(task == 1){
                        object.put(titlesLogin[i - 3], params[i]);
                        Log.d(TAG, titlesLogin[i - 3]+" : "+ params[i]);
                    }else if(task == 2){
                        object.put(titlesAutos[i - 3], params[i]);
                        Log.d(TAG, titlesAutos[i - 3]+" : "+ params[i]);
                    }
                }
            }catch(JSONException jE){
                Log.e(TAG, jE.toString());
            }
            JSONObject map = new JSONObject();
            if(task == 0)map.put("updateUsers", object);
            else if(task == 1)map.put("loginInfo", object);
            else if(task == 2)map.put("updateAutos", object);
            String content = map.toString();
            byte[] send = content.getBytes();
            out.write(send);
            out.flush();
            Log.d("Cliente", "enviado");
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputText = "";
            inputText = input.readLine();

            //input.close();

            out.close();
            input.close();
            Log.d(TAG, inputText);
            publishProgress(inputText);
            socket.close();
        }catch(Exception e){
            Log.e(TAG, e.toString());
        }
        return null;
    }
}
