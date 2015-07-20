package com.ontotext.kstoilov.interview.task1;

import java.io.OutputStream;
import java.util.List;

import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.vocabulary.FOAF;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.Rio;
import org.openrdf.sail.memory.MemoryStore;

import info.aduna.iteration.Iterations;

public class RdfBuilder {
	
	private String namespace;
	
	private List<String> people;
	
	private Model model;
	
	public RdfBuilder(String namespace, List<String> people) throws RepositoryException {
		
		this.namespace = namespace;
		this.people = people;
		this.model = createModel(people);
	}
	
	private Model createModel(List<String> people) throws RepositoryException {
		
		Repository rep = new SailRepository(new MemoryStore());
		rep.initialize();
		ValueFactory f  = rep.getValueFactory();
		RepositoryConnection conn = rep.getConnection();
		Model model = null;
		try {
			for (String person : people) {
				URI personURI= f.createURI(namespace, person);
				conn.add(personURI, RDF.TYPE, FOAF.PERSON);
				conn.add(personURI, RDFS.LABEL, f.createLiteral(person, XMLSchema.STRING));
			}
			RepositoryResult<Statement> statements =  conn.getStatements(null, null, null, true);
			model = Iterations.addAll(statements, new LinkedHashModel());
		}
		finally {
			conn.close();
		}
		
		return model;
	}
	
	public void write(OutputStream out, RDFFormat format) throws RDFHandlerException {		
		Rio.write(model, out, format);
	}
	
}
