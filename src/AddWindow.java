import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import word.Word;

import java.util.ArrayList;

public class AddWindow {
    private Word word;

    public AddWindow() {
        this.word = new Word(null,null,null);
    }

    @FXML
    private Label alert;

    @FXML
    private Button addBut;

    @FXML
    private TextField engW;

    @FXML
    private CheckBox check;

    @FXML
    private TextField proW;

    @FXML
    private TextField vnW;

    public boolean getCheck() {
        return check.isSelected();
    }

    private void closeStage() {
        Stage stage = (Stage) addBut.getScene().getWindow();
        stage.close();
    }

    public Word getWord() {
        return word;
    }

    public void clickAdd() {
        if (!this.engW.getText().trim().isEmpty() && !this.vnW.getText().trim().isEmpty() && !this.proW.getText().trim().isEmpty()) {
            alert.setVisible(false);
            String eng = engW.getText().trim();
            String pronun = proW.getText().trim();
            String def = vnW.getText().trim();
            this.word = new Word(eng, def, pronun);
            closeStage();
        } else {
            alert.setVisible(true);
        }
    }

}
