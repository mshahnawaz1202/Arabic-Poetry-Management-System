package bl.services;

import java.util.ArrayList;
import java.util.List;

import dal.IVerseDAO;
import dal.VerseDAO;
import dto.VerseDTO;

public class NGram implements INGram{
    
    private IVerseDAO verseDAO;
    
    public NGram(IVerseDAO verseDAO) {
		this.verseDAO = verseDAO;
	}
    @Override
    public List<String> generateNGrams(String text, int n) {
        List<String> grams = new ArrayList<>();
        
        if (text == null || text.length() < n) {
            return grams;
        }
        

        text = text.trim().replaceAll("\\s+", " ");
        
        for (int i = 0; i <= text.length() - n; i++) {
            grams.add(text.substring(i, i + n));
        }
        
        return grams;
    }
    
    @Override
    public int intersectionCount(List<String> gramsA, List<String> gramsB) {
        int count = 0;
        List<Boolean> used = new ArrayList<>();
        

        for (int i = 0; i < gramsB.size(); i++) {
            used.add(false);
        }
        
        for (String gramA : gramsA) {
            for (int j = 0; j < gramsB.size(); j++) {
                if (!used.get(j) && gramA.equals(gramsB.get(j))) {
                    count++;
                    used.set(j, true);
                    break;
                }
            }
        }
        
        return count;
    }
    
    @Override
    public List<String> splitWords(String text) {
        List<String> words = new ArrayList<>();
        
        if (text == null || text.isEmpty()) {
            return words;
        }
        

        String cleanedText = text.replaceAll("[^\\p{IsArabic}\\s]", "");
        String[] wordArray = cleanedText.trim().split("\\s+");
        
        for (String word : wordArray) {
            if (!word.isEmpty()) {
                words.add(word);
            }
        }
        
        return words;
    }
    
    @Override
    public int countCommonWords(List<String> wordsA, List<String> wordsB) {
        int count = 0;
        List<Boolean> used = new ArrayList<>();
        

        for (int i = 0; i < wordsB.size(); i++) {
            used.add(false);
        }
        
        for (String wordA : wordsA) {
            for (int j = 0; j < wordsB.size(); j++) {
                if (!used.get(j) && wordA.equals(wordsB.get(j))) {
                    count++;
                    used.set(j, true);
                    break;
                }
            }
        }
        
        return count;
    }
    @Override
    public double calculateSimilarity(String text1, String text2, int n) {
        List<String> gramsA = generateNGrams(text1, n);
        List<String> gramsB = generateNGrams(text2, n);
        
        if (gramsA.isEmpty()) {
            return 0.0;
        }
        
        int intersection = intersectionCount(gramsA, gramsB);
        return ((double) intersection / gramsA.size()) * 100.0;
    }
    
    @Override
    public List<NGramResult> findSimilarVerses(String targetText, int n, double minSimilarity) {
        List<NGramResult> results = new ArrayList<>();
        
        try {
            if (targetText == null || targetText.trim().isEmpty()) {
                System.err.println("Target text is empty");
                return results;
            }

            targetText = targetText.trim();
            
            System.out.println("Finding similar verses for text: " + targetText);
            System.out.println("N-gram size: " + n);
            System.out.println("Min similarity: " + minSimilarity);
            

            
            
            
            
            List<VerseDTO> allVerses = verseDAO.getAllVerses();
            System.out.println("Total verses in database: " + allVerses.size());
            
            List<String> targetGrams = generateNGrams(targetText, n);
            List<String> targetWords = splitWords(targetText);
            
            if (targetGrams.isEmpty()) {
                System.err.println("Target verse is too short for N=" + n);
                return results;
            }
            
            System.out.println("Target N-grams count: " + targetGrams.size());
            System.out.println("Target words count: " + targetWords.size());

            int processedCount = 0;
            for (VerseDTO verse : allVerses) {
                String verseText = verse.getText();
                
                if (verseText == null || verseText.trim().isEmpty()) {
                    continue;
                }
                
                List<String> verseGrams = generateNGrams(verseText, n);
                List<String> verseWords = splitWords(verseText);
                
                if (verseGrams.isEmpty()) {
                    continue;
                }
                

                int intersection = intersectionCount(targetGrams, verseGrams);
                double similarity = ((double) intersection / targetGrams.size()) * 100.0;
                

                int commonWords = countCommonWords(targetWords, verseWords);
                
                processedCount++;
                
                if (similarity >= minSimilarity) {
                    NGramResult result = new NGramResult(
                        verse,
                        similarity,
                        targetGrams.size(),
                        verseGrams.size(),
                        intersection,
                        commonWords
                    );
                    results.add(result);
                    
                    System.out.println("Found match - Verse ID: " + verse.getVerseId() + 
                                     ", Similarity: " + similarity + "%");
                }
            }
            
            System.out.println("Processed " + processedCount + " verses");
            System.out.println("Found " + results.size() + " matches");
            

            results.sort((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()));
            
        } catch (Exception e) {
            System.err.println("Error finding similar verses: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    public static class NGramResult {
        private VerseDTO verse;
        private double similarity;
        private int targetGramCount;
        private int verseGramCount;
        private int intersection;
        private int commonWords;
        
        public NGramResult(VerseDTO verse, double similarity, int targetGramCount, 
                          int verseGramCount, int intersection, int commonWords) {
            this.verse = verse;
            this.similarity = similarity;
            this.targetGramCount = targetGramCount;
            this.verseGramCount = verseGramCount;
            this.intersection = intersection;
            this.commonWords = commonWords;
        }
        
        public VerseDTO getVerse() { return verse; }
        public double getSimilarity() { return similarity; }
        public int getTargetGramCount() { return targetGramCount; }
        public int getVerseGramCount() { return verseGramCount; }
        public int getIntersection() { return intersection; }
        public int getCommonWords() { return commonWords; }
    }
}