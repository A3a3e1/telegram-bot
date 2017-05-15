package name.voropaiev.bot.command.listenum;

public enum Command {
	
	ADD("/add"),
	LIST("/list"),
	PING("/ping"),
	RANDOM("/random"),
	START("/start"),
	STOP("/stop"),
	
	NOT_A_COMMAND("-1");
	
	private final String text;
	
	private Command (final String text) {
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
