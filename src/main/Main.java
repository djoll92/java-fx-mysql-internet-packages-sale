package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Prodaja;
import model.ProdajaStringConverter;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        TextField ime = new TextField();
        Label labelIme = new Label("_Ime:");
        labelIme.setMnemonicParsing(true);
        labelIme.setLabelFor(ime);
        labelIme.setPrefWidth(50);

        TextField prezime = new TextField();
        Label labelPrezime = new Label("_Prezime:");
        labelPrezime.setMnemonicParsing(true);
        labelPrezime.setLabelFor(prezime);

        TextField adresa = new TextField();
        Label labelAdresa = new Label("_Adresa:");
        labelAdresa.setMnemonicParsing(true);
        labelAdresa.setLabelFor(adresa);

        ObservableList<String> listaBrzina = FXCollections.<String>observableArrayList("2 Mbit", "5 Mbit", "10 Mbit", "20 Mbit", "50 Mbit", "100 Mbit");
        ChoiceBox brzine = new ChoiceBox(listaBrzina);
        brzine.getSelectionModel().selectFirst();
        Label labelBrzina = new Label("_Brzina:");
        labelBrzina.setMnemonicParsing(true);

        ObservableList<String> listaProtoka = FXCollections.<String>observableArrayList("1 GB", "5 GB", "10 GB", "100 GB", "Flat");
        ChoiceBox protoci = new ChoiceBox(listaProtoka);
        protoci.getSelectionModel().selectFirst();
        Label labelProtok = new Label("P_rotok:");
        labelProtok.setMnemonicParsing(true);

        ObservableList<String> listaTrajanjaUgovora = FXCollections.<String>observableArrayList("1 godina", "2 godine");
        ChoiceBox trajanjaUgovora = new ChoiceBox(listaTrajanjaUgovora);
        trajanjaUgovora.getSelectionModel().selectFirst();
        Label labelTrajanjeUgovora = new Label("Trajanje _ugovora:");
        labelTrajanjeUgovora.setMnemonicParsing(true);

        GridPane gridPane = new GridPane();
        gridPane.setPrefWidth(800);
        gridPane.setPrefHeight(600);

        gridPane.addColumn(0, labelIme, labelPrezime, labelAdresa, labelBrzina, labelProtok, labelTrajanjeUgovora);
        gridPane.addColumn(1, ime, prezime, adresa, brzine, protoci, trajanjaUgovora);

        Button dodaj = new Button("Dodaj prodaju");
        dodaj.setId("dodajBtn");
        gridPane.add(dodaj, 1, 6);

        Text msg = new Text();
        msg.setFill(Color.DARKGREEN);
        msg.setText("");
        gridPane.add(msg, 0, 7, 2, 1);
        
        ListView prodaje = new ListView(Prodaja.sveProdaje());
        if ( Prodaja.sveProdaje().isEmpty()) {
            msg.setText("U bazi nema prodaja.");
        }
        prodaje.setPrefHeight(550);

        ProdajaStringConverter converter = new ProdajaStringConverter();
        Callback<ListView<Prodaja>, ListCell<Prodaja>> cellFactory = TextFieldListCell.forListView(converter);
        prodaje.setCellFactory(cellFactory);

        gridPane.add(prodaje, 2, 0, 1, 8);

        Button brisanje = new Button("Ukloni prodaju");

        gridPane.add(brisanje, 2, 8);

        dodaj.setOnAction((ActionEvent event) -> {

            Prodaja.novaProdaja(ime.getText(), prezime.getText(), adresa.getText(), trajanjaUgovora.getSelectionModel().getSelectedItem().toString(), brzine.getSelectionModel().getSelectedItem().toString(), protoci.getSelectionModel().getSelectedItem().toString());

            msg.setText(Prodaja.getMsg());
            prodaje.getItems().clear();
            prodaje.getItems().addAll(Prodaja.sveProdaje());
        });

        brisanje.setOnAction((ActionEvent event) -> {

            Prodaja prodaja = (Prodaja) prodaje.getSelectionModel().getSelectedItem();

            if (prodaja != null) {
                Prodaja.ukloniProdaju(String.valueOf(prodaja.getId()));
                msg.setText(Prodaja.getMsg());
                prodaje.getItems().clear();
                prodaje.getItems().addAll(Prodaja.sveProdaje());
            } else {
                msg.setText("Selektujte prodaju koju biste da uklonite.");
            }

        });

        ColumnConstraints cc1 = new ColumnConstraints();
        ColumnConstraints cc2 = new ColumnConstraints();
        ColumnConstraints cc3 = new ColumnConstraints();
        
        cc3.setHgrow(Priority.ALWAYS);
        
        gridPane.getColumnConstraints().addAll(cc1, cc2, cc3);
        
        gridPane.setVgap(10.0);
        gridPane.setHgap(10.0);
        gridPane.setPadding(new Insets(10.0));
        
        Scene scene = new Scene(gridPane);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Prodaja internet-paketa");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
