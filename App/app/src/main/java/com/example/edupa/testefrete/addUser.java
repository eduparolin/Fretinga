package com.example.edupa.testefrete;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by edupa on 23/02/2017.
 */

public class addUser extends AppCompatActivity implements addUserListener{

    Button motorista, distribuidor;//Botões de seleção do tipo de conta.

    private int id;
    private String motorista_usuario, motorista_cpf, motorista_email, motorista_celular;
    private String motorista_telefone, motorista_nome, motorista_senha, motorista_rg;
    private byte[] foto;
    private String distribuidor_razao_social, distribuidor_nome_fantasia, distribuidor_cnpj;
    private String distribuidor_inscricao_estadual;
    private String distribuidor_pais,distribuidor_estado, distribuidor_cidade, distribuidor_endereco, distribuidor_bairro;
    private String distribuidor_cep, distribuidor_complemento;
    private String distribuidor_email, distribuidor_telefone, distribuidor_celular;
    private String distribuidor_site, distribuidor_nome_contato, distribuidor_senha;
    String serverIP, serverPort;
    String tipo;

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.add_user);
        serverIP = "192.168.25.18";
        serverPort = "1234";
        //Configurando botões por layout:
        motorista = (Button)findViewById(R.id.button_motorista);
        distribuidor = (Button)findViewById(R.id.button_distribuidor);
        //Definindo suas ações:
        motorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = "motorista";
                motoristaFragment fragment = new motoristaFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.container, fragment);
                fragmentTransaction.addToBackStack("addNext");
                fragmentTransaction.commit();
            }
        });
        distribuidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = "distribuidor";
                distribuidorFragment fragment = new distribuidorFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.container, fragment);
                fragmentTransaction.addToBackStack("addNext");
                fragmentTransaction.commit();
                //abrirFragmento(new distribuidorFragment());//Abre fragmento de distribuidor.
            }
        });
    }

    public void abrirFragmento(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.addToBackStack("addNext");
        fragmentTransaction.commit();
    }

    public void addToDB(){

       /* Log.d("Adicionando ao servidor", user);
        new startTask().execute("192.168.25.18", "1234", "add", "0", user, email,
                password, tipoUser, CPF, RG, dataNasc, nomeCompleto);*/
        //Futuramente essa função deve enviar para um servidor as informações.
    }

    @Override
    public void motoristaInterface(String nome_completo, String cpf, String rg, String email,
                                   String celular, String telefone) {
        this.motorista_nome = nome_completo;
        this.motorista_cpf = cpf;
        this.motorista_rg = rg;
        this.motorista_email = email;
        this.motorista_celular = celular;
        this.motorista_telefone = telefone;
        getFragmentManager().popBackStack();
        abrirFragmento(new motorista2Fragment());
    }

    @Override
    public void motorista2Interface(String usuario, String senha, byte[] foto) {
        this.motorista_usuario = usuario;
        this.motorista_senha = senha;
        this.foto = foto;
        getFragmentManager().popBackStack();
        new startTask().execute(serverIP, serverPort, tipo, motorista_nome, motorista_cpf,
                motorista_rg,
                motorista_email, motorista_celular, motorista_telefone, motorista_usuario,
                motorista_senha);
        //addToDB();
        finish();
    }

    @Override
    public void distribuidorInterface(String razao_social, String nome_fantasia, String cnpj, String inscricao_estadual) {
       // getFragmentManager().popBackStack();
        this.distribuidor_razao_social = razao_social;
        this.distribuidor_nome_fantasia = nome_fantasia;
        this.distribuidor_cnpj = cnpj;
        this.distribuidor_inscricao_estadual = inscricao_estadual;
        abrirFragmento(new distribuidor2Fragment());
    }

    @Override
    public void distribuidor2Interface(String pais, String estado, String cidade, String endereco, String bairro, String cep, String complemento) {
        this.distribuidor_pais = pais;
        this.distribuidor_estado = estado;
        this.distribuidor_cidade = cidade;
        this.distribuidor_endereco = endereco;
        this.distribuidor_bairro = bairro;
        this.distribuidor_cep = cep;
        this.distribuidor_complemento = complemento;
        abrirFragmento(new distribuidor3Fragment());
    }

    @Override
    public void distribuidor3Interface(String email, String senha, String telefone, String celular, String site, String nome_contato) {
        this.distribuidor_email = email;
        this.distribuidor_telefone = telefone;
        this.distribuidor_celular = celular;
        this.distribuidor_site = site;
        this.distribuidor_nome_contato = nome_contato;
        this.distribuidor_senha = senha;
        new startTask().execute(serverIP, serverPort, tipo, distribuidor_razao_social,
                distribuidor_nome_fantasia, distribuidor_cnpj, distribuidor_inscricao_estadual,
                distribuidor_pais, distribuidor_estado, distribuidor_cidade,
                distribuidor_endereco, distribuidor_bairro, distribuidor_cep,
                distribuidor_complemento, distribuidor_email, distribuidor_senha,
                distribuidor_telefone,
                distribuidor_celular, distribuidor_site, distribuidor_nome_contato);
        finish();
    }

    public class startTask extends connectionTask{
        @Override
        protected void onProgressUpdate(String... progress) {
            if(progress[0] != null) {
                /*if (progress[0].equals("True")) Main2Activity.data.newUser(user, email, password,
                        tipoUser, CPF, RG, dataNasc, nomeCompleto, foto);*/
                Toast.makeText(addUser.this, "Conta criada !", Toast.LENGTH_SHORT).show();
                Log.d("addUser.class", "OK");
            }
        }

    }

    public static class motoristaFragment extends Fragment{

        addUserListener listener;
        Button proximo;
        EditText nome_completo, cpf, rg, email, r_email, celular, telefone;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle bundle){
            View actualView = inflater.inflate(R.layout.fragment_add_motorista, vGroup, false);

            //Configurando botão por layout:
            proximo = (Button)actualView.findViewById(R.id.button_proximo);
            //Configurando edits por layout:
            nome_completo = (EditText)actualView.findViewById(R.id.edit_user);
            cpf = (EditText)actualView.findViewById(R.id.edit_cpf);
            rg = (EditText)actualView.findViewById(R.id.edit_rg);
            email = (EditText)actualView.findViewById(R.id.edit_email);
            r_email = (EditText)actualView.findViewById(R.id.edit_remail);
            celular = (EditText)actualView.findViewById(R.id.edit_celular);
            telefone = (EditText)actualView.findViewById(R.id.edit_telefone);

            //Configurando clique do botão:
            proximo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isEmailFilled = false;
                    boolean isEmailsEqual = false;
                    boolean cpfIsFilled = false;
                    boolean nomeIsFilled = false;
                    boolean celularIsFilled = false;
                    boolean next = true;
                    if(!email.getText().toString().equals(""))isEmailFilled = true;
                    if(!cpf.getText().toString().equals(""))cpfIsFilled = true;
                    if(!nome_completo.getText().toString().equals(""))nomeIsFilled = true;
                    if(!celular.getText().toString().equals(""))celularIsFilled = true;
                    if(email.getText().toString().equals(r_email.getText().toString()))isEmailsEqual = true;
                    if(!nomeIsFilled && next){
                        nome_completo.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do nome..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if(!cpfIsFilled && next){
                        cpf.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do cpf..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if(!isEmailFilled && next){
                        email.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do email..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if(!isEmailsEqual && next){
                        r_email.requestFocus();
                        Toast.makeText(getActivity(), "Os emails não são iguais..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if(!celularIsFilled && next){
                        celular.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do celular..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if(next){
                        listener.motoristaInterface(nome_completo.getText().toString(),
                                cpf.getText().toString(), rg.getText().toString(),
                                email.getText().toString(),
                                celular.getText().toString(), telefone.getText().toString());
                    }
                }
            });
            return actualView;
        }
        @Override
        public void onAttach(Context context){
            super.onAttach(context);
            try {
                listener = (addUserListener) context;
            } catch (ClassCastException castException) {
                /** The activity does not implement the listener. */
            }
        }
    }
    public static class motorista2Fragment extends Fragment{

        addUserListener listener;
        Button pronto;
        EditText usuario, senha, r_senha;
        ImageView foto;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle bundle){
            View actualView = inflater.inflate(R.layout.fragment_add_motorista_2, vGroup, false);

            //Configurando botão por layout:
            pronto = (Button)actualView.findViewById(R.id.button_pronto);
            //Configurando edits por layout:
            usuario = (EditText)actualView.findViewById(R.id.edit_usuario);
            senha = (EditText)actualView.findViewById(R.id.edit_senha);
            r_senha = (EditText)actualView.findViewById(R.id.edit_rsenha);
            //Configurando foro por layout:
            foto = (ImageView)actualView.findViewById(R.id.image_foto);

            //Configurando clique do botão:
            pronto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isUsuarioFilled = false;
                    boolean isSenhaFilled = false;
                    boolean isSenhasEqual = false;
                    boolean next = true;
                    if(!usuario.getText().toString().equals(""))isUsuarioFilled = true;
                    if(!senha.getText().toString().equals(""))isSenhaFilled = true;
                    if(senha.getText().toString().equals(r_senha.getText().toString()))isSenhasEqual = true;
                    if(!isUsuarioFilled && next){
                        next = false;
                        usuario.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do usuario..", Toast.LENGTH_SHORT).show();
                    }
                    if(!isSenhaFilled && next) {
                        next = false;
                        senha.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo da senha..", Toast.LENGTH_SHORT).show();
                    }
                    if(!isSenhasEqual && next) {
                        next = false;
                        r_senha.requestFocus();
                        Toast.makeText(getActivity(), "As senhas nõ são iguais..", Toast.LENGTH_SHORT).show();
                    }
                    if(next){
                        listener.motorista2Interface(usuario.getText().toString(),
                                senha.getText().toString(), new byte[16]);
                    }
                }
            });
            return actualView;
        }
        @Override
        public void onAttach(Context context){
            super.onAttach(context);
            try {
                listener = (addUserListener) context;
            } catch (ClassCastException castException) {
                /** The activity does not implement the listener. */
            }
        }
    }
    public static class distribuidorFragment extends Fragment {

        addUserListener listener;
        Button proximo;
        EditText razao_social, nome_fantasia, cnpj, inscricao_estadual;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle bundle) {
            View actualView = inflater.inflate(R.layout.fragment_add_distribuidor, vGroup, false);

            //Configurando botão por layout:
            proximo = (Button) actualView.findViewById(R.id.button_proximo);
            //Configurando edits por layout:
            razao_social = (EditText) actualView.findViewById(R.id.edit_razao_social);
            nome_fantasia = (EditText) actualView.findViewById(R.id.edit_nome_fantasia);
            cnpj = (EditText) actualView.findViewById(R.id.edit_cnpj);
            inscricao_estadual = (EditText) actualView.findViewById(R.id.edit_inscricao_estadual);

            //Configurando clique do botão:
            proximo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isRazaoFilled = false;
                    boolean isNomeFilled = false;
                    boolean isCNPJFilled = false;
                    boolean isInscricaoFilled = false;
                    boolean next = true;
                    if (!razao_social.getText().toString().equals("")) isRazaoFilled = true;
                    if (!nome_fantasia.getText().toString().equals("")) isNomeFilled = true;
                    if (!cnpj.getText().toString().equals("")) isCNPJFilled = true;
                    if (!inscricao_estadual.getText().toString().equals(""))
                        isInscricaoFilled = true;
                    if (!isRazaoFilled && next) {
                        razao_social.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo da Razão Social..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if (!isNomeFilled && next) {
                        nome_fantasia.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do Nome Fantasia..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if (!isCNPJFilled && next) {
                        cnpj.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do CNPJ..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if (!isInscricaoFilled && next) {
                        inscricao_estadual.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo da Inscrição Estadual..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if (next) {
                        listener.distribuidorInterface(razao_social.getText().toString(),
                                nome_fantasia.getText().toString(), cnpj.getText().toString(),
                                inscricao_estadual.getText().toString());
                    }
                }
            });
            return actualView;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            try {
                listener = (addUserListener) context;
            } catch (ClassCastException castException) {
                /** The activity does not implement the listener. */
            }
        }
    }
    public static class distribuidor2Fragment extends Fragment{

        addUserListener listener;
        Button proximo;
        EditText pais, estado, cidade, endereco, bairro, cep, complemento;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle bundle){
            View actualView = inflater.inflate(R.layout.fragment_add_distribuidor_2, vGroup, false);

            //Configurando botão por layout:
            proximo = (Button)actualView.findViewById(R.id.button_proximo);
            //Configurando edits por layout:
            pais = (EditText)actualView.findViewById(R.id.edit_pais);
            estado = (EditText)actualView.findViewById(R.id.edit_estado);
            cidade = (EditText)actualView.findViewById(R.id.edit_cidade);
            endereco = (EditText)actualView.findViewById(R.id.edit_endereco);
            bairro = (EditText)actualView.findViewById(R.id.edit_bairro);
            cep = (EditText)actualView.findViewById(R.id.edit_cep);
            complemento = (EditText)actualView.findViewById(R.id.edit_complemento);

            //Configurando clique do botão:
            proximo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isPaisFilled = false;
                    boolean isEstadoFilled = false;
                    boolean isCidadeFilled = false;
                    boolean isEnderecoFilled = false;
                    boolean next = true;
                    if(!pais.getText().toString().equals(""))isPaisFilled = true;
                    if(!estado.getText().toString().equals(""))isEstadoFilled = true;
                    if(!cidade.getText().toString().equals(""))isCidadeFilled = true;
                    if(!endereco.getText().toString().equals(""))isEnderecoFilled = true;
                    if(!isPaisFilled && next){
                        pais.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do País..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if(!isEstadoFilled && next){
                        estado.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do Estado..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if(!isCidadeFilled && next){
                        cidade.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo da Cidade..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if(!isEnderecoFilled && next){
                        endereco.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do Endereço..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if(next){
                       listener.distribuidor2Interface(pais.getText().toString(),
                               estado.getText().toString(), cidade.getText().toString(),
                               endereco.getText().toString(),
                                bairro.getText().toString(), cep.getText().toString(),
                               complemento.getText().toString());
                    }
                }
            });
            return actualView;
        }
        @Override
        public void onAttach(Context context){
            super.onAttach(context);
            try {
                listener = (addUserListener) context;
            } catch (ClassCastException castException) {
                /** The activity does not implement the listener. */
            }
        }
    }
    public static class distribuidor3Fragment extends Fragment{

        addUserListener listener;
        Button pronto;
        EditText email, senha, telefone, celular, site, nome_contato;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle bundle){
            View actualView = inflater.inflate(R.layout.fragment_add_distribuidor_3, vGroup, false);

            //Configurando botão por layout:
            pronto = (Button)actualView.findViewById(R.id.button_pronto);
            //Configurando edits por layout:
            email = (EditText)actualView.findViewById(R.id.edit_email);
            senha = (EditText)actualView.findViewById(R.id.edit_senha);
            telefone = (EditText)actualView.findViewById(R.id.edit_telefone);
            celular = (EditText)actualView.findViewById(R.id.edit_celular);
            site = (EditText)actualView.findViewById(R.id.edit_site);
            nome_contato = (EditText)actualView.findViewById(R.id.edit_nome_contato);

            //Configurando clique do botão:
            pronto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isEmailFilled = false;
                    boolean isTelefoneFilled = false;
                    boolean isCelularFilled = false;
                    boolean isSiteFilled = false;
                    boolean isNomeFilled = false;
                    boolean next = true;
                    if(!email.getText().toString().equals(""))isEmailFilled = true;
                    if(!telefone.getText().toString().equals(""))isTelefoneFilled = true;
                    if(!celular.getText().toString().equals(""))isCelularFilled = true;
                    if(!site.getText().toString().equals(""))isSiteFilled = true;
                    if(!nome_contato.getText().toString().equals(""))isNomeFilled = true;
                    if(!isEmailFilled && next){
                        email.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do Email..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if(!isTelefoneFilled && next){
                        telefone.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do Telefone..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if(!isCelularFilled && next){
                        celular.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do Celular..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if(!isSiteFilled && next){
                        site.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do Site..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if(!isNomeFilled && next){
                        nome_contato.requestFocus();
                        Toast.makeText(getActivity(), "Preencha o campo do Nome de Contato..", Toast.LENGTH_SHORT).show();
                        next = false;
                    }
                    if(next){
                        listener.distribuidor3Interface(email.getText().toString(),
                                senha.getText().toString(),
                                telefone.getText().toString(), celular.getText().toString(),
                                site.getText().toString(), nome_contato.getText().toString());
                    }
                }
            });
            return actualView;
        }
        @Override
        public void onAttach(Context context){
            super.onAttach(context);
            try {
                listener = (addUserListener) context;
            } catch (ClassCastException castException) {
                /** The activity does not implement the listener. */
            }
        }
    }

}
