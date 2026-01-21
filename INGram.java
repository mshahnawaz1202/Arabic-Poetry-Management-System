package bl.services;

import java.util.List;

import bl.services.NGram.NGramResult;

public interface INGram {

	List<String> generateNGrams(String text, int n);

	int intersectionCount(List<String> gramsA, List<String> gramsB);

	List<String> splitWords(String text);

	int countCommonWords(List<String> wordsA, List<String> wordsB);

	double calculateSimilarity(String text1, String text2, int n);

	List<NGramResult> findSimilarVerses(String targetText, int n, double minSimilarity);

}
