/*******************************************************************************
 * Copyright 2019 Universidad Politécnica de Madrid UPM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package es.upm.tfo.lst;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.FieldMethodizer;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import es.upm.tfo.lst.CodeGenerator.GenerateProject;

/**
 * @author amedrano
 *
 */
public class IntegratedTest {

	private static final String ONT_URL = "https://protege.stanford.edu/ontologies/pizza/pizza.owl";
	private static final String ONT_URL_ANN = "https://raw.githubusercontent.com/ApolloDev/apollo-sv/master/src/ontology/apollo-sv.owl";
	OWLOntology ontology = null;
	OWLOntology ontologyWithAnnotations = null;
	OWLReasonerFactory reasonerFactory = null;
	OWLOntologyManager ontManager = null;
	VelocityContext context = null;
	Template template = null;
	VelocityEngine engine = null;
	Properties props = null;
	FileWriter writer = null;
	Reader templateReader;

	@Before
	public void init() throws OWLOntologyCreationException, IOException {
		ontManager = OWLManager.createOWLOntologyManager();
		this.ontology = ontManager.loadOntologyFromOntologyDocument(new URL(ONT_URL).openStream());
		this.ontologyWithAnnotations = ontManager.loadOntologyFromOntologyDocument(new URL(ONT_URL_ANN).openStream());
		this.engine = new VelocityEngine();
		this.props = new Properties();
		props.put("file.resource.loader.path", "src/main/resources/");
		// TODO: should use class loader instead, everything in this dir should be
		// accessible with this.getClass().getClassLoader().getResource(name)
		this.engine.init(this.props);
		this.writer = new FileWriter(new File("target/output.txt"));
		this.context = new VelocityContext();
		// Variables
		this.context.put("PackageBase", "org.universAAL.ontology");
		this.context.put("isASL2", "true");
		this.context.put("License-holder", "UPM");
	}

	protected void runProject(String velociMacro) throws IOException {
		this.context.put("Date", new Date());
		GenerateProject project = new GenerateProject();
		// TODO add project variable
		this.context.put("project", project);
		this.template = engine.getTemplate(velociMacro);
		this.template.merge(context, writer);
		this.writer.close();
	}

	protected void runEnum(String velociMacro) throws IOException {
		this.context.put("ontology", this.ontologyWithAnnotations);
		runProject(velociMacro);
	}
	protected void runOntology(String velociMacro) throws IOException {
		this.context.put("ontology", this.ontology);
		runProject(velociMacro);
	}

	protected void runClass(String velociMacro) throws IOException {
		OWLDataFactory manager = ontManager.getOWLDataFactory();
		OWLClass cls = manager.getOWLClass(IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl#Pizza"));
		this.context.put("class", cls);
		runOntology(velociMacro);
	}

	@Test
	public void classTest() throws IOException {
		this.context.put("AxiomType", new FieldMethodizer("org.semanticweb.owlapi.model.AxiomType"));
		this.writer = new FileWriter(new File("target/class.java"));
		runClass("Class.java.vm");
	}

	@Test
	public void ontologyTest() throws IOException {
		this.context.put("AxiomType", new FieldMethodizer("org.semanticweb.owlapi.model.AxiomType"));
		this.writer = new FileWriter(new File("target/ontology.java"));
		runOntology("Ontology.java.vm");
	}

	@Test
	public void activatorTest() throws IOException {
		this.writer = new FileWriter(new File("target/activator.java"));
		runEnum("Activator.java.vm");
	}

	@Test
	public void enumerationTest() throws IOException  {
		// TODO find an ontology with Enumerations.
		this.writer = new FileWriter(new File("target/enumeratios.java"));
		runEnum("Enumeration.java.vm");
	}

	@Test
	public void factoryTest() throws IOException {
		this.writer = new FileWriter(new File("target/factory.java"));
		runOntology("Factory.java.vm");
	}

	@Test
	public void pomTest() throws IOException {
		this.writer = new FileWriter(new File("target/pom.xml"));
		runProject("pom.xml.vm");
	}

}
