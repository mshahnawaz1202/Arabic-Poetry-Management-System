package dto;

public class LemmaDTO {
    private int lemmaId;
    private int verseId;
    private String verseText;
    private String word;
    private String lemma;
    private int positionInVerse;

    public LemmaDTO() {}

    public LemmaDTO(int verseId, String word, String lemma) {
        this.verseId = verseId;
        this.word = word;
        this.lemma = lemma;
    }

    public LemmaDTO(int lemmaId, int verseId, String word, String lemma, int positionInVerse) {
        this.lemmaId = lemmaId;
        this.verseId = verseId;
        this.word = word;
        this.lemma = lemma;
        this.positionInVerse = positionInVerse;
    }

    public int getLemmaId() { return lemmaId; }
    public void setLemmaId(int lemmaId) { this.lemmaId = lemmaId; }

    public int getVerseId() { return verseId; }
    public void setVerseId(int verseId) { this.verseId = verseId; }

    public String getVerseText() { return verseText; }
    public void setVerseText(String verseText) { this.verseText = verseText; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getLemma() { return lemma; }
    public void setLemma(String lemma) { this.lemma = lemma; }

    public int getPositionInVerse() { return positionInVerse; }
    public void setPositionInVerse(int positionInVerse) { this.positionInVerse = positionInVerse; }

    @Override
    public String toString() {
        return "Lemma{" +
                "lemmaId=" + lemmaId +
                ", verseId=" + verseId +
                ", word='" + word + '\'' +
                ", lemma='" + lemma + '\'' +
                ", position=" + positionInVerse +
                '}';
    }
}