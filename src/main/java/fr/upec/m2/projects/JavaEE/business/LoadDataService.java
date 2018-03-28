package fr.upec.m2.projects.JavaEE.business;

import fr.upec.m2.projects.JavaEE.model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/* This Singleton bean manage the transaction because we need control over the commit
 * if we let the container manage the transaction, the exception transaction timeout will occur.
 */


@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(value = TransactionManagementType.BEAN)

public class LoadDataService {

    private static final Logger LOG  = LogManager.getLogger(LoadDataService.class);
    private static final int PRECISION = 1; // number of persisted object before committing.
    private static final int MAX = 80; // Must be grater than 146562 to parse all tuple in csv files

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    //------------------------------------------------------------------------------------------------------------------

    @PostConstruct
    public void init() {
 
        try {
            userTransaction.begin();
        } catch (Exception e) {
            LOG.error("Error in beginning transaction: {}", e.getMessage());
        }
         try{
            AdminUser admin=new AdminUser("ramzi","chebbak","ramzi.chebbak@gmail.com","123456","04 rue salvador allende","admin");
            entityManager.persist(admin);
        }
        catch(Exception e){
            e.getMessage();
        }

        //-----zones_de_rattachement.csv--------------------------------------------------------------------------------

        String path = Thread.currentThread().getContextClassLoader()
                .getResource("data/zones_de_rattachement.csv")
                .getPath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
            Reader in = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';')
                    .withHeader("Geo Point", "Geo Shape", "ARRONDISSE", "NUM_BV", "SHAPE_Leng", "SHAPE_Area")
                    .parse(in);

            int i = 0;
            for (CSVRecord record : records) {

                // polygon coordinates
                List<Double> polygon_latitude_gps_coordinate_list = new ArrayList<>();
                List<Double> polygon_longitude_gps_coordinate_list = new ArrayList<>();
                String pointList = record.get("Geo Shape").split(": ")[2];
                pointList = pointList.substring(3, pointList.length() - 4);
                for (String point: pointList.split("\\], \\[")) {
                    String point_longitude = point.split(" ")[0];
                    String point_latitude = point.split(" ")[1];
                    point_longitude = point_longitude.substring(0, point_longitude.length() - 1);
                    Double c_latitude_double = 0.0;
                    Double c_longitude_double = 0.0;
                    try {
                        c_latitude_double = Double.parseDouble(point_latitude);
                        c_longitude_double = Double.parseDouble(point_longitude);
                    } catch (NumberFormatException e) {
                        LOG.error("GPS coordinate number {} can't be converted to double: -{}- -{}-", i,
                                point_latitude, point_longitude);
                        break;
                    }
                    polygon_latitude_gps_coordinate_list.add(c_latitude_double);
                    polygon_longitude_gps_coordinate_list.add(c_longitude_double);
                }

                // office number
                String office_number = record.get("ARRONDISSE") + "-" + record.get("NUM_BV");

                // office coordinate
                String office_latitude_coordinate_string = record.get("Geo Point").split(", ")[0];
                String office_longitude_coordinate_string = record.get("Geo Point").split(", ")[1];
                Double c_latitude_double = 0.0;
                Double c_longitude_double = 0.0;
                try {
                    c_latitude_double = Double.parseDouble(office_latitude_coordinate_string);
                    c_longitude_double = Double.parseDouble(office_longitude_coordinate_string);
                } catch (NumberFormatException e) {
                    LOG.error("GPS coordinate number {} can't be converted to double: -{}- -{}-", i,
                            office_latitude_coordinate_string, office_longitude_coordinate_string);
                    break;
                }

                // persist Zone
                entityManager.persist(new Zone(office_number, polygon_latitude_gps_coordinate_list,
                        polygon_longitude_gps_coordinate_list, c_latitude_double, c_longitude_double)
                );

                if (i % PRECISION == 0) {
                    try {
                        userTransaction.commit();
                        userTransaction.begin();
                    } catch (RollbackException e) {
                        LOG.error("Rollback transaction: {}", e.getMessage());
                        try {
                            // userTransaction.rollback();
                            userTransaction.begin();
                        } catch (Exception e1) {
                            LOG.error("begin after Rollback error: {}", e1.getMessage());
                        }
                    } catch (Exception e) {
                        LOG.error("Error in committing/beginning transaction: {}", e.getMessage());
                    }
                    LOG.info("Loading zone_de_rattachement.csv: {}%", (i / 9));
                }

                if (i == MAX) // max must be > 865
                    break;
                i++;
            }
            LOG.info("Loading zone_de_rattachement.csv: Done");
        } catch (UnsupportedEncodingException e) {
            LOG.error("can't convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("can't read csv file: {}", e.getMessage());
        }

        //-----bureaux.csv----------------------------------------------------------------------------------------------

        path = Thread.currentThread().getContextClassLoader().getResource("data/bureaux.csv").getPath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
            Reader in = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';')
                    .withHeader("objectid","num_bv","lib","adresse","cp","id_bv","geo_shape","geo_point_2d")
                    .parse(in);

            int i = 0;
            for (CSVRecord record : records) {

                // office number
                String office_number = record.get("id_bv");
                String address = record.get("adresse");
                String street_number = address.split(" ", 2)[0];
                String street_name = address.split(" ", 2)[1];

                // office address
                String zip_code = record.get("cp");

                // office name
                String office_name = record.get("lib");

                // persist Office
                entityManager.persist(new Office(office_number, office_name, street_number, street_name,zip_code));

                if (i % PRECISION == 0) {
                    try {
                        userTransaction.commit();
                        userTransaction.begin();
                    } catch (RollbackException e) {
                        LOG.error("Rollback transaction: {}", e.getMessage());
                        try {
                            // userTransaction.rollback();
                            userTransaction.begin();
                        } catch (Exception e1) {
                            LOG.error("begin after Rollback error: {}", e1.getMessage());
                        }
                    } catch (Exception e) {
                        LOG.error("Error in committing/beginning transaction: {}", e.getMessage());
                    }
                    LOG.info("Loading bureaux.csv: {}%", i / 9);
                }

                if (i == MAX) // Max must be > 896
                    break;
                i++;
            }
            LOG.info("Loading bureaux.csv: Done");
        } catch (UnsupportedEncodingException e) {
            LOG.error("cant convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("cant read csv file: {}", e.getMessage());
        }

        //-----res_leg_2.csv--------------------------------------------------------------------------------------------

        path = Thread.currentThread().getContextClassLoader().getResource("data/res_leg_2.csv").getPath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
            Reader in = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';')
                    .withHeader("libelle_du_scrutin","date_du_scrutin_jj_mm_ssaa","numero_d_arrondissement_01_a_20",
                            "numero_de_bureau_de_vote_000_a_999","numero_de_la_circonscription_01_a_18_a_partir_de_2012_ou_21_avant_2012",
                            "code_commune_insee_751_01_a_20","commune_paris_01_a_20","nombre_d_inscrits_du_bureau_de_vote",
                            "nombre_de_votants_du_bureau_de_vote","nombre_d_exprimes_du_bureau_de_vote",
                            "numero_de_depot_du_candidat_ou_liste", "nom_du_candidat_ou_liste",
                            "prenom_du_candidat_ou_liste","nombre_de_voix_du_candidat_ou_liste_obtenues_pour_le_bureau_de_vote","nombre_de_nuls_uniquement_du_bureau_de_vote",
                            "nombre_de_blancs_uniquement_du_bureau_de_vote", "nombre_de_procurations_du_bureau_de_vote")
                    .parse(in);

            int i = 0;
            for (CSVRecord record : records) {
                Resultat resultat = new Resultat_log_2(
                        record.get("numero_d_arrondissement_01_a_20"),
                        record.get("numero_de_bureau_de_vote_000_a_999"),
                        record.get("code_commune_insee_751_01_a_20"),
                        record.get("nombre_de_votants_du_bureau_de_vote"),
                        record.get("nombre_d_exprimes_du_bureau_de_vote"),
                        record.get("nom_du_candidat_ou_liste"),
                        record.get("prenom_du_candidat_ou_liste"),
                        record.get("nombre_de_voix_du_candidat_ou_liste_obtenues_pour_le_bureau_de_vote"),
                        record.get("nombre_d_inscrits_du_bureau_de_vote")
                );
                entityManager.persist(resultat);

                if (i % PRECISION == 0) {
                    try {
                        userTransaction.commit();
                        userTransaction.begin();
                    } catch (RollbackException e) {
                        LOG.error("Rollback transaction: {}", e.getMessage());
                        try {
                            // userTransaction.rollback();
                            userTransaction.begin();
                        } catch (Exception e1) {
                            LOG.error("begin after Rollback error: {}", e1.getMessage());
                        }
                    } catch (Exception e) {
                        LOG.error("Error in committing/beginning transaction: {}", e.getMessage());
                    }
                    LOG.info("Loading res_leg_2.csv: {}%", i / 16);
                }

                if (i == MAX) // max must be > 1593
                    break;
                i++;
            }
            LOG.info("Loading res_leg_2.csv: Done");
        } catch (UnsupportedEncodingException e) {
            LOG.error("cant convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("cant read csv file: {}", e.getMessage());
        }

        //-----res_leg_1.csv--------------------------------------------------------------------------------------------

        path = Thread.currentThread().getContextClassLoader().getResource("data/res_leg_1.csv").getPath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
            Reader in = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';')
                    .withHeader("libelle_du_scrutin","date_du_scrutin_jj_mm_ssaa","numero_d_arrondissement_01_a_20",
                            "numero_de_bureau_de_vote_000_a_999","numero_de_la_circonscription_01_a_18_a_partir_de_2012_ou_21_avant_2012",
                            "code_commune_insee_751_01_a_20","commune_paris_01_a_20","nombre_d_inscrits_du_bureau_de_vote",
                            "nombre_de_votants_du_bureau_de_vote","nombre_d_exprimes_du_bureau_de_vote",
                            "numero_de_depot_du_candidat_ou_liste", "nom_du_candidat_ou_liste",
                            "prenom_du_candidat_ou_liste","nombre_de_voix_du_candidat_ou_liste_obtenues_pour_le_bureau_de_vote","nombre_de_nuls_uniquement_du_bureau_de_vote",
                            "nombre_de_blancs_uniquement_du_bureau_de_vote", "nombre_de_procurations_du_bureau_de_vote")
                    .parse(in);

            int i = 0;
            for (CSVRecord record : records) {
                Resultat resultat = new Resultat_log_1(
                        record.get("numero_d_arrondissement_01_a_20"),
                        record.get("numero_de_bureau_de_vote_000_a_999"),
                        record.get("code_commune_insee_751_01_a_20"),
                        record.get("nombre_de_votants_du_bureau_de_vote"),
                        record.get("nombre_d_exprimes_du_bureau_de_vote"),
                        record.get("nom_du_candidat_ou_liste"),
                        record.get("prenom_du_candidat_ou_liste"),
                        record.get("nombre_de_voix_du_candidat_ou_liste_obtenues_pour_le_bureau_de_vote"),
                        record.get("nombre_d_inscrits_du_bureau_de_vote")
                );
                entityManager.persist(resultat);

                if (i % PRECISION == 0) {
                    try {
                        userTransaction.commit();
                        userTransaction.begin();
                    } catch (Exception e) {
                        LOG.error("Error in committing/beginning transaction: {}", e.getMessage());
                    }
                    LOG.info("Loading res_leg_1.csv: {}%", i / 146);
                }

                if (i == MAX) // max must be > 14601
                    break;
                i++;
            }
            LOG.info("Loading res_leg_1.csv: Done");
        } catch (UnsupportedEncodingException e) {
            LOG.error("cant convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("cant read csv file: {}", e.getMessage());
        }

        //-----res_psd_2.csv--------------------------------------------------------------------------------------------

        path = Thread.currentThread().getContextClassLoader().getResource("data/res_psd_2.csv").getPath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
            Reader in = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';')
                    .withHeader("libelle_du_scrutin","date_du_scrutin_jj_mm_ssaa","numero_d_arrondissement_01_a_20",
                            "numero_de_bureau_de_vote_000_a_999","numero_de_la_circonscription_01_a_18_a_partir_de_2012_ou_21_avant_2012",
                            "code_commune_insee_751_01_a_20","commune_paris_01_a_20","nombre_d_inscrits_du_bureau_de_vote",
                            "nombre_de_votants_du_bureau_de_vote","nombre_d_exprimes_du_bureau_de_vote",
                            "numero_de_depot_du_candidat_ou_liste", "nom_du_candidat_ou_liste",
                            "prenom_du_candidat_ou_liste","nombre_de_voix_du_candidat_ou_liste_obtenues_pour_le_bureau_de_vote","nombre_de_nuls_uniquement_du_bureau_de_vote",
                            "nombre_de_blancs_uniquement_du_bureau_de_vote", "nombre_de_procurations_du_bureau_de_vote")
                    .parse(in);

            int i = 0;
            for (CSVRecord record : records) {
                Resultat resultat = new Resultat_psd_2(
                        record.get("numero_d_arrondissement_01_a_20"),
                        record.get("numero_de_bureau_de_vote_000_a_999"),
                        record.get("code_commune_insee_751_01_a_20"),
                        record.get("nombre_de_votants_du_bureau_de_vote"),
                        record.get("nombre_d_exprimes_du_bureau_de_vote"),
                        record.get("nom_du_candidat_ou_liste"),
                        record.get("prenom_du_candidat_ou_liste"),
                        record.get("nombre_de_voix_du_candidat_ou_liste_obtenues_pour_le_bureau_de_vote"),
                        record.get("nombre_d_inscrits_du_bureau_de_vote")
                );
                entityManager.persist(resultat);

                if (i % PRECISION == 0) {
                    try {
                        userTransaction.commit();
                        userTransaction.begin();
                    } catch (Exception e) {
                        LOG.error("Error in committing/beginning transaction: {}", e.getMessage());
                    }
                    LOG.info("Loading res_psd_2.csv: {}%", i / 18);
                }

                if (i == MAX) // max must be > 1792
                    break;
                i++;
            }
            LOG.info("Loading res_psd_2.csv: Done");
        } catch (UnsupportedEncodingException e) {
            LOG.error("cant convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("cant read csv file: {}", e.getMessage());
        }

        //-----res_psd_1.csv--------------------------------------------------------------------------------------------

        path = Thread.currentThread().getContextClassLoader().getResource("data/res_psd_1.csv").getPath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
            Reader in = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';')
                    .withHeader("libelle_du_scrutin","date_du_scrutin_jj_mm_ssaa","numero_d_arrondissement_01_a_20",
                            "numero_de_bureau_de_vote_000_a_999",
                            "numero_de_la_circonscription_01_a_18_a_partir_de_2012_ou_21_avant_2012",
                            "code_commune_insee_751_01_a_20","commune_paris_01_a_20",
                            "nombre_d_inscrits_du_bureau_de_vote",
                            "nombre_de_votants_du_bureau_de_vote","nombre_d_exprimes_du_bureau_de_vote",
                            "numero_de_depot_du_candidat_ou_liste", "nom_du_candidat_ou_liste",
                            "prenom_du_candidat_ou_liste",
                            "nombre_de_voix_du_candidat_ou_liste_obtenues_pour_le_bureau_de_vote",
                            "nombre_de_nuls_uniquement_du_bureau_de_vote",
                            "nombre_de_blancs_uniquement_du_bureau_de_vote", "nombre_de_procurations_du_bureau_de_vote")
                    .parse(in);

            int i = 0;
            for (CSVRecord record : records) {
                Resultat resultat = new Resultat_psd_1(
                        record.get("numero_d_arrondissement_01_a_20"),
                        record.get("numero_de_bureau_de_vote_000_a_999"),
                        record.get("code_commune_insee_751_01_a_20"),
                        record.get("nombre_de_votants_du_bureau_de_vote"),
                        record.get("nombre_d_exprimes_du_bureau_de_vote"),
                        record.get("nom_du_candidat_ou_liste"),
                        record.get("prenom_du_candidat_ou_liste"),
                        record.get("nombre_de_voix_du_candidat_ou_liste_obtenues_pour_le_bureau_de_vote"),
                        record.get("nombre_d_inscrits_du_bureau_de_vote")
                );
                entityManager.persist(resultat);

                if (i % PRECISION == 0) {
                    try {
                        userTransaction.commit();
                        userTransaction.begin();
                    } catch (Exception e) {
                        LOG.error("Error in committing/beginning transaction: {}", e.getMessage());
                    }
                    LOG.info("Loading res_psd_1.csv: {}%", i / 99);
                }

                if (i == MAX) // max must be > 9857
                    break;
                i++;
            }
            LOG.info("Loading res_psd_1.csv: Done");
        } catch (UnsupportedEncodingException e) {
            LOG.error("cant convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("cant read csv file: {}", e.getMessage());
        }

        //-----adresse_paris.csv----------------------------------------------------------------------------------------

        path = Thread.currentThread().getContextClassLoader().getResource("data/adresse_paris.csv").getPath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
            Reader in = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';')
                    .withHeader("Geometry X Y", "Geometry", "N_SQ_AD", "N_VOIE", "C_SUF1", "C_SUF2", "C_SUF3", "C_AR",
                            "A_NVOIE", "B_ANGLE", "B_OFFSTDF", "B_AFFSTDF", "B_HORS75", "L_NVOIE", "L_ADR", "N_SQ_AR",
                            "N_SQ_VO", "OBJECTID")
                    .parse(in);

            int i = 0;
            for (CSVRecord record : records) {
                // GPS coordinate to Double
                String c = record.get("Geometry");
                c = c.split("\"coordinates\": \\[")[1];
                c = c.substring(0, c.length() - 2);
                String c_latitude_string = c.split(", ")[1];
                String c_longitude_string = c.split(", ")[0];

                Double c_latitude_double = 0.0;
                Double c_longitude_double = 0.0;
                try {
                    c_latitude_double = Double.parseDouble(c_latitude_string);
                    c_longitude_double = Double.parseDouble(c_longitude_string);
                } catch (NumberFormatException e) {
                    LOG.error("GPS coordinate number {} can't be converted to double: -{}- -{}-", i,
                            c_latitude_string, c_longitude_string);
                    break;
                }

                // parse street name from address string
                String street_name_number = record.get("L_ADR");
                String street_number = record.get("N_VOIE");
                String street_name = street_name_number.replaceFirst(street_number + " ", "");


                // add 75000 to zip code
                String zip_code = record.get("C_AR");
                switch (zip_code.length()) {
                    case 1:
                        zip_code = "7500" + zip_code;
                        break;
                    case 2 :
                        zip_code = "750" + zip_code;
                        break;
                    default:
                        LOG.error("zip code -{}- number {} can't be parsed, address passed", zip_code, i);
                        continue;
                }

                // Persist address object
                entityManager.persist(new Address(street_number, street_name, zip_code, c_latitude_double,
                        c_longitude_double ));

                if (i % 10000 == 0) {
                    try {
                        userTransaction.commit();
                        userTransaction.begin();
                    } catch (RollbackException e) {
                        LOG.error("Rollback transaction: {}", e.getMessage());
                        try {
                            // userTransaction.rollback();
                            userTransaction.begin();
                        } catch (Exception e1) {
                            LOG.error("begin after Rollback error: {}", e1.getMessage());
                        }
                    } catch (Exception e) {
                        LOG.error("Error in committing/beginning transaction: {}", e.getMessage());
                    }

                    String p = String.valueOf(i / 1465.62);
                    if (p.length() > 5)
                        LOG.info("Loading adresse_paris.csv: {}%", p.substring(0, 5));
                    else
                        LOG.info("Loading adresse_paris.csv: {}%", p);
                }

                if (i == MAX) // max = 146562
                    break;
                i++;
            }
            LOG.info("Loading adresse_paris.csv: Done");
        } catch (UnsupportedEncodingException e) {
            LOG.error("cant convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("cant read csv file: {}", e.getMessage());
        }

        //--------------------------------------------------------------------------------------------------------------

        try {
            userTransaction.commit();
        } catch (Exception e) {
            LOG.error("Error in committing/beginning transaction: {}", e.getMessage());
        }
           
    }
}
