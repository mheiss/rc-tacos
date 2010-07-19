package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;

import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Link;

// creates link stages
public class LinkStage implements DatasetupStage {

    @Override
    public void performCleanup(EntityManager manager) {
        TypedQuery<Link> query = manager.createQuery("from Link", Link.class);
        for (Link link : query.getResultList()) {
            manager.remove(link);
        }
    }

    @Override
    public void performImport(EntityManager manager) {
        {
            Link link = new Link();
            link.setName("http://www.st.roteskreuz.at/1.html");
            link.setDescription("Rotes Kreuz");
            manager.persist(link);
        }
        {
        	Link link = new Link();
            link.setName("http://info.st.redcross.or.at/rknet/login.aspx");
            link.setDescription("RK Net Mitarbeiterverwaltung");
            manager.persist(link);
        }
        {
        	Link link = new Link();
            link.setName("http://consense.st.roteskreuz.at/");
            link.setDescription("Consense Dokumentenmanager");
            manager.persist(link);
        }
        {
            Link link = new Link();
            link.setName("http://www.st.roteskreuz.at/9.html");
            link.setDescription("RK-Bruck/Kapfenberg");
            manager.persist(link);
        }
        {
        	Link link = new Link();
            link.setName("http://webmail.st.redcross.or.at/");
            link.setDescription("Webmail RotesKreuz");
            manager.persist(link);
        }
    }
}
