package name.voropaiev.bot.jpa.entities;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;


/**
 * The persistent class for the EVENT_PHRASES database table.
 * 
 */
@Entity
@Data
@Table(name="EVENT_PHRASES")
@NamedQueries({
	@NamedQuery(name="EventPhrase.findAll", query="SELECT e FROM EventPhrase e"),
	@NamedQuery(name="EventPhrase.findAllByEvent", query="SELECT p.phrase FROM EventPhrase p WHERE p.event = :event")
})
public class EventPhrase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private int event;

	private String phrase;

}