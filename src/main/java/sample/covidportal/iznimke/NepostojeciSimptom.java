package main.java.sample.covidportal.iznimke;

/**
 * Služi za definiranje iznimke koja se baca u slučaju nepostojecih osoba
 *
 * @author Mislav Srečec
 * @version 1.0
 */

public class NepostojeciSimptom extends Exception {

    /**
     * Instancira objekt klase <code>NepostojeciSimptom</code> i šalje vrijednost stringa konstruktoru nadklase
     * <code>super("Simptom ne postoji!");</code>
     */

    public NepostojeciSimptom() {
        super("Simptom ne postoji!");
    }

    /**
     * Instancira objekt klase <code>NepostojeciSimptom</code> i šalje vrijednost stringa <code>String message</code> konstruktoru nadklase
     * <code>super(message);</code>
     *
     * @param message
     */

    public NepostojeciSimptom(String message) {
        super(message);
    }

    /**
     * Instancira objekt klase <code>NepostojeciSimptom</code> i šalje vrijednost stringa <code>String message</code> konstruktoru nadklase
     * <code>super(message);</code> i ujedno vrijednost drugog uzroka greške <code>Throwable cause</code>
     *
     * @param message
     * @param cause
     */

    public NepostojeciSimptom(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instancira objekt klase <code>NepostojeciSimptom</code> i šalje drugi uzrok greške <code>Throwable cause</code> konstruktoru nadklase
     * <code>super(cause);</code>
     *
     * @param cause
     */

    public NepostojeciSimptom(Throwable cause) {
        super(cause);
    }

}
