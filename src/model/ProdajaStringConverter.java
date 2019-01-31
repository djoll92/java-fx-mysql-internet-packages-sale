package model;

import javafx.util.StringConverter;

public class ProdajaStringConverter extends StringConverter<Prodaja> {

    @Override
    public String toString(Prodaja object) {

        StringBuilder sb = new StringBuilder();
        sb.append(object.getId());
        sb.append(" - ");
        sb.append(object.getIme());
        sb.append(" ");
        sb.append(object.getPrezime());
        sb.append(", ");
        sb.append(object.getAdresa());
        sb.append(" - brzina ");
        sb.append(object.getBrzina());
        sb.append(" - protok ");
        sb.append(object.getProtok());
        sb.append(" - trajanje ugovora ");
        sb.append(object.getTrajanjeUgovora());

        return object == null ? null :  sb.toString();
    }

    @Override
    public Prodaja fromString(String string) {
        Prodaja p = null;
        return p;
    }

}
