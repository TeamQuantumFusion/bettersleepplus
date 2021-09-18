package dev.quantumfusion.bettersleepplus.parser;

public record ActionValue(String text, int pos) implements Action {

	public void get(StringBuilder sb, Object[] objects) {
		sb.append(objects[pos]).append(text);
	}
}
