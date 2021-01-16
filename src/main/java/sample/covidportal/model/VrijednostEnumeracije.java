package main.java.sample.covidportal.model;

import main.java.sample.covidportal.enumeracija.VrijednostSimptoma;

/**
 * Služi za definiranje sučelja Zarazno
 *
 * @author Mislav Srečec
 * @version 1.0
 */

public interface VrijednostEnumeracije {

    /**
     * Služi za za dohvat vrijednosti enumeracije
     *
     * @param vrijednostSimptoma vrijednost stringa enumeracije
     */

    static VrijednostSimptoma vrijednostZarazno(String vrijednostSimptoma) {
        VrijednostSimptoma vrijednost;

        if(vrijednostSimptoma.equalsIgnoreCase(VrijednostSimptoma.INTENZIVNO.getVrijednost()))
            vrijednost = VrijednostSimptoma.INTENZIVNO;
        else if(vrijednostSimptoma.equalsIgnoreCase(VrijednostSimptoma.JAKA.getVrijednost()))
            vrijednost = VrijednostSimptoma.JAKA;
        else if(vrijednostSimptoma.equalsIgnoreCase(VrijednostSimptoma.PRODUKTIVNI.getVrijednost()))
            vrijednost = VrijednostSimptoma.PRODUKTIVNI;
        else
            vrijednost = VrijednostSimptoma.VISOKA;
        return vrijednost;
    }
}
