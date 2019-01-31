package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Prodaja {

    private final StringProperty ime = new SimpleStringProperty(this, "ime", "");
    private final StringProperty prezime = new SimpleStringProperty(this, "prezime", "");
    private final StringProperty adresa = new SimpleStringProperty(this, "adresa", "");
    private final StringProperty trajanjeUgovora = new SimpleStringProperty(this, "trajanjeUgovora", "1 godina");
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final StringProperty brzina = new SimpleStringProperty(this, "brzina", "2 MBit");
    private final StringProperty protok = new SimpleStringProperty(this, "protok", "1 GB");

    private static String msg = "";

    public Prodaja() {

    }

    public Prodaja(String ime, String prezime, String adresa, String trajanjeUgovora, String brzina, String protok) {
        this.ime.set(ime);
        this.prezime.set(prezime);
        this.adresa.set(adresa);
        this.trajanjeUgovora.set(trajanjeUgovora);
        this.brzina.set(brzina);
        this.protok.set(protok);
    }

    public Prodaja(int id, String ime, String prezime, String adresa, String trajanjeUgovora, String brzina, String protok) {
        this.id.set(id);
        this.ime.set(ime);
        this.prezime.set(prezime);
        this.adresa.set(adresa);
        this.trajanjeUgovora.set(trajanjeUgovora);
        this.brzina.set(brzina);
        this.protok.set(protok);
    }

    public String getIme() {
        return ime.get();
    }

    public void setIme(String ime) {
        this.ime.set(ime);
    }

    public StringProperty imeProperty() {
        return ime;
    }

    public String getPrezime() {
        return prezime.get();
    }

    public void setPrezime(String prezime) {
        this.prezime.set(prezime);
    }

    public StringProperty prezimeProperty() {
        return prezime;
    }

    public String getAdresa() {
        return adresa.get();
    }

    public void setAdresa(String adresa) {
        this.adresa.set(adresa);
    }

    public StringProperty adresaProperty() {
        return adresa;
    }

    public String getTrajanjeUgovora() {
        return trajanjeUgovora.get();
    }

    public void setTrajanjeUgovora(String trajanjeUgovora) {
        this.trajanjeUgovora.set(trajanjeUgovora);
    }

    public StringProperty trajanjeUgovoraStringProperty() {
        return trajanjeUgovora;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getBrzina() {
        return brzina.get();
    }

    public void setBrzina(String brzina) {
        this.brzina.set(brzina);
    }

    public StringProperty brzinaProperty() {
        return brzina;
    }

    public String getProtok() {
        return protok.get();
    }

    public void setProtok(String protok) {
        this.protok.set(protok);
    }

    public StringProperty protokProperty() {
        return protok;
    }
    
    public static String getMsg() {
        return msg;
    }

    public boolean isValid() {

        boolean isValid = true;

        if (ime.get() != null && ime.get().equals("")) {
            isValid = false;
        }
        if (prezime.get().equals("")) {
            isValid = false;
        }
        if (adresa.get().equals("")) {
            isValid = false;
        }

        return isValid;
    }

    public static void novaProdaja(String ime, String prezime, String adresa, String trajanjeUgovora, String brzina, String protok) {
        Prodaja prodaja = new Prodaja(ime, prezime, adresa, trajanjeUgovora, brzina, protok);
        
        if (prodaja.isValid()) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/internet_paketi", "root", "");) {

                PreparedStatement st = conn.prepareStatement("INSERT INTO prodaja (ime,prezime,adresa,brzina,protok,trajanje_ugovora) VALUES (?,?,?,?,?,?)");
                st.setString(1, prodaja.getIme());
                st.setString(2, prodaja.getPrezime());
                st.setString(3, prodaja.getAdresa());
                st.setString(4, prodaja.getBrzina());
                st.setString(5, prodaja.getProtok());
                st.setString(6, prodaja.getTrajanjeUgovora());
                st.execute();
                
                ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID() AS id FROM prodaja");
                rs.next();
                prodaja.setId(Integer.valueOf(rs.getString("id")));
                
                msg = "Nova prodaja je uspešno zavedena u bazu.";
                
            } catch (SQLException ex) {
                msg = ex.getMessage();
            }
        } else {
            msg = "Popunite kompletnu formu!";
        }
    }
    
    public static ObservableList<Prodaja> sveProdaje() {
        Prodaja prodaja;
        
        ObservableList<Prodaja> prodaje = FXCollections.observableArrayList();
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/internet_paketi", "root", "");){
            
            Statement st = conn.createStatement();
            st.executeQuery("SELECT * FROM prodaja");
            ResultSet rs = st.getResultSet();
            
            while (rs.next()) {
                
                prodaja =  new Prodaja(rs.getInt("id"), rs.getString("ime"), rs.getString("prezime"), rs.getString("adresa"), rs.getString("trajanje_ugovora"), rs.getString("brzina"), rs.getString("protok"));
                prodaje.add(prodaja);
                
            }
            
        } catch (Exception e) {
            msg = e.getMessage();
        }
        
        return prodaje;
    }
    
    public static void ukloniProdaju(String id) {
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/internet_paketi", "root", "");) {

            PreparedStatement st = conn.prepareStatement("DELETE FROM prodaja WHERE id = ?");
            st.setString(1, id);
            st.execute();
            
            msg = "Prodaja broj " + id + " je uklonjena iz baze.";
            
        } catch (Exception ex) {
            msg = ex.getMessage();
        }
        
    }

    
}
