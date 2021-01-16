package main.java.sample.covidportal.iznimke;

/**
 * Služi za definiranje iznimke koja se baca u slučaju praznog polja
 *
 * @author Mislav Srečec
 * @version 1.0
 */

public class PraznoPolje extends Exception {

    /**
     * Instancira objekt klase <code>PraznoPolje</code> i šalje vrijednost stringa konstruktoru nadklase
     * <code>super("Polje je prazno!");</code>
     */

    public PraznoPolje() {
        super("Polje je prazno!");
    }

    /**
     * Instancira objekt klase <code>PraznoPolje</code> i šalje vrijednost stringa <code>String message</code> konstruktoru nadklase
     * <code>super(message);</code>
     *
     * @param message
     */

    public PraznoPolje(String message) {
        super(message);
    }

    /**
     * Instancira objekt klase <code>PraznoPolje</code> i šalje vrijednost stringa <code>String message</code> konstruktoru nadklase
     * <code>super(message);</code> i ujedno vrijednost drugog uzroka greške <code>Throwable cause</code>
     *
     * @param message
     * @param cause
     */

    public PraznoPolje(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instancira objekt klase <code>PraznoPolje</code> i šalje drugi uzrok greške <code>Throwable cause</code> konstruktoru nadklase
     * <code>super(cause);</code>
     *
     * @param cause
     */

    public PraznoPolje(Throwable cause) {
        super(cause);
    }

}
