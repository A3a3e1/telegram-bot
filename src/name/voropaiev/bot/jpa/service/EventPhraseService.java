package name.voropaiev.bot.jpa.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class EventPhraseService {

//	public static void main(String[] args) {
//		List<String> list = getPhraseByEvent(2);
//		for (String s : list) {
//			System.out.println(s);
//		}
//	}
	
	public static List<String> getPhraseByEvent(int eventType) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("TeleBotPU");
		EntityManager entitymanager = emfactory.createEntityManager();
		Query query = entitymanager.createNamedQuery("EventPhrase.findAllByEvent").setParameter("event", eventType);

		return query.getResultList();
	}

}
