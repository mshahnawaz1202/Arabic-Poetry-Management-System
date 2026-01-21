package dto;

public class SegmentDTO {
    private int segmentId;
    private int verseId;
    private String verseText;
    private String word;
    private String prefix;
    private String stem;
    private String suffix;
    private int positionInVerse;

    public SegmentDTO() {}

    public SegmentDTO(int verseId, String word, String prefix, String stem, String suffix) {
        this.verseId = verseId;
        this.word = word;
        this.prefix = prefix;
        this.stem = stem;
        this.suffix = suffix;
    }

    public SegmentDTO(int segmentId, int verseId, String word, String prefix, String stem, String suffix, int positionInVerse) {
        this.segmentId = segmentId;
        this.verseId = verseId;
        this.word = word;
        this.prefix = prefix;
        this.stem = stem;
        this.suffix = suffix;
        this.positionInVerse = positionInVerse;
    }

    public int getSegmentId() { return segmentId; }
    public void setSegmentId(int segmentId) { this.segmentId = segmentId; }

    public int getVerseId() { return verseId; }
    public void setVerseId(int verseId) { this.verseId = verseId; }

    public String getVerseText() { return verseText; }
    public void setVerseText(String verseText) { this.verseText = verseText; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }

    public String getStem() { return stem; }
    public void setStem(String stem) { this.stem = stem; }

    public String getSuffix() { return suffix; }
    public void setSuffix(String suffix) { this.suffix = suffix; }

    public int getPositionInVerse() { return positionInVerse; }
    public void setPositionInVerse(int positionInVerse) { this.positionInVerse = positionInVerse; }

    @Override
    public String toString() {
        return "Segment{" +
                "segmentId=" + segmentId +
                ", verseId=" + verseId +
                ", word='" + word + '\'' +
                ", prefix='" + prefix + '\'' +
                ", stem='" + stem + '\'' +
                ", suffix='" + suffix + '\'' +
                ", position=" + positionInVerse +
                '}';
    }
}