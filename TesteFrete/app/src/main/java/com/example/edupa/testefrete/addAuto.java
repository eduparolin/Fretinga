package com.example.edupa.testefrete;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

public class addAuto extends AppCompatActivity implements addAutoListener{

    //Primeira tela
    String user = "";
    int tipo_veiculo;
    int tipo_carroceria;
    int carga_maxima;
    String placa;
    String nome_proprietario;
    boolean rastreador;

    //Segunda tela(fragment)
    String marca;
    String modelo;
    int ano;
    String cor;

    Spinner t_v, t_c;
    EditText cm, pl, np;
    Switch rast;

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transporte);

        if(getIntent().getExtras() != null){
            user = getIntent().getStringExtra("user");
        }

        t_v = (Spinner)findViewById(R.id.spinner);
        t_c = (Spinner)findViewById(R.id.spinner2);

        cm = (EditText)findViewById(R.id.carga);
        pl = (EditText)findViewById(R.id.placa);
        np = (EditText)findViewById(R.id.nome);

        rast = (Switch)findViewById(R.id.rast);

        next = (Button)findViewById(R.id.button);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_veiculo = t_v.getSelectedItemPosition();
                tipo_carroceria = t_c.getSelectedItemPosition();
                carga_maxima = Integer.parseInt(cm.getText().toString());
                placa = pl.getText().toString();
                nome_proprietario = np.getText().toString();
                rastreador = rast.isChecked();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                addAuto2 fragment = new addAuto2();
                fragmentTransaction.add(R.id.container, fragment);
                fragmentTransaction.addToBackStack("auto2");
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public void setInfo(String marca, String modelo, int ano, String cor) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.cor = cor;
        new startTask().execute("192.168.25.18", "1234", "add", "2", user, String.valueOf(tipo_veiculo),
                String.valueOf(tipo_carroceria), String.valueOf(carga_maxima), placa, nome_proprietario, String.valueOf(rastreador), marca,
                modelo, String.valueOf(ano), cor);

        //db.addVeiculo
    }

    public class startTask extends connectionTask{
        @Override
        protected void onProgressUpdate(String... progress) {
            if(progress[0] != null) {
                int vRast = 0;
                if(rastreador)vRast = 1;
                if (progress[0].equals("True")) Main2Activity.data.newAutomovel(user,
                        String.valueOf(tipo_veiculo), String.valueOf(tipo_carroceria),
                        carga_maxima, placa, nome_proprietario, vRast, marca, modelo, ano, cor);
                Log.d("addAuto.class", "Adicionando ao banco de dados local...");
            }

            //if(progress[0].equals("True"))loginInfo.setText("Logado !");
            //else loginInfo.setText("Usuário ou senha incorretos ):");
        }

    }

    public static class addAuto2 extends Fragment {

        EditText mc, mo, an, co;
        addAutoListener listener;
        Button conc;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle bundle) {
            final View actualView = inflater.inflate(R.layout.add_transporte_2, vGroup, false);
            mc = (EditText)actualView.findViewById(R.id.marca);
            mo = (EditText)actualView.findViewById(R.id.modelo);
            an = (EditText)actualView.findViewById(R.id.ano);
            co = (EditText)actualView.findViewById(R.id.cor);

            conc = (Button)actualView.findViewById(R.id.button2);
            conc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.setInfo(mc.getText().toString(), mo.getText().toString(),
                            Integer.parseInt(an.getText().toString()), co.getText().toString());
                    getFragmentManager().popBackStack();
                }
            });

            return actualView;
        }
        @Override
        public void onAttach(Context context){
            super.onAttach(context);
            listener = (addAutoListener)context;
        }
    }
}
