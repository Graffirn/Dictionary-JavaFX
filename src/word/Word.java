package word;

public class Word {
    private String word_target;
    private String word_explain;
    private String sound;

    public Word() {
        this.word_explain = null;
        this.word_target = null;
        this.sound = null;
    }

    public Word(String word_target, String word_explain, String sound) {
        this.word_target = word_target;
        this.word_explain = word_explain;
        this.sound = sound;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    public String getWord_target() {
        return word_target;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public String getWord_explain() {
        return word_explain;
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }
}
