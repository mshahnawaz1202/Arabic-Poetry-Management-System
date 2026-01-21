package dto;

public class FrequencyDTO {
    private String word;
    private int frequency;

    public FrequencyDTO(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public FrequencyDTO() {
		// TODO Auto-generated constructor stub
	}

	public String getWord() { return word; }
    public int getFrequency() { return frequency; }
}
