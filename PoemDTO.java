package dto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dal.DatabaseConfigure;

public class PoemDTO {
    private int poemId;
    private String title;
    private int poetId;
    private int bookId;

    public PoemDTO() {}

    public PoemDTO(int poemId, String title, int poetId, int bookId) {
        this.poemId = poemId;
        this.title = title;
        this.poetId = poetId;
        this.bookId = bookId;
    }

    public PoemDTO(String title, int poetId, int bookId) {
        this.title = title;
        this.poetId = poetId;
        this.bookId = bookId;
    }

    public int getPoemId() {
        return poemId;
    }

    public void setPoemId(int poemId) {
        this.poemId = poemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPoetId() {
        return poetId;
    }

    public void setPoetId(int poetId) {
        this.poetId = poetId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    public List<PoemDTO> getPoemsByBookId(int bookId) {
        List<PoemDTO> poems = new ArrayList<>();
        String sql = "SELECT * FROM poem WHERE book_id = ?";
        try (Connection con = DatabaseConfigure.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                poems.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving poems: " + e.getMessage());
        }
        return poems;
    }
    private PoemDTO map(ResultSet rs) throws SQLException {
        PoemDTO poem = new PoemDTO();
        poem.setPoemId(rs.getInt("poem_id"));
        poem.setTitle(rs.getString("title"));
        poem.setPoetId(rs.getInt("poet_id"));
        poem.setBookId(rs.getInt("book_id")); // make sure your table has this column
        return poem;
    }


    @Override
    public String toString() {
        return title;
    }
}
