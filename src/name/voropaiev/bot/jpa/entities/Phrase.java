package name.voropaiev.bot.jpa.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the PHRASES database table.
 * 
 */
@Entity
@Table(name="PHRASES")
@NamedQueries({
	@NamedQuery(name="Phrase.findAll", query="SELECT p FROM Phrase p"),
	@NamedQuery(name="Phrase.findAllNickNames", query="SELECT DISTINCT p FROM Phrase p"),
	@NamedQuery(name="Phrase.findAllByNickName", query="SELECT p FROM Phrase p WHERE p.nickName = :nickName")
})
public class Phrase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="NICK_NAME")
	private String nickName;

	private String phrase;

	@Column(name="TELEGRAM_ID")
	private int telegramId;

	public Phrase() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhrase() {
		return this.phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public int getTelegramId() {
		return this.telegramId;
	}

	public void setTelegramId(int telegramId) {
		this.telegramId = telegramId;
	}

}