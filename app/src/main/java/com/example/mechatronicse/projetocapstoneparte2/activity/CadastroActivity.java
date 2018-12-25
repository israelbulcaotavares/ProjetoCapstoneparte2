package com.example.mechatronicse.projetocapstoneparte2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;
import com.example.mechatronicse.projetocapstoneparte2.R;
import com.example.mechatronicse.projetocapstoneparte2.constants.Constants;
import com.example.mechatronicse.projetocapstoneparte2.data.FirebaseConfig;
import com.example.mechatronicse.projetocapstoneparte2.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.Alpha;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.NUMBER_OFFSET;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.Y;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.ZERO;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.alphaNumber;

public class CadastroActivity extends AppCompatActivity {
    @BindView(R.id.buttonAcesso)
    FloatingActionButton botaoAcessar;
    @BindView(R.id.editCadastroNome)
    EditText campoNome;
    @BindView(R.id.editCadastroEmail)
    EditText campoEmail ;
    @BindView(R.id.editCadastroSenha)
    EditText campoSenha ;
    @BindView(R.id.switchAcesso)
    Switch tipoAcesso;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tipoAcesso();
        acessarBotao();
        animateViewsIn();
    }

    private void tipoAcesso(){
        tipoAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void animateViewsIn() {
        ViewGroup root = (ViewGroup) findViewById(R.id.root);
        int count = root.getChildCount();
        float offset = getResources().getDimensionPixelSize(R.dimen.offset_y);
        Interpolator interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in);


        for (int i = ZERO; i < count; i++) {
            View view = root.getChildAt(i);

            view.setTranslationY(offset);
            view.setAlpha(Alpha);
            view.animate()
                    .translationY(Y)
                    .alpha(alphaNumber)
                    .setInterpolator(interpolator)
                    .setDuration(Constants.DURATION)
                    .start();
            offset *= NUMBER_OFFSET;
        }
    }

    private void acessarBotao(){
        progressBar.setVisibility( View.GONE );
        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoNome  = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textosenha = campoSenha.getText().toString();

                if( !textoNome.isEmpty() ){
                    if( !textoEmail.isEmpty() ){
                        if( !textosenha.isEmpty() ){

                            usuario = new Usuario();
                            usuario.setNome( textoNome );
                            usuario.setEmail( textoEmail );
                            usuario.setSenha( textosenha );
                            cadastrarUsuario( usuario );

                        }else{
                            Toast.makeText(CadastroActivity.this
                                    ,getString(R.string.digite_senha),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this,
                                getString(R.string.digite_email),
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroActivity.this,
                            getString(R.string.digite_nome),
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    public void cadastrarUsuario(final Usuario usuario){
        mAuth = FirebaseConfig.getFirebaseAutenticacao();
        progressBar.setVisibility(View.VISIBLE);
        mAuth = FirebaseConfig.getFirebaseAutenticacao();
        mAuth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if( task.isSuccessful() ){

                            try {
                                progressBar.setVisibility(View.GONE);
                                String idUsuario = task.getResult().getUser().getUid();
                                usuario.setId( idUsuario );
                                usuario.salvar();

                                Toast.makeText(CadastroActivity.this,
                                        getString(R.string.sucesso),
                                        Toast.LENGTH_SHORT).show();

                                startActivity( new Intent(getApplicationContext(), LoginActivity.class));


                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }else {

                            progressBar.setVisibility( View.GONE );

                            String erroExcecao = "";
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                erroExcecao = getString(R.string.senha_segura);
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                erroExcecao =  getString(R.string.email_valido);
                            }catch (FirebaseAuthUserCollisionException e){
                                erroExcecao = getString(R.string.conta_cadastrada);
                            } catch (Exception e) {
                                erroExcecao = getString(R.string.ao_cadastrar)  + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(CadastroActivity.this,
                                    getString(R.string.erro) + erroExcecao ,
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                }
        );

    }


}
