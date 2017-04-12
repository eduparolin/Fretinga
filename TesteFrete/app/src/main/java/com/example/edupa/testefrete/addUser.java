package com.example.edupa.testefrete;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * Created by edupa on 23/02/2017.
 */

public class addUser extends AppCompatActivity implements closeListener{

    Button next;
    private int id;
    private String user;
    private String email;
    private String password;
    private String tipoUser;
    private String CPF;
    private String RG;
    private String dataNasc;
    private String nomeCompleto;
    private byte[] foto;

    Switch tipo;

    EditText eUser, eEmail, rEmail, pass, rpass;

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.add_user);
        next = (Button)findViewById(R.id.next);
        eUser = (EditText)findViewById(R.id.user);
        eEmail = (EditText)findViewById(R.id.email);
        rEmail = (EditText)findViewById(R.id.remail);
        pass = (EditText)findViewById(R.id.pass);
        rpass = (EditText)findViewById(R.id.rpass);
        tipo = (Switch)findViewById(R.id.switch1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = eUser.getText().toString();
                String email = eEmail.getText().toString();
                String emailComp = rEmail.getText().toString();
                String sPass = pass.getText().toString();
                String passComp = rpass.getText().toString();
                boolean tipoConta = false;
                String tipoS = "cnpj";
                if(emailComp.equals(eEmail.getText().toString())){
                    if(passComp.equals(pass.getText().toString())) {
                        if(user.isEmpty())eUser.requestFocus();
                        else if(email.isEmpty())eEmail.requestFocus();
                        else if(sPass.isEmpty())pass.requestFocus();
                        else {
                            if(tipo.isChecked()){
                                tipoS = "cpf";
                                tipoConta = true;
                            }
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            nextFragment fragment = new nextFragment();
                            Bundle b = new Bundle();
                            b.putBoolean("tipo", tipoConta);
                            fragment.setArguments(b);
                            fragmentTransaction.add(R.id.container, fragment);
                            fragmentTransaction.addToBackStack("userExtras");
                            fragmentTransaction.commit();
                            firstScreen(user, email, sPass, tipoS);

                        }
                    }else {
                        Toast.makeText(addUser.this, "Senha nao confere", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(addUser.this, "Email nao confere", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void firstScreen(String user, String email, String password, String tipo) {
        this.user = user;
        this.email = email;
        this.password = password;
        this.tipoUser = tipo;
    }

    @Override
    public void secondScreen(String cpf, String rg, String dataNasc) {
        this.CPF = cpf;
        this.RG = rg;
        this.dataNasc = dataNasc;
    }

    @Override
    public void lastScreen(String nomeComp, byte[] foto) {
        this.nomeCompleto = nomeComp;
        this.foto = foto;
        addToDB();
    }

    public void addToDB(){

        Log.d("Adicionando ao servidor", user);
        new startTask().execute("192.168.25.18", "1234", "add", "0", user, email,
                password, tipoUser, CPF, RG, dataNasc, nomeCompleto);
        //Futuramente essa função deve enviar para um servidor as informações.
    }
    public class startTask extends connectionTask{
        @Override
        protected void onProgressUpdate(String... progress) {
            if(progress[0] != null) {
                if (progress[0].equals("True")) Main2Activity.data.newUser(user, email, password,
                        tipoUser, CPF, RG, dataNasc, nomeCompleto, foto);
                Log.d("addUser.class", "Adicionando ao banco de dados local...");
            }

            //if(progress[0].equals("True"))loginInfo.setText("Logado !");
            //else loginInfo.setText("Usuário ou senha incorretos ):");
        }

    }

    public static class nextFragment extends Fragment{

        Button next;
        closeListener listener;
        EditText cpf, rg;
        CalendarView calendarView;
        String date = "";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle bundle){
            View actualView = inflater.inflate(R.layout.add_user_extras, vGroup, false);
            Bundle b = getArguments();
            boolean isCpf = false;
            if(b!=null){
                isCpf = b.getBoolean("tipo");
            }

            cpf = (EditText)actualView.findViewById(R.id.cpf);
            if(isCpf)cpf.setHint("CPF");
            else cpf.setHint("CNPJ");
            rg = (EditText)actualView.findViewById(R.id.rg);

            calendarView = (CalendarView)actualView.findViewById(R.id.calendarView);
            int day = 1;
            int month = 0;
            int year = 1999;

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            calendarView.setDate(calendar.getTimeInMillis());
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    date = String.valueOf(dayOfMonth)+String.valueOf(month+1)+String.valueOf(year);
                }
            });
            next = (Button)actualView.findViewById(R.id.next2);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sCpf = cpf.getText().toString();
                    String sRg = rg.getText().toString();
                    String sData = date;
                    if(sCpf.isEmpty())cpf.requestFocus();
                    else if(sRg.isEmpty())rg.requestFocus();
                    else {
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        finalFragment fragment = new finalFragment();
                        fragmentTransaction.add(R.id.container, fragment);
                        fragmentTransaction.addToBackStack("final");
                        fragmentTransaction.commit();
                        listener.secondScreen(cpf.getText().toString(), rg.getText().toString(),
                                sData);
                    }
                }
            });
            return actualView;
        }
        @Override
        public void onAttach(Context context){
            super.onAttach(context);
            try {
                listener = (closeListener) context;
            } catch (ClassCastException castException) {
                /** The activity does not implement the listener. */
            }
        }
    }public static class finalFragment extends Fragment{

        Button next;
        private closeListener listener;
        EditText nomeComp;
        ImageView foto;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle bundle){
            View actualView = inflater.inflate(R.layout.add_user_final, vGroup, false);
            next = (Button)actualView.findViewById(R.id.final1);
            nomeComp = (EditText)actualView.findViewById(R.id.nome_comp);
            foto = (ImageView)actualView.findViewById(R.id.foto);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                                        String nome = nomeComp.getText().toString();
                    if(nome.isEmpty())nomeComp.requestFocus();
                    else {
                        //Bitmap bmp = foto.getDrawingCache();
                        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        //bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = new byte[10];
                        listener.lastScreen(nomeComp.getText().toString(), byteArray);
                        //getFragmentManager().popBackStack();
                        listener.close();
                    }
                }
            });
            return actualView;
        }
        @Override
        public void onAttach(Context context){
            super.onAttach(context);
            try {
                listener = (closeListener) context;
            } catch (ClassCastException castException) {
                /** The activity does not implement the listener. */
            }
        }
    }

}
