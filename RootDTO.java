package dto;

public class RootDTO {
    private int rootId;
    private int verseId;
    private String verseText;
    private String word;
    private String root;
    private int positionInVerse;

    public RootDTO() {}

    public RootDTO(int verseId, String word, String root) {
        this.verseId = verseId;
        this.word = word;
        this.root = root;
    }

    public RootDTO(int rootId, int verseId, String word, String root, int positionInVerse) {
        this.rootId = rootId;
        this.verseId = verseId;
        this.word = word;
        this.root = root;
        this.positionInVerse = positionInVerse;
    }

    public int getRootId() { return rootId; }
    public void setRootId(int rootId) { this.rootId = rootId; }

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
        return "Root{" +
                "rootId=" + rootId +
                ", verseId=" + verseId +
                ", word='" + word + '\'' +
                ", root='" + root + '\'' +
                ", position=" + positionInVerse +
                '}';
    }
}