package com.shitajimado.academicwritingrecommender;

import com.shitajimado.academicwritingrecommender.entities.*;
import com.shitajimado.academicwritingrecommender.services.GateService;
import gate.CorpusController;
import gate.Gate;
import gate.util.GateException;
import gate.util.GateRuntimeException;
import gate.util.persistence.PersistenceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@SpringBootApplication
public class AcademicWritingRecommenderApplication {
	@Autowired private CorpusRepository corpusRepository;
	@Autowired private DocumentRepository documentRepository;
	@Autowired private TextRepository textRepository;
	@Autowired private GateService gateService;

	public static void main(String[] args) {
		SpringApplication.run(AcademicWritingRecommenderApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Beans provided by Spring Boot:");
			String[] beans = ctx.getBeanDefinitionNames();
			Arrays.sort(beans);

			for (String name : beans) {
				// System.out.println(name);
			}
		};
	}

	@Bean
	public CorpusController corpusController() throws GateException, URISyntaxException, IOException {
		Gate.init();

		var resource = Optional.ofNullable(getClass().getClassLoader().getResource("gate/corpus-pipeline.xml"))
				.orElseThrow(() -> new GateRuntimeException("GATE program not found"));

		return (CorpusController) PersistenceManager.loadObjectFromUrl(resource);
	}
}
