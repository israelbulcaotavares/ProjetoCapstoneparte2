package com.example.mechatronicse.projetocapstoneparte2.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.mechatronicse.projetocapstoneparte2.R;
import com.example.mechatronicse.projetocapstoneparte2.data.FirebaseConfig;
import com.example.mechatronicse.projetocapstoneparte2.helper.Permissions;
import com.example.mechatronicse.projetocapstoneparte2.model.Anuncio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santalu.maskedittext.MaskEditText;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.request_DOIS;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.request_TRES;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.request_UM;

public class CadastrarPetFragment extends Fragment implements View.OnClickListener  {

    @BindView(R.id.button)
    FloatingActionButton cadastrarAnuncio;
    @BindView(R.id.editTitulo)
    EditText campoTitulo;
    @BindView(R.id.editDescricao)
    EditText campoDescricao;
    @BindView(R.id.editEmail)
    EditText campoEmail;
    @BindView(R.id.editValor)
    CurrencyEditText campoValor;
    @BindView(R.id.editTelefone)
    MaskEditText campoTelefone;
    @BindView(R.id.imageCadastro1)
    ImageView imagem1;
    @BindView(R.id.imageCadastro2)
    ImageView imagem2;
    @BindView(R.id.imageCadastro3)
    ImageView imagem3;
    @BindView(R.id.spinnerEstado)
    Spinner campoEstado;
    @BindView(R.id.spinnerCategoria)
    Spinner campoCategoria;
    @BindView(R.id.progressBar3)
    ProgressBar progressBar3;
    @BindView(R.id.textProgressBar)
    TextView textProgressBar;
    @BindView(R.id.cardViewProgress)
    CardView cardViewProgress;

    private Anuncio anuncio;
    private StorageReference storage;
    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private List<String> listaFotosRecuperadas = new ArrayList<>();
    private List<String> listaURLFotos = new ArrayList<>();

    public CadastrarPetFragment() {
     }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_cadastrar_pet, container, false);
        ButterKnife.bind(this,view);


        configStoragesFirebasee();
        Permissions.validPermissions(permissoes, getActivity(), 1);
        inicializarComponentes( view);
        carregarDadosSpinner();

        return view;
    }

    private void configStoragesFirebasee(){
        storage = FirebaseConfig.getFirebaseStorage();
        progressBar3.setVisibility(View.GONE);
        textProgressBar.setVisibility(View.GONE);
        cardViewProgress.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == Activity.RESULT_OK){
            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            if( requestCode == request_UM ){
                imagem1.setImageURI( imagemSelecionada );
            }else if( requestCode == request_DOIS ){
                imagem2.setImageURI( imagemSelecionada );
            }else if( requestCode == request_TRES ){
                imagem3.setImageURI( imagemSelecionada );
            }

            listaFotosRecuperadas.add( caminhoImagem );

        }
    }

    public void saveAnuncio(){
        progressBar3.setVisibility(View.VISIBLE);
        textProgressBar.setVisibility(View.VISIBLE);
        cardViewProgress.setVisibility(View.VISIBLE);

        for (int i=0; i < listaFotosRecuperadas.size(); i++){
            String urlImagem = listaFotosRecuperadas.get(i);
            int tamanhoLista = listaFotosRecuperadas.size();
            salvarFotoStorage(urlImagem, tamanhoLista, i );
        }

    }

    private void salvarFotoStorage(String urlString, final int totalFotos, int contador){

        StorageReference imagemAnuncio = storage.child(getString(R.string.imagens_child))
                .child(getString(R.string.anuncios_child))
                .child( anuncio.getIdAnuncio() )
                .child(getString(R.string.imagens_child) +contador);

        UploadTask uploadTask = imagemAnuncio.putFile( Uri.parse(urlString) );
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri firebaseUrl = taskSnapshot.getDownloadUrl();
                String urlConvertida = firebaseUrl.toString();

                listaURLFotos.add( urlConvertida );

                if( totalFotos == listaURLFotos.size() ){
                    anuncio.setFotos( listaURLFotos );
                    anuncio.salvar();

                    progressBar3.setVisibility(View.GONE);
                    textProgressBar.setVisibility(View.GONE);
                    getActivity().finish();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                exibirMensagemErro(getString(R.string.falha_upload));
                Log.i(getString(R.string.info), getString(R.string.falha_upload) + e.getMessage());
            }
        });

    }

    private Anuncio configurarAnuncio(){
        String estado = campoEstado.getSelectedItem().toString();
        String categoria = campoCategoria.getSelectedItem().toString();
        String titulo = campoTitulo.getText().toString();
        String valor = campoValor.getText().toString();
        String telefone = campoTelefone.getText().toString();
        String descricao = campoDescricao.getText().toString();
        String email = campoEmail.getText().toString();

        Anuncio anuncio = new Anuncio();
        anuncio.setEstado( estado );
        anuncio.setCategoria(categoria);
        anuncio.setTitulo(titulo);
        anuncio.setValor(valor);
        anuncio.setTelefone( telefone );
        anuncio.setDescricao(descricao);
        anuncio.setEmail(email);

        return anuncio;

    }

    private void validarCamposDoAnuncio(){

        anuncio = configurarAnuncio();
        String valor = String.valueOf(campoValor.getRawValue());

        if( listaFotosRecuperadas.size() != 0  ){
            if( !anuncio.getEstado().isEmpty() ){
                if( !anuncio.getCategoria().isEmpty() ){
                    if( !anuncio.getTitulo().isEmpty() ){
                        if( !valor.isEmpty() && !valor.equals(getString(R.string.equals_a_zero)) ){
                            if( !anuncio.getTelefone().isEmpty()  ){
                                if( !anuncio.getDescricao().isEmpty() ){
                                    saveAnuncio();
                                }else {
                                    exibirMensagemErro(getString(R.string.campo_descricao));
                                }
                            }else {
                                exibirMensagemErro(getString(R.string.campo_telefone) );
                            }
                        }else {
                            exibirMensagemErro(getString(R.string.campo_valor) );
                        }
                    }else {
                        exibirMensagemErro(getString(R.string.campo_titulo) );
                    }
                }else {
                    exibirMensagemErro(getString(R.string.campo_categoria) );
                }
            }else {
                exibirMensagemErro(getString(R.string.campo_estado) );
            }
        }else {
            exibirMensagemErro(getString(R.string.campo_3_fotos));
        }

    }
    private void exibirMensagemErro(String texto){
        Toast.makeText(getActivity(), texto, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
         switch ( v.getId() ){
            case R.id.imageCadastro1 :
                escolherImagem(request_UM);
                break;
            case R.id.imageCadastro2 :
                escolherImagem(request_DOIS);
                break;
            case R.id.imageCadastro3 :
                escolherImagem(request_TRES);
                break;
        }

    }

    public void escolherImagem(int requestCode){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, requestCode);
    }


    private void carregarDadosSpinner(){

        String[] estados = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item,
                estados
        );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        campoEstado.setAdapter( adapter );

        String[] categorias = getResources().getStringArray(R.array.racas);
        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item,
                categorias
        );
        adapterCategoria.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        campoCategoria.setAdapter( adapterCategoria );

    }

    private void inicializarComponentes(View view){
        cadastrarAnuncio.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               validarCamposDoAnuncio();

           }
       });
        imagem1.setOnClickListener(this);
        imagem2.setOnClickListener(this);
        imagem3.setOnClickListener(this);

         Locale locale = new Locale(getString(R.string.portugues), getString(R.string.BRASIL_BR));
        campoValor.setLocale( locale );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for( int permissaoResultado : grantResults ){
            if( permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }

    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.negado_permissao));
        builder.setMessage(getString(R.string.necessario_permissao));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.confirmacao), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


}
