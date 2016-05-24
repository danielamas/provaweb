package bd;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CreateBd {

	public void createDB() {
		EntityManagerFactory factory =  Persistence.createEntityManagerFactory ("filme-PU");
		factory.close();
	}

}
