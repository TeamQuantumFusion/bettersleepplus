package dev.quantumfusion.bettersleepplus.parser;

import java.util.LinkedHashMap;
import java.util.Map;

public class CodeParser {
	private static final Map<String, Integer> values = new LinkedHashMap<>();

	static {
		addValue("countdown");
		addValue("seconds");
		addValue("minutes");
		addValue("hours");
		addValue("sleepingcolor");
		addValue("playerssleeping");
		addValue("players");
	}

	public static void addValue(String name) {
		values.put(name, values.size());
	}

	public static Calc parseFast(String string) {
		final char[] chars = string.toCharArray();

		int length = 0;
		for (char c : chars) if (c == '}') length++;

		final Action[] actions = new Action[length * 2];
		final StringBuilder sb = new StringBuilder();

		int pos = 0;
		boolean skipEntire = false;
		for (char c : chars) {
			if (c == '\\') skipEntire = true;
			else if (c == '{') {

				if (skipEntire) sb.append(c);
				else actions[pos++] = new ActionString(getAndClear(sb));

			} else if (c == '}') {
				if (skipEntire) {
					sb.append(c);
					skipEntire = false;
				} else actions[pos++] = new ActionValue("", values.get(getAndClear(sb)));
			} else sb.append(c);
		}
		return new Calc(actions, new Object[values.size()], values);
	}

	private static String getAndClear(StringBuilder sb) {
		final String string = sb.toString();
		sb.delete(0, sb.length());
		return string;
	}
}
