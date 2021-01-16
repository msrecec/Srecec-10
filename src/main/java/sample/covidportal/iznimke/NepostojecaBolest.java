package main.java.sample.covidportal.iznimke;

/**
 * Služi za definiranje iznimke koja se baca u slučaju nepostojanja bolesti
 *
 * @author Mislav Srečec
 * @version 1.0
 */

public class NepostojecaBolest extends Exception {

    /**
     * Instancira objekt klase <code>NepostojecaBolest</code> i šalje vrijednost stringa konstruktoru nadklase
     * <code>super("Ne postoji zupanija za uneseni ID!");</code>
     */

    public NepostojecaBolest() {
        super("Ne postoji bolest za uneseni ID!");
    }

    /**
     * Instancira objekt klase <code>NepostojecaBolest</code> i šalje vrijednost stringa<code>String message</code> konstruktoru nadklase
     * <code>super(message);</code>
     */

    public NepostojecaBolest(String message) {
        super(message);
    }

    /**
     * Instancira objekt klase <code>NepostojecaBolest</code> i šalje vrijednost stringa <code>String message</code> konstruktoru nadklase
     * <code>super(message);</code> i ujedno vrijednost drugog uzroka greške <code>Throwable cause</code>
     *
     * @param message
     * @param cause
     */

    public NepostojecaBolest(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instancira objekt klase <code>NepostojecaBolest</code> i šalje drugi uzrok greške <code>Throwable cause</code> konstruktoru nadklase
     * <code>super(cause);</code>
     *
     * @param cause
     */

    public NepostojecaBolest(Throwable cause) {
        super(cause);
    }
}
