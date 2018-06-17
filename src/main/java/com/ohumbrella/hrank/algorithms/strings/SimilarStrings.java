package com.ohumbrella.hrank.algorithms.strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimilarStrings {

	private class TestCases {
		private int[] toSearch;
		private List<ArrayRangePair> subString;

		TestCases(int[] toSearch, List<ArrayRangePair> subString) {
			this.toSearch = toSearch;
			this.subString = subString;
		}

		int[] getToSearch() {
			return toSearch;
		}

		List<ArrayRangePair> getSubstrings() {
			return subString;
		}
	}

	private class ArrayRangePair {
		int lowIndex;
		int highIndex;

		ArrayRangePair(int lowIndex, int highIndex) {

			this.lowIndex = lowIndex;
			this.highIndex = highIndex;
		}

		int getLowIndex() {
			return lowIndex;
		}

		int getHighIndex() {
			return highIndex;
		}
	}

	private class CharacterEquivalents {
		private static final char UNSET_NULL = '\u0000';
		private int[] equivalents = new int[10];
		private int[] reverseEquivalents = new int[10];

		boolean areEquivalent(int indexI, int indexJ) {
			boolean result;

			if(equivalents[indexI] == UNSET_NULL  && reverseEquivalents[indexJ] == UNSET_NULL) {
				equivalents[indexI] = indexJ;
				reverseEquivalents[indexJ] = indexI;
				result = true;
			}
			else {
				result = equivalents[indexI] == indexJ;
			}

			return result;
		}

		void reset() {
			Arrays.fill(equivalents, UNSET_NULL);
			Arrays.fill(reverseEquivalents, UNSET_NULL);
		}
	}

	public static void main(String[] args) {
		new SimilarStrings().performSolution();
	}

	public void performSolution() {
		TestCases testCases = getInput();

		StringBuilder output = new StringBuilder();
		for(ArrayRangePair substring : testCases.getSubstrings()) {
			output.append(getCountOfSimilarStrings(substring, testCases.getToSearch()));
			output.append(System.lineSeparator());
		}
		System.out.println(output.toString());
	}

	private TestCases getInput() {
		String inputLine;
		List<ArrayRangePair> substrings = new ArrayList<>();
		int[] toSearch;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/SimilarStrings11")))) {
			//try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			final String[] metadata = reader.readLine().trim().split(" ");
			toSearch = convertInputLineToIndexArray(reader.readLine().trim());

			int expectedTestcases = Integer.valueOf(metadata[1]);
			while((inputLine = reader.readLine()) != null) {
				String[] substringIndexes = inputLine.trim().split(" ");
				final int lowIndex = Integer.valueOf(substringIndexes[0]) - 1;
				final int highIndex = Integer.valueOf(substringIndexes[1]);
				substrings.add(new ArrayRangePair(lowIndex, highIndex));
			}

			if(substrings.size() != expectedTestcases) {
				throw new IllegalStateException(
						"Input did not contain the expected number of test cases (" +
								expectedTestcases + ")");
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

		return new TestCases(toSearch, substrings);
	}

	private int[] convertInputLineToIndexArray(String toConvert) {
		return toConvert.chars()
				.map(this::getBaseZeroShiftedCharacterIndexValue)
				.toArray();
	}

	private int getBaseZeroShiftedCharacterIndexValue(int toShift) {
		return toShift - 97;
	}

	private long getCountOfSimilarStrings(ArrayRangePair substring, int[] toSearch) {
		CharacterEquivalents equivalents = new CharacterEquivalents();
		final int substringLength = substring.getHighIndex() - substring.getLowIndex();

		long matchCount = 0;

		for(int i = 0; i <= toSearch.length - substringLength; i++) {
			if(checkSubstringSimilarityAgainstRange(substring, toSearch, i, equivalents)) {
				matchCount++;
			}
		}

		return matchCount;
	}

	private boolean checkSubstringSimilarityAgainstRange(
			ArrayRangePair substring, int[] toSearch, int baseSearchIndex, CharacterEquivalents equivalents) {
		final int substringLength = substring.getHighIndex() - substring.getLowIndex();

		equivalents.reset();
		boolean result = true;

		for(int i = 0; i < substringLength; i++) {
			if( ! equivalents.areEquivalent(
					toSearch[substring.getLowIndex() + i], toSearch[i + baseSearchIndex])) {
				result = false;
				break;
			}
		}

		return result;
	}


}