package bl.services;

public interface TextAnalysisService {
    Tokenization tokenize(String text);
    Lemmatization lemmatize(String text);
    RootExtraction extractRoots(String text);
}
