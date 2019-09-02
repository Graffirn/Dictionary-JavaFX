import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.util.Locale;

public class Speech {
    public static Synthesizer synthesizer;

    public Speech() {
        try {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            this.synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
            this.synthesizer.allocate();
            this.synthesizer.resume();
        } catch (Exception ex) {
        }
    }

    public void gay(String word) {
        try {
            this.synthesizer.speakPlainText(word, null);
            this.synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
