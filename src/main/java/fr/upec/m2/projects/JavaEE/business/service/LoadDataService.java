package fr.upec.m2.projects.JavaEE.business.service;

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
import java.util.concurrent.TimeUnit;

@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(value = TransactionManagementType.BEAN)
public class LoadDataService {

    private static final Logger LOG  = LogManager.getLogger(LoadDataService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    private static final int PRECISION = 100;

    @PostConstruct
    public void init() {

        try {
            userTransaction.begin();
        } catch (NotSupportedException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        }


        //--------------------------------------------------------------------------------------------------------------

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

                Polygon polygon = new Polygon();

                String pointList = record.get("Geo Shape").split(": ")[2];
                pointList = pointList.substring(3, pointList.length() - 4);

                for (String point: pointList.split("\\], \\[")) {
                    Point2D point2D = new Point2D();

                    String lon = point.split(" ")[0];
                    lon = lon.substring(0, lon.length() - 1);
                    point2D.setCoordinates_longitude(lon);
                    point2D.setCoordinates_latitude(point.split(" ")[1]);
                    polygon.addPoint(point2D);
                }

                Zone zone = new Zone(
                        polygon,
                        record.get("ARRONDISSE") + "-" + record.get("NUM_BV")
                );
                entityManager.persist(zone);

                if (i % PRECISION == 0) {

                    try {
                        userTransaction.commit();
                        userTransaction.begin();
                    } catch (RollbackException e) {
                        e.printStackTrace();
                    } catch (HeuristicMixedException e) {
                        e.printStackTrace();
                    } catch (HeuristicRollbackException e) {
                        e.printStackTrace();
                    } catch (SystemException e) {
                        e.printStackTrace();
                    } catch (NotSupportedException e) {
                        e.printStackTrace();
                    }

                    LOG.info("Loading zone_de_rattachement.csv: {}%", (i / 9));
                }


                if (i == 870) // max = 865
                    break;
                i++;
            }
            LOG.info("Loading zone_de_rattachement.csv: Done");
        } catch (UnsupportedEncodingException e) {
            LOG.error("can't convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("can't read csv file: {}", e.getMessage());
        }

        //--------------------------------------------------------------------------------------------------------------

        path = Thread.currentThread().getContextClassLoader().getResource("data/bureaux.csv").getPath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
            Reader in = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';')
                    .withHeader("objectid","num_bv","lib","adresse","cp","id_bv","geo_shape","geo_point_2d")
                    .parse(in);

            int i = 0;
            for (CSVRecord record : records) {
                Bureau bureau = new Bureau(
                        record.get("objectid"),
                        record.get("id_bv"),
                        record.get("lib"),
                        record.get("adresse"),
                        record.get("cp")
                );
                entityManager.persist(bureau);
                if (i % PRECISION == 0) {

                    try {
                        userTransaction.commit();
                        userTransaction.begin();
                    } catch (RollbackException e) {
                        e.printStackTrace();
                    } catch (HeuristicMixedException e) {
                        e.printStackTrace();
                    } catch (HeuristicRollbackException e) {
                        e.printStackTrace();
                    } catch (SystemException e) {
                        e.printStackTrace();
                    } catch (NotSupportedException e) {
                        e.printStackTrace();
                    }

                    LOG.info("Loading bureaux.csv: {}%", i / 9);
                }

                if (i == 1000) // Max = 896
                    break;
                i++;
            }
            LOG.info("Loading bureaux.csv: Done");
        } catch (UnsupportedEncodingException e) {
            LOG.error("cant convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("cant read csv file: {}", e.getMessage());
        }

        //--------------------------------------------------------------------------------------------------------------

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
                Resultat_leg_2 resultat = new Resultat_leg_2(
                        record.get("numero_d_arrondissement_01_a_20"),
                        record.get("numero_de_bureau_de_vote_000_a_999"),
                        record.get("code_commune_insee_751_01_a_20"),
                        record.get("nombre_de_votants_du_bureau_de_vote"),
                        record.get("nombre_d_exprimes_du_bureau_de_vote"),
                        record.get("nom_du_candidat_ou_liste"),
                        record.get("prenom_du_candidat_ou_liste"),
                        record.get("nombre_de_voix_du_candidat_ou_liste_obtenues_pour_le_bureau_de_vote")

                );
                entityManager.persist(resultat);

                if (i % PRECISION == 0) {

                    try {
                        userTransaction.commit();
                        userTransaction.begin();
                    } catch (RollbackException e) {
                        e.printStackTrace();
                    } catch (HeuristicMixedException e) {
                        e.printStackTrace();
                    } catch (HeuristicRollbackException e) {
                        e.printStackTrace();
                    } catch (SystemException e) {
                        e.printStackTrace();
                    } catch (NotSupportedException e) {
                        e.printStackTrace();
                    }

                    LOG.info("Loading res_leg_2.csv: {}%", i / 16);
                }

                if (i == 1600) // max = 1593
                    break;
                i++;
            }
            LOG.info("Loading res_leg_2.csv: Done");
        } catch (UnsupportedEncodingException e) {
            LOG.error("cant convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("cant read csv file: {}", e.getMessage());
        }

        //--------------------------------------------------------------------------------------------------------------

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
                Resultat_leg_1 resultat = new Resultat_leg_1(
                        record.get("numero_d_arrondissement_01_a_20"),
                        record.get("numero_de_bureau_de_vote_000_a_999"),
                        record.get("code_commune_insee_751_01_a_20"),
                        record.get("nombre_de_votants_du_bureau_de_vote"),
                        record.get("nombre_d_exprimes_du_bureau_de_vote"),
                        record.get("nom_du_candidat_ou_liste"),
                        record.get("prenom_du_candidat_ou_liste"),
                        record.get("nombre_de_voix_du_candidat_ou_liste_obtenues_pour_le_bureau_de_vote")

                );
                entityManager.persist(resultat);

                if (i % PRECISION == 0) {

                    try {
                        userTransaction.commit();
                        userTransaction.begin();
                    } catch (RollbackException e) {
                        e.printStackTrace();
                    } catch (HeuristicMixedException e) {
                        e.printStackTrace();
                    } catch (HeuristicRollbackException e) {
                        e.printStackTrace();
                    } catch (SystemException e) {
                        e.printStackTrace();
                    } catch (NotSupportedException e) {
                        e.printStackTrace();
                    }

                    LOG.info("Loading res_leg_1.csv: {}%", i / 146);
                }

                if (i == 14700) // max = 14601
                    break;
                i++;
            }
            LOG.info("Loading res_leg_1.csv: Done");
        } catch (UnsupportedEncodingException e) {
            LOG.error("cant convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("cant read csv file: {}", e.getMessage());
        }

        //--------------------------------------------------------------------------------------------------------------

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
                Resultat_psd_2 resultat = new Resultat_psd_2(
                        record.get("numero_d_arrondissement_01_a_20"),
                        record.get("numero_de_bureau_de_vote_000_a_999"),
                        record.get("code_commune_insee_751_01_a_20"),
                        record.get("nombre_de_votants_du_bureau_de_vote"),
                        record.get("nombre_d_exprimes_du_bureau_de_vote"),
                        record.get("nom_du_candidat_ou_liste"),
                        record.get("prenom_du_candidat_ou_liste"),
                        record.get("nombre_de_voix_du_candidat_ou_liste_obtenues_pour_le_bureau_de_vote")

                );
                entityManager.persist(resultat);

                if (i % PRECISION == 0) {

                    try {
                        userTransaction.commit();
                        userTransaction.begin();
                    } catch (RollbackException e) {
                        e.printStackTrace();
                    } catch (HeuristicMixedException e) {
                        e.printStackTrace();
                    } catch (HeuristicRollbackException e) {
                        e.printStackTrace();
                    } catch (SystemException e) {
                        e.printStackTrace();
                    } catch (NotSupportedException e) {
                        e.printStackTrace();
                    }

                    LOG.info("Loading res_psd_2.csv: {}%", i / 18);
                }

                if (i == 1800) // max = 1792
                    break;
                i++;
            }
            LOG.info("Loading res_psd_2.csv: Done");
        } catch (UnsupportedEncodingException e) {
            LOG.error("cant convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("cant read csv file: {}", e.getMessage());
        }

        //--------------------------------------------------------------------------------------------------------------

        path = Thread.currentThread().getContextClassLoader().getResource("data/res_psd_1.csv").getPath();
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
                Resultat_psd_1 resultat = new Resultat_psd_1(
                        record.get("numero_d_arrondissement_01_a_20"),
                        record.get("numero_de_bureau_de_vote_000_a_999"),
                        record.get("code_commune_insee_751_01_a_20"),
                        record.get("nombre_de_votants_du_bureau_de_vote"),
                        record.get("nombre_d_inscrits_du_bureau_de_vote"),
                        record.get("nombre_d_exprimes_du_bureau_de_vote"),
                        record.get("nom_du_candidat_ou_liste"),
                        record.get("prenom_du_candidat_ou_liste"),
                        record.get("nombre_de_voix_du_candidat_ou_liste_obtenues_pour_le_bureau_de_vote")
                );
                entityManager.persist(resultat);

                if (i % PRECISION == 0) {

                    try {
                        userTransaction.commit();
                        userTransaction.begin();
                    } catch (RollbackException e) {
                        e.printStackTrace();
                    } catch (HeuristicMixedException e) {
                        e.printStackTrace();
                    } catch (HeuristicRollbackException e) {
                        e.printStackTrace();
                    } catch (SystemException e) {
                        e.printStackTrace();
                    } catch (NotSupportedException e) {
                        e.printStackTrace();
                    }

                    LOG.info("Loading res_psd_1.csv: {}%", i / 99);
                }

                if (i == 10000) // max = 9857
                    break;
                i++;
            }
            LOG.info("Loading res_psd_1.csv: Done");
        } catch (UnsupportedEncodingException e) {
            LOG.error("cant convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("cant read csv file: {}", e.getMessage());
        }

        //--------------------------------------------------------------------------------------------------------------

        path = Thread.currentThread().getContextClassLoader().getResource("data/adresse_paris.csv").getPath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
            Reader in = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';')
                    .withHeader("Geometry X Y", "Geometry", "N_SQ_AD", "N_VOIE", "C_SUF1", "C_SUF2", "C_SUF3", "C_AR", "A_NVOIE", "B_ANGLE",
                            "B_OFFSTDF", "B_AFFSTDF", "B_HORS75", "L_NVOIE", "L_ADR", "N_SQ_AR", "N_SQ_VO", "OBJECTID")
                    .parse(in);

            int i = 0;
            for (CSVRecord record : records) {
                String c = record.get("Geometry");
                c = c.split("\"coordinates\": \\[")[1];
                c = c.substring(0, c.length() - 2);

                Adresse adresse = new Adresse(
                        record.get("N_VOIE"),
                        record.get("L_ADR"),
                        record.get("C_AR"),

                        new Point2D(
                                c.split(", ")[1],
                                c.split(", ")[0]
                        )
                );
                entityManager.persist(adresse);

                if (LOG.isErrorEnabled() && i % PRECISION == 0) {

                    try {
                        userTransaction.commit();
                        userTransaction.begin();
                    } catch (RollbackException e) {
                        e.printStackTrace();
                    } catch (HeuristicMixedException e) {
                        e.printStackTrace();
                    } catch (HeuristicRollbackException e) {
                        e.printStackTrace();
                    } catch (SystemException e) {
                        e.printStackTrace();
                    } catch (NotSupportedException e) {
                        e.printStackTrace();
                    }

                    String p = String.valueOf(i / 1465.62);
                    if (p.length() > 5)
                        LOG.info("Loading adresse_paris.csv: {}%", p.substring(0, 5));
                    else
                        LOG.info("Loading adresse_paris.csv: {}%", p);
                }

                if (i == 147000) // max = 146562
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
        } catch (RollbackException e) {
            e.printStackTrace();
        } catch (HeuristicMixedException e) {
            e.printStackTrace();
        } catch (HeuristicRollbackException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }
}
