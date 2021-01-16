package main.java.sample.covidportal.iznimke;

/**
 * Služi za definiranje iznimke koja se baca u slučaju zupanija istog naziva
 *
 * @author Mislav Srečec
 * @version 1.0
 */

public class ZupanijaIstogNaziva extends RuntimeException {

    /**
     * Instancira objekt klase <code>ZupanijaIstogNaziva</code> i šalje vrijednost stringa konstruktoru nadklase
     * <code>super("Unesena zupanija ima isti naziv kao prethodno unesena zupanija!");</code>
     */

    public ZupanijaIstogNaziva() {
        super("Unesena zupanija ima isti naziv kao prethodno unesena zupanija!");
    }

    /**
     * Instancira objekt klase <code>ZupanijaIstogNaziva</code> i šalje vrijednost stringa<code>String message</code> konstruktoru nadklase
     * <code>super(message);</code>
     */

    public ZupanijaIstogNaziva(String message) {
        super(message);
    }

    /**
     * Instancira objekt klase <code>ZupanijaIstogNaziva</code> i šalje vrijednost stringa <code>String message</code> konstruktoru nadklase
     * <code>super(message);</code> i ujedno vrijednost drugog uzroka greške <code>Throwable cause</code>
     *
     * @param message
     * @param cause
     */

    public ZupanijaIstogNaziva(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instancira objekt klase <code>ZupanijaIstogNaziva</code> i šalje drugi uzrok greške <code>Throwable cause</code> konstruktoru nadklase
     * <code>super(cause);</code>
     *
     * @param cause
     */

    public ZupanijaIstogNaziva(Throwable cause) {
        super(cause);
    }
}
