package name.voropaiev.bot.jpa.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import name.voropaiev.bot.jpa.entities.Phrase;

public class PhraseService {

	public static void addPhrase(String nickName, String phraseString) {

		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("TeleBotPU");

		EntityManager entitymanager = emfactory.createEntityManager();
		entitymanager.getTransaction().begin();

		Phrase phrase = new Phrase();
		phrase.setNickName(nickName);
		phrase.setPhrase(phraseString);

		entitymanager.persist(phrase);
		entitymanager.getTransaction().commit();

		entitymanager.close();
		emfactory.close();
	}

	@SuppressWarnings("unchecked")
	public static List<Phrase> getAllPhrases() {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("TeleBotPU");
		EntityManager entitymanager = emfactory.createEntityManager();
		Query query = entitymanager.createNamedQuery("Phrase.findAll");

		return (List<Phrase>) query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public static List<Phrase> getPhraseByNickname(String nickName) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("TeleBotPU");
		EntityManager entitymanager = emfactory.createEntityManager();
		Query query = entitymanager.createNamedQuery("Phrase.findAllByNickName").setParameter("nickName", nickName);

		return (List<Phrase>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public static List<String> getKeywordList() {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("TeleBotPU");
		EntityManager entitymanager = emfactory.createEntityManager();
		Query query = entitymanager.createNamedQuery("Phrase.findAllNickNames");
		List<String> keywordList = new ArrayList<>();
		List<Phrase> phraseList = (List<Phrase>) query.getResultList();
		System.out.println(phraseList.size());
		
		for (Phrase phrase: phraseList) {
			keywordList.add(phrase.getNickName().trim().toLowerCase());
		}

		return keywordList;
	}

}
