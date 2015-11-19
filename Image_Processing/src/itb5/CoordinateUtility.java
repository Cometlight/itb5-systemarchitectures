package itb5;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import itb5.types.Coordinate;

public class CoordinateUtility {
	private final static String coordinateRegex = "\\((-?\\d+),(-?\\d+)\\)";
	private final static String coordinateRegexSimple = "\\(-?\\d+,-?\\d+\\)";

	/**
	 * Parses the specified string and returns a list of coordinates.
	 */
	public static LinkedList<Coordinate> toCoordinateList(String str) {
		if (str == null) {
			return null;
		}

		Pattern pattern = Pattern.compile(coordinateRegexSimple);
		Matcher matcher = pattern.matcher(str);

		LinkedList<Coordinate> list = new LinkedList<>();
		while (matcher.find()) {
			Coordinate coordinate = toCoordinate(matcher.group());
			if (coordinate != null) {
				list.addLast(coordinate);
			}
		}
		return list;

	}

	/**
	 * Parses the specified string and returns a coordinate.
	 */
	public static Coordinate toCoordinate(String str) {
		if (str == null) {
			return null;
		}

		Pattern pattern = Pattern.compile(coordinateRegex);
		Matcher matcher = pattern.matcher(str);

		if (matcher.matches()) {
			try {
				int x = Integer.parseInt(matcher.group(1));
				int y = Integer.parseInt(matcher.group(2));
				return new Coordinate(x, y);
			} catch (Exception e) {
				// noop
			}
		}

		return null;
	}
}
