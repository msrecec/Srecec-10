package main.java.sample.covidportal.enumeracija;

public enum VrijednostSimptoma {
    PRODUKTIVNI("Produktivni"),
    INTENZIVNO("Intenzivno"),
    VISOKA("Visoka"),
    JAKA("Jaka");

    private String vrijednost;

    private VrijednostSimptoma(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }
}
