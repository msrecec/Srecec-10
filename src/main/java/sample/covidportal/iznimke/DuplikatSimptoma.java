package main.java.sample.covidportal.iznimke;

/**
 * Služi za definiranje iznimke koja se baca u slučaju duplikata Simptoma
 *
 * @author Mislav Srečec
 * @version 1.0
 */

public class DuplikatSimptoma extends RuntimeException {

    /**
     * Instancira objekt klase <code>DuplikatSimptoma</code> i šalje vrijednost stringa konstruktoru nadklase
     * <code>super("Uneseni simptom je duplikat!");</code>
     */

    public DuplikatSimptoma() {
        super("Uneseni simptom je duplikat!");
    }

    /**
     * Instancira objekt klase <code>DuplikatSimptoma</code> i šalje vrijednost stringa <code>String message</code> konstruktoru nadklase
     * <code>super(message);</code>
     *
     * @param message
     */

    public DuplikatSimptoma(String message) {
        super(message);
    }

    /**
     * Instancira objekt klase <code>DuplikatSimptoma</code> i šalje vrijednost stringa <code>String message</code> konstruktoru nadklase
     * <code>super(message);</code> i ujedno vrijednost drugog uzroka greške <code>Throwable cause</code>
     *
     * @param message
     * @param cause
     */

    public DuplikatSimptoma(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instancira objekt klase <code>DuplikatSimptoma</code> i šalje drugi uzrok greške <code>Throwable cause</code> konstruktoru nadklase
     * <code>super(cause);</code>
     *
     * @param cause
     */

    public DuplikatSimptoma(Throwable cause) {
        super(cause);
    }

}
