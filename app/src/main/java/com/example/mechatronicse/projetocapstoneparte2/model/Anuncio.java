package com.example.mechatronicse.projetocapstoneparte2.model;

import com.example.mechatronicse.projetocapstoneparte2.data.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;
import java.io.Serializable;
import java.util.List;

public class Anuncio implements Serializable {
    private String idAnuncio;
    private String estado;
    private String categoria;
    private String titulo;
    private String valor;
    private String telefone;
    private String descricao;
    private String email;
    private List<String> fotos;
    public static final String CHILD_MEUS_ANUNCIOS = "meus_anuncios";
    public static final String CHILD_ANUNCIOS = "anuncios";

    public Anuncio() {
        DatabaseReference anuncioRef = FirebaseConfig.getFirebase().child(CHILD_MEUS_ANUNCIOS);
        setIdAnuncio( anuncioRef.push().getKey() );
    }

    public void salvar(){
        String idUsuario = FirebaseConfig.getIdUsuario();
        DatabaseReference anuncioRef = FirebaseConfig.getFirebase()
                .child(CHILD_MEUS_ANUNCIOS);

        anuncioRef.child(idUsuario)
                .child(getIdAnuncio())
                .setValue(this);

        saveAnuncioPublico();

    }

    private void saveAnuncioPublico(){
        DatabaseReference anuncioRef = FirebaseConfig.getFirebase().child(CHILD_ANUNCIOS);

        anuncioRef.child( getEstado() )
                .child( getCategoria() )
                .child( getIdAnuncio() )
                .setValue(this);

    }

    public void remove(){

        String idUsuario = FirebaseConfig.getIdUsuario();
        DatabaseReference anuncioRef = FirebaseConfig.getFirebase()
                .child(CHILD_MEUS_ANUNCIOS)
                .child( idUsuario )
                .child( getIdAnuncio() );

        anuncioRef.removeValue();
        removesAnuncioPublico();

    }

    public void removesAnuncioPublico(){

        DatabaseReference anuncioRef = FirebaseConfig.getFirebase()
                .child(CHILD_ANUNCIOS)
                .child( getEstado() )
                .child( getCategoria() )
                .child( getIdAnuncio() );

        anuncioRef.removeValue();

    }

    public String getIdAnuncio() {
        return idAnuncio;
    }

    public void setIdAnuncio(String idAnuncio) {
        this.idAnuncio = idAnuncio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
