package dto;

import java.sql.Date;

public class BookDTO {
    private int bookId;
    private String title;
    private String compiler;
    private Date era;

    public BookDTO() {}

    public BookDTO(int bookid, String title, String compiler, Date era) {
        this.title = title;
        this.compiler = compiler;
        this.era = era;
        this.bookId = bookid;
    }
    
    public BookDTO(String title, String compiler, Date era) {
        this.title = title;
        this.compiler = compiler;
        this.era = era;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompiler() { return compiler; }
    public void setCompiler(String compiler) { this.compiler = compiler; }

    public Date getEra() { return era; }
    public void setEra(Date era) { this.era = era; }
    
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    @Override
    public String toString() {
        return title; // Only show the title in lists
    }
}