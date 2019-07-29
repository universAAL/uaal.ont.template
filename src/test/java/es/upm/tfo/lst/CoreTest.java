package es.upm.tfo.lst;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Date;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import es.upm.tfo.lst.CodeGenerator.GenerateProject;
import es.upm.tfo.lst.CodeGenerator.model.TemplateDataModel;
import es.upm.tfo.lst.CodeGenerator.owl.OntologyLoader;
import es.upm.tfo.lst.CodeGenerator.xmlparser.XmlParser;
import uk.ac.manchester.cs.jfact.kernel.Ontology;

public class CoreTest {

	private XmlParser parser=null;
	private TemplateDataModel model=null;
	private GenerateProject genPro=null;
	private OntologyLoader ontologyLoader=null;
	//----constants
	private final String pizza_ontology ="https://protege.stanford.edu/ontologies/pizza/pizza.owl";
	private final String uaal_device ="http://ontology.universaal.org/Device.owl";
	private final String uaal_lightning ="http://ontology.universaal.org/Lighting.owl";
	private final String uaal_health ="http://ontology.universaal.org/Health.owl";
	
	
	private final String baseOutput="target/generated/";
	private OWLOntologyManager ontManager;
	
	@Before
	public void init() {
		ontManager = OWLManager.createOWLOntologyManager();
		PropertyConfigurator.configure("./src/main/resources/log4jConfigFile/log4j.properties");
		this.parser = new XmlParser();
		this.ontologyLoader = new OntologyLoader();
		this.genPro = new GenerateProject();
	}
	
	@Test
	public void PizzaTest() {
			
		 try {
			 	this.model=this.parser.generateXMLCoordinator("src/main/resources/MavenProject.xml");
				this.genPro.setMainModel(this.model);
				//set the ontology to project and recursive state
				this.genPro.addOntology(this.ontologyLoader.loadOntology(this.pizza_ontology), true);
				//set output directory
				this.genPro.setOutputFolder(this.baseOutput+"pizza/");
				//optional: add value to variables. You can add extra variable plus the variables provided into XML file
				this.genPro.setVariable("mavenArtifactID","test");
				File f = new File(this.baseOutput+"pizza/");
				f.mkdirs();
				genPro.process();
		} catch (Exception e) {
			e.printStackTrace();
			genPro.addError(e);
		}

		assertTrue(genPro.getErrors().isEmpty());
	}
	
	@Test
	public void LightningUAALTest() {
			
		 try {
			 	this.model=this.parser.generateXMLCoordinator("src/main/resources/MavenProject.xml");
				this.genPro.setMainModel(this.model);
				
				//set the ontology to project and recursive state
				this.genPro.addOntology(this.ontologyLoader.loadOntology(this.uaal_lightning), false);
				//set output directory
				this.genPro.setOutputFolder(this.baseOutput+"lightning/");
				//optional: add value to variables. You can add extra variable plus the variables provided into XML file
				this.genPro.setVariable("mavenArtifactID","test");
				File f = new File(this.baseOutput+"lightning/");
				f.mkdirs();
				genPro.process();
		} catch (Exception e) {
			e.printStackTrace();
			genPro.addError(e);
		}

		assertTrue(genPro.getErrors().isEmpty());
	}
	
	@Test
	public void DeviceUAALTest() {
			
		 try {
			 	this.model=this.parser.generateXMLCoordinator("src/main/resources/MavenProject.xml");
				this.genPro.setMainModel(this.model);
				//set the ontology to project and recursive state
				this.genPro.addOntology(this.ontologyLoader.loadOntology(this.uaal_device), false);
				//set output directory
				this.genPro.setOutputFolder(this.baseOutput+"device/");
				//optional: add value to variables. You can add extra variable plus the variables provided into XML file
				this.genPro.setVariable("mavenArtifactID","test");
				File f = new File(this.baseOutput+"device/");
				f.mkdirs();
				genPro.process();
		} catch (Exception e) {
			e.printStackTrace();
			genPro.addError(e);
		}

		assertTrue(genPro.getErrors().isEmpty());
	}

	@Test
	public void HealthTest() {
		
		 try {
			 	this.model=this.parser.generateXMLCoordinator("src/main/resources/MavenProject.xml");
				this.genPro.setMainModel(this.model);
				//set the ontology to project and recursive state
				this.genPro.addOntology(this.ontologyLoader.loadOntology(this.uaal_health), false);
				//set output directory
				this.genPro.setOutputFolder(this.baseOutput+"health/");
				//optional: add value to variables. You can add extra variable plus the variables provided into XML file
				this.genPro.setVariable("mavenArtifactID","test");
				File f = new File(this.baseOutput+"health/");
				f.mkdirs();
				genPro.process();
		} catch (Exception e) {
			e.printStackTrace();
			genPro.addError(e);
		}

		assertTrue(genPro.getErrors().isEmpty());
	}

}
