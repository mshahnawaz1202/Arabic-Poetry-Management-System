package dto;

public class IndexResultDTO {
    private int verseId;
    private String verseText;
    private int verseNo;
    private String poemTitle;
    private String poetName;
    private String bookName;
    private String word;
    private String type; // "token", "lemma", "root"

    public IndexResultDTO() {}

    public IndexResultDTO(int verseId, String verseText, int verseNo, String poemTitle, 
                         String poetName, String bookName, String word, String type) {
        this.verseId = verseId;
        this.verseText = verseText;
        this.verseNo = verseNo;
        this.poemTitle = poemTitle;
        this.poetName = poetName;
        this.bookName = bookName;
        this.word = word;
        this.type = type;
    }

    // Getters and setters
    public int getVerseId() { return verseId; }
    public void setVerseId(int verseId) { this.verseId = verseId; }

    public String getVerseText() { return verseText; }
    public void setVerseText(String verseText) { this.verseText = verseText; }

    public int getVerseNo() { return verseNo; }
    public void setVerseNo(int verseNo) { this.verseNo = verseNo; }

    public String getPoemTitle() { return poemTitle; }
    public void setPoemTitle(String poemTitle) { this.poemTitle = poemTitle; }

    public String getPoetName() { return poetName; }
    public void setPoetName(String poetName) { this.poetName = poetName; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}