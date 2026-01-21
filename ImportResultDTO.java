package dto;

/***
 * @author shahn
 */
public class ImportResultDTO {
    private String bookTitle;
    private int poemsImported;
    private String message;

    public ImportResultDTO() {}

    public ImportResultDTO(String bookTitle, int poemsImported, String message) {
        this.bookTitle = bookTitle;
        this.poemsImported = poemsImported;
        this.message = message;
    }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public int getPoemsImported() { return poemsImported; }
    public void setPoemsImported(int poemsImported) { this.poemsImported = poemsImported; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public String toString() {
        return "ImportResultDTO{" +
                "bookTitle='" + bookTitle + '\'' +
                ", poemsImported=" + poemsImported +
                ", message='" + message + '\'' +
                '}';
    }
}
