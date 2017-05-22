package name.voropaiev.bot.enums;

public enum CommandEnum {
	
	ADD("/add"),
	LIST("/list"),
	PING("/ping"),
	RANDOM("/random"),
	START("/start"),
	STOP("/stop"),
	
	NOT_A_COMMAND("-1");
	
	private final String text;
	
	private CommandEnum (final String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	public String isNotCommand() {
		return "-1";
	}
	
}
