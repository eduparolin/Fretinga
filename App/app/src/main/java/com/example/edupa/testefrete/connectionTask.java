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
    private String[] titlesMotorista = {"nome", "cpf", "rg", "email", "celular", "telefone", "usuario", "senha"};
    private String[] titlesDistribuidor = {"razao_social", "nome_fantasia", "cnpj", "inscricao_estadual", "pais", "estado", "cidade", "endereco", "bairro", "cep", "complemento", "email", "senha","telefone", "celular", "site", "nome_contato"};
    private String[] titlesLogin = {"usuario", "senha"};
    private String[] titlesAutos = {"usuario", "tipo", "carroceria", "carga", "placa", "nome", "rast", "marca", "modelo", "ano", "cor"};
    private String[] titlesAutoList = {"user"};
    private int task = 0;
    @Override
    protected String doInBackground(String... params) {
        try {
            Log.d("Cliente", "conectanto");
            Socket socket = new Socket(params[0], Integer.parseInt(params[1]));
            OutputStream out = socket.getOutputStream();
            if(params[2].equals("motorista"))task = 0;
            if(params[2].equals("distribuidor"))task = 1;
            if(params[2].equals("loginMotorista"))task = 2;
            if(params[2].equals("loginDistribuidor"))task = 3;
            else if(params[2].equals("auto"))task = 4;
            else if(params[2].equals("auto_list"))task = 5;
            JSONObject object = new JSONObject();
            try{
                int size = params.length;
                for (int i = 3; i < size; i++) {
                    if(task == 0) {
                        object.put(titlesMotorista[i - 3], params[i]);
                        Log.d(TAG, titlesMotorista[i - 3]+" : "+params[i]);

                    }else if(task == 1) {
                        object.put(titlesDistribuidor[i - 3], params[i]);
                        Log.d(TAG, titlesDistribuidor[i - 3] + " : " + params[i]);
                    }else if(task == 2){
                        object.put(titlesLogin[i - 3], params[i]);
                        Log.d(TAG, titlesLogin[i - 3]+" : "+ params[i]);
                    }else if(task == 3){
                        object.put(titlesLogin[i - 3], params[i]);
                        Log.d(TAG, titlesLogin[i - 3]+" : "+ params[i]);
                    }
                    else if(task == 4){
                        object.put(titlesAutoList[i - 3], params[i]);
                        Log.d(TAG, titlesAutoList[i - 3]+" : "+ params[i]);
                    }
                }
            }catch(JSONException jE){
                Log.e(TAG, jE.toString());
            }
            JSONObject map = new JSONObject();
            if(task == 0)map.put("updateMotoristas", object);
            else if(task == 1)map.put("updateDistribuidor", object);
            else if(task == 2){
                map.put("loginInfoMotorista", object);
                Log.d("Connection", "logando como motorista");
            }
            else if(task == 3){
                map.put("loginInfoDistribuidor", object);
                Log.d("Connection", "logando como distribuidor");
            }
            else if(task == 4)map.put("updateAutos", object);
            else if(task == 4)map.put("autoList", object);

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
