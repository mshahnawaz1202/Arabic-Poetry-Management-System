package dto;

public class PoetDTO {
    private int poetId;
    private String poetName;
    private String biography;

    public PoetDTO() {}

    public PoetDTO(int poetId, String poetName, String biography) {
        this.poetId = poetId;
        this.poetName = poetName;
        this.biography = biography;
    }

    public PoetDTO(String poetName, String biography) {
        this.poetName = poetName;
        this.biography = biography;
    }

    public int getPoetId() {
        return poetId;
    }

    public void setPoetId(int poetId) {
        this.poetId = poetId;
    }

    public String getPoetName() {
        return poetName;
    }

    public void setPoetName(String poetName) {
        this.poetName = poetName;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public String toString() {
        return poetName;
    }
}
