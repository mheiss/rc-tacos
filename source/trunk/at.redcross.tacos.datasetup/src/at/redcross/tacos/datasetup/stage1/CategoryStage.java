package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;

import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Category;

public class CategoryStage implements DatasetupStage {

	@Override
	public void performCleanup(EntityManager manager) {
		TypedQuery<Category> query = manager.createQuery("from Category", Category.class);
		for (Category category : query.getResultList()) {
			manager.remove(category);
		}
	}

	@Override
	public void performImport(EntityManager manager) {
		{
			Category category = new Category();
			category.setName("Ausbildung");
			category.setDescription("Hier werden ausbildungsrelevante Themen angekündigt.");
			manager.persist(category);
		}
		{
			Category category = new Category();
			category.setName("Ambulanzdienst");
			category.setDescription("Ambulanzdienste aller Art.");
			manager.persist(category);
		}
	}
}
