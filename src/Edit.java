import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Edit {
    private String pronun;
    private String def;

    @FXML
    private Label alert;

    @FXML
    public Button editButton;

    @FXML
    private TextField pronunciationText;

    @FXML
    private Label engWord;

    @FXML
    private TextField defText;

    public String getPronun() {
        return pronun;
    }

    public void setPronun(String pronun) {
        this.pronun = pronun;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public void setTitle(String eng) {
        this.engWord.setText(eng);
    }

    private void closeStage() {
        Stage stage = (Stage) editButton.getScene().getWindow();
        stage.close();
    }

    public void clickEdit() {
        if (!this.defText.getText().trim().isEmpty() && !this.pronunciationText.getText().trim().isEmpty()) {
            alert.setVisible(false);
            String pronunciation = pronunciationText.getText().trim();
            String def = defText.getText().trim();
            this.setDef(def);
            this.setPronun(pronunciation);
            closeStage();
        } else {
            alert.setVisible(true);
        }
    }
}
