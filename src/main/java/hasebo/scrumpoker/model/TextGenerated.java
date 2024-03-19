package hasebo.scrumpoker.model;

public class TextGenerated {

    private String randomText;

    public TextGenerated(String randomText) {
        this.randomText = randomText;
    }

    public void assignGeneretedText(String textToAssign) {
        this.randomText = textToAssign;
    }

    public String getGeneratedText() {
        return randomText;
    }

//    @Override
//    public String toString() {
//        return randomText;
//    }


}
