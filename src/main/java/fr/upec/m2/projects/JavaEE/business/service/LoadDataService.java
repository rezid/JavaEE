package fr.upec.m2.projects.JavaEE.business.service;

import fr.upec.m2.projects.JavaEE.model.Bureau;
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

            for (CSVRecord record : records) {

                Bureau bureau = new Bureau(
                        record.get("objectid"),
                        record.get("num_bv"),
                        record.get("lib"),
                        record.get("adresse"),
                        record.get("cp")
                );
                entityManager.persist(bureau);
            }
            LOG.info("Loading in DB is done: bureaux.csv");
        } catch (UnsupportedEncodingException e) {
            LOG.error("cant convert path to csv file: {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("cant read csv file: {}", e.getMessage());
        }
    }
}
