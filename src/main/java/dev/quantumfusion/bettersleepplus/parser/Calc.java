package dev.quantumfusion.bettersleepplus.parser;

import java.util.Map;

public class Calc {
	private final Action[] actions;
	public final Object[] values;
	private final Map<String, Integer> valueMappings;

	public Calc(Action[] actions, Object[] values, Map<String, Integer> valueMappings) {
		this.actions = actions;
		this.values = values;
		this.valueMappings = valueMappings;
	}

	public int getPos(String name) {
		return valueMappings.get(name);
	}
	public void setValue(int pos, Object object) {
		values[pos] = object;
	}

	public String calc() {
		final StringBuilder sb = new StringBuilder();
		for (Action action : actions) if (action != null) action.get(sb, values);
		return sb.toString();
	}

}
