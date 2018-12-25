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
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.Alpha;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.NUMBER_OFFSET;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.Y;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.ZERO;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.alphaNumber;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.buttonAcesso)
    FloatingActionButton botaoAcessar;
    @BindView(R.id.editCadastroEmail)
    EditText campoEmail;
    @BindView(R.id.editCadastroSenha)
    EditText campoSenha;
    @BindView(R.id.switchAcesso)
    Switch tipoAcesso;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    Usuario usuario;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        currentUser();
        tipoAcesso();
        acessarBotao();
        animateViewsIn();
    }


    private void acessarBotao(){
        progressBar.setVisibility( View.GONE );
        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoEmail = campoEmail.getText().toString();
                String textosenha = campoSenha.getText().toString();

                if( !textoEmail.isEmpty() ){
                    if( !textosenha.isEmpty() ){
                        usuario = new Usuario();
                        usuario.setEmail( textoEmail );
                        usuario.setSenha( textosenha );
                        validarLogin( usuario );

                    }else{
                        Toast.makeText(LoginActivity.this,
                                getString(R.string.digite_senha),
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.digite_email),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void tipoAcesso(){
        tipoAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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

    public void currentUser(){
        mAuth = FirebaseConfig.getFirebaseAutenticacao();
        if( mAuth.getCurrentUser() != null ){
            telaFeed();
            finish();
        }
    }

    public void validarLogin( Usuario usuario ){

        progressBar.setVisibility( View.VISIBLE );
        mAuth = FirebaseConfig.getFirebaseAutenticacao();

        mAuth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful() ){
                    progressBar.setVisibility( View.GONE );
                    telaFeed();


                }else {

                    Toast.makeText(LoginActivity.this,
                            getString(R.string.erro_ao_login),
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility( View.GONE );
                }

            }
        });

    }

    private void telaFeed(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }


}
