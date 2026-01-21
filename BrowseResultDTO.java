package dto;

public class BrowseResultDTO {
    private int verseId;
    private String text;
    private String poemTitle;
    private String poetName;
    private String bookName;
    private int verseNo;

    public BrowseResultDTO() {}

    public BrowseResultDTO(int verseId, String text, String poemTitle, String poetName, String bookName, int verseNo) {
        this.verseId = verseId;
        this.text = text;
        this.poemTitle = poemTitle;
        this.poetName = poetName;
        this.bookName = bookName;
        this.verseNo = verseNo;
    }

    // Getters and setters
    public int getVerseId() { return verseId; }
    public void setVerseId(int verseId) { this.verseId = verseId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getPoemTitle() { return poemTitle; }
    public void setPoemTitle(String poemTitle) { this.poemTitle = poemTitle; }

    public String getPoetName() { return poetName; }
    public void setPoetName(String poetName) { this.poetName = poetName; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public int getVerseNo() { return verseNo; }
    public void setVerseNo(int verseNo) { this.verseNo = verseNo; }
}