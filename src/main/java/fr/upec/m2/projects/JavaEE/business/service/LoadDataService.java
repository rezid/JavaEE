package fr.upec.m2.projects.JavaEE.business.service;

import fr.upec.m2.projects.JavaEE.model.Bureau;
import fr.upec.m2.projects.JavaEE.model.Resultat_psd_1;
import fr.upec.m2.projects.JavaEE.model.Resultat_psd_2;
import fr.upec.m2.projects.JavaEE.model.Resultat_log_1;
import fr.upec.m2.projects.JavaEE.model.Resultat_log_2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.net.URLDecoder;

@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class LoadDataService {

    private static final Logger LOG  = LogManager.getLogger(LoadDataService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        String path = Thread.currentThread().getContextClassLoader().getResource("data/bureaux.csv").getPath();

        try {
            path = URLDecoder.decode(path, "UTF-8");
            Reader in = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';')
                    .withHeader("objectid","num_bv","lib","adresse","cp","id_bv","geo_shape","geo_point_2d")
                    .parse(in);
            int i=0;
            for (CSVRecord record : records) {
                i++;
                Bureau bureau = new Bureau(
                        record.get("objectid"),
                        record.get("id_bv"),
                        record.get("lib"),
                        record.get("adresse"),
                        record.get("cp")
                );
                entityManager.persist(bureau);
            if(i==100)
                break;
            }
            LOG.info("Loading in DB is done: bureaux.csv");
        } catch (UnsupportedEncodingException e) {
            LOG.error("cant convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("cant read csv file: {}", e.getMessage());
        }


        load_res("data/resultat_psd_2017_1er.csv",0);
        load_res("data/res_psd_2.csv",1);
        load_res("data/res_leg_1.csv",2);
        load_res("data/res_leg_2.csv",3);

        
    }

    void load_res(String csvpath,int votetype)
    { 
    String path = Thread.currentThread().getContextClassLoader().getResource(csvpath).getPath();
    
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
            if(votetype == 0 ) { 
                int i=0;
                for (CSVRecord record : records) {
                    i++;
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
    
                    //LOG.error(resultat.getNom_du_candidat() + " " + resultat.getPrenom_du_candidat());
                    if(i==100)
                        break;
                }
                LOG.info("Loading in DB is done: bureaux.csv");
            } else if (votetype == 1 ) { 
                int i=0;
                for (CSVRecord record : records) {
                    i++;
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
    
                    //LOG.error(resultat.getNom_du_candidat() + " " + resultat.getPrenom_du_candidat());
                    if(i==100)
                        break;
                }
                LOG.info("Loading in DB is done: bureaux.csv");
            } else if (votetype == 3 )  { 
                int i=0;
                for (CSVRecord record : records) {
                    i++;
                    Resultat_log_1 resultat = new Resultat_log_1(
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
    
                    //LOG.error(resultat.getNom_du_candidat() + " " + resultat.getPrenom_du_candidat());
                    if(i==100)
                        break;
                }
                LOG.info("Loading in DB is done: bureaux.csv");
            } else if (votetype == 4 ) {
                int i=0;
                for (CSVRecord record : records) {
                    i++;
                    Resultat_log_2 resultat = new Resultat_log_2(
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
    
                    //LOG.error(resultat.getNom_du_candidat() + " " + resultat.getPrenom_du_candidat());
                    if(i==100)
                        break;
                }
                LOG.info("Loading in DB is done: bureaux.csv");
            }

            } catch (UnsupportedEncodingException e) {
                LOG.error("cant convert path to csv file: {}", e.getMessage());
            } catch (IOException e) {
                LOG.error("cant read csv file: {}", e.getMessage());
            }
        }
}
