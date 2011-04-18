package fr.natoine.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import fr.natoine.model_document.Document;
import fr.natoine.model_resource.URI;
import fr.natoine.model_resource.UriStatus;

import junit.framework.TestCase;

public class DocumentTest  extends TestCase
{
	public DocumentTest(String name) 
	{
	    super(name);
	}
	
	public void testCreate()
	{
		Document _testDocument = new Document();
		_testDocument.setContextCreation("Document Test");
		_testDocument.setCreation(new Date());
		_testDocument.setLabel("test document");
		URI _representsResource = new URI();
		_representsResource.setEffectiveURI("http://www.testURI.fr");
		_testDocument.setRepresentsResource(_representsResource);
		_testDocument.setAccess(_representsResource); // same URI to access and Represent the Document.
		
		Collection<URI> uris = new ArrayList<URI>() ;
		URI _testURI1 = new URI();
		_testURI1.setEffectiveURI("http://www.testURI1.fr");
		uris.add(_testURI1);
		URI _testURI2 = new URI();
		_testURI2.setEffectiveURI("http://www.testURI2.fr");
		uris.add(_testURI2);
		_testDocument.setUris(uris);
		
		Collection<UriStatus> _status = new ArrayList<UriStatus>() ;
		UriStatus _status1 = new UriStatus();
		_status1.setLabel("test père status");
		_status.add(_status1);
		UriStatus _status2 = new UriStatus();
		_status2.setLabel("test fils status");
		_status2.setFather(_status1);
		_status.add(_status2);
		_testDocument.setUrisStatus(_status);
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("document");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
        	tx.begin();
        	em.persist(_testDocument);
        	tx.commit();
        }
        catch(Exception e)
        {
        	 System.out.println( "[DocumentTest] unable to persist" );
        	 System.out.println(e.getStackTrace());
        }
        em.close();
	}
	
	public void testRetrieve()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("document");
        EntityManager newEm = emf.createEntityManager();
        EntityTransaction newTx = newEm.getTransaction();
        newTx.begin();
        List documents = newEm.createQuery("from Document").getResultList();
        System.out.println( documents.size() + " document(s) found" );
        Document loadedDocument ;
        for (Object u : documents) 
        {
        	loadedDocument = (Document) u;
            System.out.println("[DocumentTest] Document id : " + loadedDocument.getId()  
            		+ " contextCreation : " + loadedDocument.getContextCreation()
            		+ " label : " + loadedDocument.getLabel()
            		+ " date : " + loadedDocument.getCreation()
            		);
        }
        newTx.commit();
        newEm.close();
        // Shutting down the application
        emf.close();
	}
}
