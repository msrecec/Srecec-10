package main.java.sample.covidportal.baza;

import main.java.sample.covidportal.enumeracija.VrijednostSimptoma;
import main.java.sample.covidportal.iznimke.*;
import main.java.sample.covidportal.model.*;
import main.java.sample.covidportal.sort.CovidSorter;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class BazaPodataka implements VrijednostEnumeracije {
    private static final String DATABASE_CONFIGURATION_FILE = "src\\main\\resources\\database.properties";

    /**
     * Stvara novu konekciju na bazu podataka i vraca referencu na njenu instancu
     *
     * @return referencu na instancu baze podataka
     * @throws SQLException ako je greska prilikom rada s bazom
     * @throws IOException ako je greska prilikom dohvacanja konfiguracijske datoteke
     */

    public static Connection connectToDatabase() throws SQLException, IOException {
        Properties svojstva = new Properties();

        svojstva.load(new FileReader(DATABASE_CONFIGURATION_FILE));

        String urlBazePodataka = svojstva.getProperty("bazaPodatakaUrl");
        String korisnickoIme = svojstva.getProperty("korisnickoIme");
        String lozinka = svojstva.getProperty("lozinka");

        Connection veza = DriverManager.getConnection(urlBazePodataka, korisnickoIme, lozinka);

        return veza;
    }

    /**
     * Zatvara konekciju na bazu podataka
     *
     * @param veza referenca na instancu konekcije na bazu
     * @throws SQLException ako je greska prilikom rada s bazom
     */

    public static void closeConnectionToDatabase(Connection veza) throws SQLException {
        veza.close();
    }

    /**
     * Dohvaca sve simptome iz baze podataka
     *
     * @return simptomi iz baze
     * @throws SQLException ako je greska prilikom rada s bazom
     * @throws IOException ako je greska prilikom dohvacanja konfiguracijske datoteke
     */

    public static List<Simptom> dohvatiSveSimptome() throws SQLException, IOException {
        List<Simptom> simptomi = new ArrayList<>();
        Connection veza = connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SIMPTOM");

        while(rs.next()) {
            simptomi.add(unosSimptoma(rs));
        }

        closeConnectionToDatabase(veza);

        return simptomi;

    }

    /**
     * Dohvaca simptom iz baze podataka
     *
     * @param idSimptoma id simptoma koji dohvacamo
     * @return simptom koji dohvacamo
     * @throws SQLException ako je greska prilikom rada s bazom
     * @throws IOException ako je greska prilikom dohvacanja konfiguracijske datoteke
     */

    public static Simptom dohvatiSimptom(Long idSimptoma) throws SQLException, IOException, NepostojeciSimptom {
        Simptom simptom;
        Connection veza = connectToDatabase();
        PreparedStatement upit = veza.prepareStatement("SELECT * FROM SIMPTOM WHERE ID = ?");

        upit.setString(1, String.valueOf(idSimptoma));

        ResultSet rs = upit.executeQuery();

        if(rs.next()) {
            simptom = unosSimptoma(rs);
        } else {
            throw new NepostojeciSimptom();
        }

        closeConnectionToDatabase(veza);

        return simptom;
    }

    /**
     * Unosi simptom iz result set-a i vraca novi simptom
     *
     * @param rs set rezultata iz baze podataka
     * @return novi simptom
     * @throws SQLException ako je greska prilikom rada s bazom
     */

    private static Simptom unosSimptoma(ResultSet rs) throws SQLException {
        long id = rs.getLong("ID");
        String naziv = rs.getString("NAZIV");
        String vrijednostSimptoma = rs.getString("VRIJEDNOST");

        Simptom simptom = new Simptom(id, naziv, VrijednostEnumeracije.vrijednostZarazno(vrijednostSimptoma));

        System.out.println(simptom.getVrijednost().getVrijednost());

        return simptom;
    }

    /**
     * Sprema novi simptom u bazu podataka
     *
     * @param simptom novi simptom koji unosimo u bazu
     * @throws SQLException ako je greska prilikom rada s bazom
     * @throws IOException ako je greska prilikom dohvacanja konfiguracijske datoteke
     * @throws DuplikatSimptoma ako postoje dva simptoma sa istim nazivom i vrijednosti
     */

    public static void spremiNoviSimptom(Simptom simptom) throws SQLException, IOException, DuplikatSimptoma {
        Connection veza = connectToDatabase();

        // Provjera duplikata

        PreparedStatement upit = veza.prepareStatement("SELECT * FROM SIMPTOM WHERE NAZIV = ? AND VRIJEDNOST = ?");

        upit.setString(1, simptom.getNaziv());
        upit.setString(2, simptom.getVrijednost().getVrijednost());

        ResultSet rs = upit.executeQuery();

        if(rs.next()) {
            closeConnectionToDatabase(veza);
            throw new DuplikatSimptoma();
        }

        // Spremanje Simptoma u Bazu podataka

        veza.setAutoCommit(false);

        upit = veza.prepareStatement("INSERT INTO SIMPTOM(NAZIV, VRIJEDNOST) VALUES (?, ?)");

        upit.setString(1, simptom.getNaziv());
        upit.setString(2, simptom.getVrijednost().getVrijednost());

        upit.executeUpdate();

        veza.commit();

        veza.setAutoCommit(true);

        closeConnectionToDatabase(veza);

    }

    /**
     * Dohvaca sve bolesti iz baze podataka
     *
     * @return bolesti iz baze
     * @throws SQLException ako je greska prilikom rada s bazom
     * @throws IOException ako je greska prilikom dohvacanja konfiguracijske datoteke
     */

    public static List<Bolest> dohvatiSveBolesti() throws SQLException, IOException {
        List<Bolest> bolesti = new ArrayList<>();
        Connection veza = connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOLEST");

        while(rs.next()) {
            bolesti.add(unosBolesti(rs, veza));
        }

        closeConnectionToDatabase(veza);

        return bolesti;

    }


    /**
     * Dohvaca bolest iz baze podataka
     *
     * @param idBolesti id bolesti koju dohvacamo
     * @return bolest koji dohvacamo
     * @throws SQLException ako je greska prilikom rada s bazom
     * @throws IOException ako je greska prilikom dohvacanja konfiguracijske datoteke
     */

    public static Bolest dohvatiBolest(Long idBolesti) throws SQLException, IOException, NepostojecaBolest {
        Bolest bolest;
        Connection veza = connectToDatabase();
        PreparedStatement upit = veza.prepareStatement("SELECT * FROM BOLEST WHERE ID = ?");

        upit.setString(1, String.valueOf(idBolesti));

        ResultSet rs = upit.executeQuery();

        if(rs.next()) {
            bolest = unosBolesti(rs, veza);
        } else {
            throw new NepostojecaBolest();
        }

        closeConnectionToDatabase(veza);

        return bolest;
    }


    /**
     * Unosi bolest iz result set-a i vraca novu bolest
     *
     * @param rs set rezultata iz baze podataka
     * @return nova bolest
     * @throws SQLException ako je greska prilikom rada s bazom
     */

    private static Bolest unosBolesti(ResultSet rs, Connection veza) throws SQLException {
        Bolest bolest;
        long idBolesti = rs.getLong("ID");
        String nazivBolesti = rs.getString("NAZIV");
        Boolean isVirus = rs.getBoolean("VIRUS");
        Set<Simptom> simptomi = new HashSet<>();
        PreparedStatement upit = veza.prepareStatement("SELECT SIMPTOM.ID, SIMPTOM.NAZIV, SIMPTOM.VRIJEDNOST FROM BOLEST\n" +
                "INNER JOIN BOLEST_SIMPTOM ON\n" +
                "BOLEST.ID = BOLEST_SIMPTOM.BOLEST_ID\n" +
                "INNER JOIN SIMPTOM ON\n" +
                "BOLEST_SIMPTOM.SIMPTOM_ID = SIMPTOM.ID\n" +
                "WHERE BOLEST.ID = ?");

        upit.setLong(1, idBolesti);

        ResultSet nrs = upit.executeQuery();

        while(nrs.next()) {
            Simptom noviSimptom = unosSimptoma(nrs);
            simptomi.add(noviSimptom);
        }

        bolest = isVirus ?
                new Virus(idBolesti, nazivBolesti, simptomi) :
                new Bolest(idBolesti, nazivBolesti, simptomi);

        return bolest;
    }


    /**
     * Sprema nova bolest u bazu podataka
     *
     * @param bolest nova bolest koji unosimo u bazu
     * @throws SQLException ako je greska prilikom rada s bazom
     * @throws IOException ako je greska prilikom dohvacanja konfiguracijske datoteke
     * @throws BolestIstihSimptoma ako se u bazi nalazi bolest koja ima iste simptome
     */

    public static void spremiNovuBolest(Bolest bolest) throws BolestIstihSimptoma, IOException, SQLException, NepostojecaBolest {
        Connection veza = connectToDatabase();
        ResultSet nrs;
        long idNovoSpremljeneBolesti;
        ResultSet rs;

        // Provjera duplikata

        Long sumaIdSimptoma = bolest.getSimptomi().stream().map(s->s.getId()).reduce((long) 0, (s1, s2) -> s1+s2);
        Integer brojSimptoma = bolest.getSimptomi().size();


        PreparedStatement preparedStatement = veza.prepareStatement("SELECT BOLEST_ID FROM BOLEST_SIMPTOM\n" +
                "GROUP BY BOLEST_ID\n" +
                "HAVING SUM(SIMPTOM_ID) = ? AND COUNT(SIMPTOM_ID)  = ?");

        preparedStatement.setLong(1, sumaIdSimptoma);
        preparedStatement.setInt(2, brojSimptoma);

        rs = preparedStatement.executeQuery();

        Long idDohvaceneVrijednosti;
        Bolest dohvacenaBolest;

        while(rs.next()) {
            idDohvaceneVrijednosti = rs.getLong("BOLEST_ID");
            dohvacenaBolest = BazaPodataka.dohvatiBolest(idDohvaceneVrijednosti);
            if(dohvacenaBolest.getSimptomi().equals(bolest.getSimptomi())) {
                closeConnectionToDatabase(veza);
                throw new BolestIstihSimptoma();
            }
        }

        // Spremanje Bolesti/Virusa u Bazu podataka

        PreparedStatement upit;

        veza.setAutoCommit(false);

        upit = veza.prepareStatement("INSERT INTO BOLEST(NAZIV, VIRUS) VALUES (?, ?)");

        upit.setString(1, bolest.getNaziv());
        upit.setBoolean(2, bolest instanceof Virus);

        upit.executeUpdate();

        veza.commit();


        upit = veza.prepareStatement("SELECT ID FROM BOLEST WHERE NAZIV = ? AND VIRUS = ?");

        upit.setString(1, bolest.getNaziv());
        upit.setBoolean(2, bolest instanceof Virus);

        nrs = upit.executeQuery();

        if(nrs.next()) {
            idNovoSpremljeneBolesti = nrs.getLong("ID");
        } else {
            idNovoSpremljeneBolesti = 1;
        }

        Set<Simptom> simptomi = bolest.getSimptomi();

        for(Simptom simptom : simptomi) {

            upit = veza.prepareStatement("INSERT INTO BOLEST_SIMPTOM(BOLEST_ID, SIMPTOM_ID) VALUES (?, ?)");

            upit.setLong(1, idNovoSpremljeneBolesti);
            upit.setLong(2, simptom.getId());

            upit.executeUpdate();
        }

        veza.commit();

        veza.setAutoCommit(true);

        closeConnectionToDatabase(veza);

    }

    /**
     * Dohvaca sve zupanije iz baze podataka
     *
     * @return zupanije iz baze
     * @throws SQLException ako je greska prilikom rada s bazom
     * @throws IOException ako je greska prilikom dohvacanja konfiguracijske datoteke
     */

    public static SortedSet<Zupanija> dohvatiSveZupanije() throws SQLException, IOException {
        SortedSet<Zupanija> zupanije = new TreeSet<>(new CovidSorter());
        Connection veza = connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ZUPANIJA");

        while(rs.next()) {
            zupanije.add(unosZupanije(rs));
        }

        closeConnectionToDatabase(veza);

        return zupanije;

    }

    /**
     * Unosi zupaniju iz result set-a i vraca novi zupaniju
     *
     * @param rs set rezultata iz baze podataka
     * @return nova zupanija
     * @throws SQLException ako je greska prilikom rada s bazom
     */

    private static Zupanija unosZupanije(ResultSet rs) throws SQLException {
        long id = rs.getLong("ID");
        String naziv = rs.getString("NAZIV");
        Integer brojStanovnika = rs.getInt("BROJ_STANOVNIKA");
        Integer brojZarazenihStanovnika = rs.getInt("BROJ_ZARAZENIH_STANOVNIKA");

        Zupanija zupanija = new Zupanija(id, naziv, brojStanovnika, brojZarazenihStanovnika);

        return zupanija;
    }

    /**
     * Dohvaca zupaniju iz baze podataka
     *
     * @param idZupanije id zupanije koju dohvacamo
     * @return zupanija koju dohvacamo
     * @throws SQLException ako je greska prilikom rada s bazom
     * @throws IOException ako je greska prilikom dohvacanja konfiguracijske datoteke
     */

    public static Zupanija dohvatiZupaniju(Long idZupanije) throws SQLException, IOException, NepostojecaZupanija {
        Zupanija zupanija;
        Connection veza = connectToDatabase();
        PreparedStatement upit = veza.prepareStatement("SELECT * FROM ZUPANIJA WHERE ID = ?");

        upit.setString(1, String.valueOf(idZupanije));

        ResultSet rs = upit.executeQuery();

        if(rs.next()) {
            zupanija = unosZupanije(rs);
        } else {
            throw new NepostojecaZupanija();
        }

        closeConnectionToDatabase(veza);

        return zupanija;
    }



    /**
     * Sprema novu zupaniju u bazu podataka
     *
     * @param zupanija nova zupanija koju unosimo u bazu
     * @throws SQLException ako je greska prilikom rada s bazom
     * @throws IOException ako je greska prilikom dohvacanja konfiguracijske datoteke
     * @throws ZupanijaIstogNaziva ako se u bazi nalazi zupanija koja ima isti naziv
     */

    public static void spremiNovuZupaniju(Zupanija zupanija) throws ZupanijaIstogNaziva, IOException, SQLException {
        Connection veza = connectToDatabase();

        // Provjera duplikata

        PreparedStatement upit = veza.prepareStatement("SELECT * FROM ZUPANIJA WHERE NAZIV = ?");

        upit.setString(1, zupanija.getNaziv());

        ResultSet rs = upit.executeQuery();

        if(rs.next()) {
            closeConnectionToDatabase(veza);
            throw new ZupanijaIstogNaziva();
        }

        // Spremanje Zupanije u Bazu podataka

        veza.setAutoCommit(false);

        upit = veza.prepareStatement("INSERT INTO ZUPANIJA(NAZIV, BROJ_STANOVNIKA, BROJ_ZARAZENIH_STANOVNIKA) VALUES (?, ?, ?)");

        upit.setString(1, zupanija.getNaziv());
        upit.setInt(2, zupanija.getBrojStanovnika());
        upit.setInt(3, zupanija.getBrojZarazenih());

        upit.executeUpdate();

        veza.commit();

        veza.setAutoCommit(true);

        closeConnectionToDatabase(veza);

    }


    /**
     * Dohvaca sve osobe iz baze podataka
     *
     * @return osobe iz baze
     * @throws SQLException ako je greska prilikom rada s bazom
     * @throws IOException ako je greska prilikom dohvacanja konfiguracijske datoteke
     */

    public static List<Osoba> dohvatiSveOsobe() throws SQLException, IOException, NepostojecaBolest, NepostojecaZupanija {
        List<Osoba> osobe = new ArrayList<>();
        Connection veza = connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM OSOBA");

        while(rs.next()) {
            osobe.add(unosOsobe(rs, veza));
        }

        for(Osoba osoba : osobe) {
            unosKontaktiranihOsoba(osoba, osobe, veza);
        }

        closeConnectionToDatabase(veza);

        return osobe;

    }

    /**
     * Unosi osobu iz result set-a i vraca novu osobu
     *
     * @param rs set rezultata iz baze podataka
     * @param veza veza s bazom podataka
     * @return nova osoba
     * @throws SQLException ako je greska prilikom rada s bazom
     * @throws IOException ako je greska prilikom dohvacanja konfiguracijske datoteke
     */

    private static Osoba unosOsobe(ResultSet rs, Connection veza) throws SQLException, IOException, NepostojecaZupanija, NepostojecaBolest {
        Osoba osoba;
        long idOsobe = rs.getLong("ID");
        String imeOsobe = rs.getString("IME");
        String prezimeOsobe = rs.getString("PREZIME");
        Date date = (Date) rs.getDate("DATUM_RODJENJA");
        Long zupanijaId = rs.getLong("ZUPANIJA_ID");
        Long bolestId = rs.getLong("BOLEST_ID");
        Zupanija zupanija = dohvatiZupaniju(zupanijaId);
        Bolest bolest = dohvatiBolest(bolestId);

        osoba = new Osoba.Builder(idOsobe).ime(imeOsobe).prezime(prezimeOsobe).datumRodjenja(date).zupanija(zupanija)
                .zarazenBolescu(bolest).build();

        return osoba;
    }

    /**
     * Trazi kontaktirane osobe u bazi podataka i vraca listu id-a kontaktiranih osoba te pridruzuje reference
     * osoba sa tim id-jem u listu kontaktiranih osoba od unesene osobe
     *
     * @param osoba osoba kojoj pridruzujemo listu kontaktiranih osoba
     * @param osobe lista svih osoba
     * @param veza veza s bazom podataka
     * @throws SQLException ako je greska prilikom rada s bazom
     */

    private static void unosKontaktiranihOsoba(Osoba osoba, List<Osoba> osobe, Connection veza) throws SQLException {
        List<Long> idKontaktiranihOsoba = new ArrayList<>();
        List<Osoba> kontaktiraneOsobe;
        PreparedStatement upit = veza.prepareStatement("SELECT KONTAKTIRANA.ID FROM OSOBA\n" +
                "INNER JOIN KONTAKTIRANE_OSOBE ON\n" +
                "OSOBA.ID = KONTAKTIRANE_OSOBE.OSOBA_ID\n" +
                "INNER JOIN OSOBA AS KONTAKTIRANA ON\n" +
                "KONTAKTIRANE_OSOBE.KONTAKTIRANA_OSOBA_ID = KONTAKTIRANA.ID\n" +
                "WHERE OSOBA.ID = ?");

        upit.setLong(1, osoba.getId());

        ResultSet nrs = upit.executeQuery();

        while(nrs.next()) {
            Long idKontaktiraneOsobe = nrs.getLong("ID");
            idKontaktiranihOsoba.add(idKontaktiraneOsobe);
        }
        kontaktiraneOsobe = osobe.stream().filter(o -> idKontaktiranihOsoba.contains(o.getId())).collect(Collectors.toList());

        osoba.setKontaktiraneOsobe(kontaktiraneOsobe);
    }


    /**
     * Dohvaca osobu iz baze podataka
     *
     * @param idOsobe id osobe koju dohvacamo
     * @return bolest koji dohvacamo
     * @throws SQLException ako je greska prilikom rada s bazom
     * @throws IOException ako je greska prilikom dohvacanja konfiguracijske datoteke
     */

    public static Osoba dohvatiOsobu(Long idOsobe) throws SQLException, IOException, NepostojecaOsoba, NepostojecaBolest, NepostojecaZupanija {
        Osoba osoba;
        Connection veza = connectToDatabase();
        PreparedStatement upit = veza.prepareStatement("SELECT * FROM OSOBA WHERE ID = ?");

        upit.setString(1, String.valueOf(idOsobe));

        ResultSet rs = upit.executeQuery();

        if(rs.next()) {
            osoba = unosOsobe(rs, veza);
        } else {
            throw new NepostojecaOsoba();
        }

        closeConnectionToDatabase(veza);

        return osoba;
    }



    /**
     * Sprema novu osobu u bazu podataka
     *
     * @param osoba nova osoba koji unosimo u bazu
     * @throws SQLException ako je greska prilikom rada s bazom
     * @throws IOException ako je greska prilikom dohvacanja konfiguracijske datoteke
     * @throws DuplikatKontaktiraneOsobe ako se u bazi nalazi bolest koja ima iste simptome
     */

    public static void spremiNovuOsobu(Osoba osoba) throws DuplikatKontaktiraneOsobe, IOException, SQLException {
        Connection veza = connectToDatabase();
        ResultSet nrs;
        PreparedStatement upit;

        // Spremanje Osobe u Bazu podataka

        veza.setAutoCommit(false);

        upit = veza.prepareStatement("INSERT INTO OSOBA(IME, PREZIME, DATUM_RODJENJA, ZUPANIJA_ID, BOLEST_ID) VALUES (?, ?, ?, ?, ?)");

        upit.setString(1, osoba.getIme());
        upit.setString(2, osoba.getPrezime());
        upit.setDate(3, osoba.getDatumRodjenja());
        upit.setLong(4, osoba.getZupanija().getId());
        upit.setLong(5, osoba.getZarazenBolescu().getId());

        upit.executeUpdate();

        veza.commit();

        long idNovoSpremljeneOsobe;

        upit = veza.prepareStatement("SELECT ID FROM OSOBA WHERE IME = ? AND PREZIME = ? AND DATUM_RODJENJA = ? AND ZUPANIJA_ID = ? AND BOLEST_ID = ?");

        upit.setString(1, osoba.getIme());
        upit.setString(2, osoba.getPrezime());
        upit.setDate(3, osoba.getDatumRodjenja());
        upit.setLong(4, osoba.getZupanija().getId());
        upit.setLong(5, osoba.getZarazenBolescu().getId());

        nrs = upit.executeQuery();

        if(nrs.next()) {
            idNovoSpremljeneOsobe = nrs.getLong("ID");
        } else {
            idNovoSpremljeneOsobe = 1;
        }

        List<Osoba> kontaktiraneOsobe = osoba.getKontaktiraneOsobe();

        for(Osoba o : kontaktiraneOsobe) {

            upit = veza.prepareStatement("INSERT INTO KONTAKTIRANE_OSOBE(OSOBA_ID, KONTAKTIRANA_OSOBA_ID) VALUES (?, ?)");

            upit.setLong(1, idNovoSpremljeneOsobe);
            upit.setLong(2, o.getId());

            upit.executeUpdate();
        }

        veza.commit();

        veza.setAutoCommit(true);

        closeConnectionToDatabase(veza);

    }

}
