package com.example.mechatronicse.projetocapstoneparte2.model;

import com.example.mechatronicse.projetocapstoneparte2.data.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import java.io.Serializable;

public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String caminhoFoto;
    private int seguidores = 0;
    private int seguindo = 0;
    private int postagens = 0;
    public static final String CHILD_USUARIOS = "usuarios";
    public Usuario() {
    }

    public void salvar(){
        DatabaseReference firebaseRef = FirebaseConfig.getFirebase();
        DatabaseReference usuariosRef = firebaseRef.child(CHILD_USUARIOS).child( getId() );
        usuariosRef.setValue( this );
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public int getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(int seguindo) {
        this.seguindo = seguindo;
    }

    public int getPostagens() {
        return postagens;
    }

    public void setPostagens(int postagens) {
        this.postagens = postagens;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome.toUpperCase();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
