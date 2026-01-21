package dto;

public class VerseDTO {
    private int verseId;
    private int poemId;
    private int verseNo;
    private String text;
    private String textDiacritized;
    private String translation;
    private String notes;

    public VerseDTO() {}

    public VerseDTO(int verseId, int poemId, int verseNo, String text, String textDiacritized,
                    String translation, String notes) {
        this.verseId = verseId;
        this.poemId = poemId;
        this.verseNo = verseNo;
        this.text = text;
        this.textDiacritized = textDiacritized;
        this.translation = translation;
        this.notes = notes;
    }

    public VerseDTO(int poemId, int verseNo, String text, String textDiacritized,
                    String translation, String notes) {
        this.poemId = poemId;
        this.verseNo = verseNo;
        this.text = text;
        this.textDiacritized = textDiacritized;
        this.translation = translation;
        this.notes = notes;
    }

    public int getVerseId() {
        return verseId;
    }

    public void setVerseId(int verseId) {
        this.verseId = verseId;
    }

    public int getPoemId() {
        return poemId;
    }

    public void setPoemId(int poemId) {
        this.poemId = poemId;
    }

    public int getVerseNo() {
        return verseNo;
    }

    public void setVerseNo(int verseNo) {
        this.verseNo = verseNo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextDiacritized() {
        return textDiacritized;
    }

    public void setTextDiacritized(String textDiacritized) {
        this.textDiacritized = textDiacritized;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Verse " + verseNo + ": " + text;
    }
}
