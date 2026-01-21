package dto;

public class TokenDTO {
    private int tokenId;
    private int verseId;
    private String verseText;
    private String word;
    private String root;
    private int positionInVerse;

    public TokenDTO(int verseId, String word, String root) {
        this.verseId = verseId;
        this.word = word;
        this.root = root;
    }

    public TokenDTO(int verseId, String verseText, String word, String root) {
        this.verseId = verseId;
        this.verseText = verseText;
        this.word = word;
        this.root = root;
    }

    public TokenDTO(int tokenId, int verseId, String word, String root, int positionInVerse) {
        this.tokenId = tokenId;
        this.verseId = verseId;
        this.word = word;
        this.root = root;
        this.positionInVerse = positionInVerse;
    }

    public int getTokenId() { return tokenId; }
    public void setTokenId(int tokenId) { this.tokenId = tokenId; }

    public int getVerseId() { return verseId; }
    public void setVerseId(int verseId) { this.verseId = verseId; }

    public String getVerseText() { return verseText; }
    public void setVerseText(String verseText) { this.verseText = verseText; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getRoot() { return root; }
    public void setRoot(String root) { this.root = root; }

    public int getPositionInVerse() { return positionInVerse; }
    public void setPositionInVerse(int positionInVerse) { this.positionInVerse = positionInVerse; }

    @Override
    public String toString() {
        return "Token{" +
                "tokenId=" + tokenId +
                ", verseId=" + verseId +
                ", word='" + word + '\'' +
                ", root='" + root + '\'' +
                ", position=" + positionInVerse +
                '}';
    }
}