package dev.quantumfusion.bettersleepplus.parser;

public record ActionString(String text) implements Action {

	@Override
	public  void get(StringBuilder sb, Object[] objects) {
		sb.append(text);
	}
}
