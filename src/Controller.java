import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sqlite.DataBase;
import word.Word;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    static Speech mrKevin;
    static ArrayList<String> dictionary = new ArrayList<String>();
    static DataBase data = new DataBase();
    /*
     * Search tab
     */
    @FXML
    private TextField SearchBarWord;
    @FXML
    private TextArea txtDefinition;
    @FXML
    ListView Wordlist = new ListView();
    @FXML
    ListView RecentList = new ListView();
    /*
     * My words tab
     */
    @FXML
    private TextField SearchMW;
    @FXML
    private TextArea mwDef;
    @FXML
    private ListView mwList = new ListView();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.mrKevin = new Speech();
        dictionary = data.getEng();
        Collections.sort(dictionary);
        Wordlist.setItems(data.getListView());
        RecentList.setItems(data.recent());
        mwList.setItems(data.myword());
    }

    /*
     * Lay dinh nghia
     */
    @FXML
    void getDefinition() {
        try {
            txtDefinition.setWrapText(true);
            String wordToDefine;
            wordToDefine = SearchBarWord.getText();
            Word word = data.getWord(wordToDefine);
            txtDefinition.setText(word.getSound() + "\n" + word.getWord_explain());
        } catch (Exception e) {
            Wordlist.setItems(data.searchWord(SearchBarWord.getText()));
            return;
        }
        data.deleteRecent(SearchBarWord.getText());
        data.insertRecent(SearchBarWord.getText());
        RecentList.setItems(data.recent());
    }

    @FXML
    void getDefinitionMW() {
        try {
            mwDef.setWrapText(true);
            String wordToDefine;
            wordToDefine = SearchMW.getText();
            Word word = data.getWord(wordToDefine);
            mwDef.setText(word.getSound() + "\n" + word.getWord_explain());
        } catch (Exception e) {
            mwList.setItems(data.searchMW(SearchMW.getText()));
            return;
        }
    }

    /**
     * Hien nghia khi bam vao tu
     */
    public void MouseClickOnWord() {
        Wordlist.setOnMouseClicked((EventHandler<? super MouseEvent>) arg0 -> {
            String w = Wordlist.getSelectionModel().getSelectedItem().toString();
            SearchBarWord.setText(w);
            getDefinition();
        });
        RecentList.setOnMouseClicked((EventHandler<? super MouseEvent>) arg0 -> {
            String w = RecentList.getSelectionModel().getSelectedItem().toString();
            SearchBarWord.setText(w);
            getDefinition();
        });
    }

    public void MouseClickOnWordMW() {
        mwList.setOnMouseClicked((EventHandler<? super MouseEvent>) arg0 -> {
            String w = mwList.getSelectionModel().getSelectedItem().toString();
            SearchMW.setText(w);
            getDefinitionMW();
        });
    }

    /*
     * Refresh search bar khi bam vao
     */
    public void MouseClickOnSearchBar() {
        SearchBarWord.setOnMouseClicked((EventHandler<? super MouseEvent>) arg0 -> {
            SearchBarWord.clear();
            txtDefinition.clear();
            Wordlist.setItems(data.getListView());
        });
    }

    public void MouseClickOnSearchBarMW() {
        SearchMW.setOnMouseClicked((EventHandler<? super MouseEvent>) arg0 -> {
            SearchMW.clear();
            mwDef.clear();
            mwList.setItems(data.myword());
        });
    }

    /*
     * Hien goi y
     */
    public void InputSearchBarChange() {
        String str = SearchBarWord.getText();
        Wordlist.setItems(data.searchWord(str));
    }

    public void InputSearchBarChangeMW() {
        String str = SearchMW.getText();
        mwList.setItems(data.searchMW(str));
    }

    /*
     * Nhan enter de tim kiem nhanh
     */
    public void buttonPressed(KeyEvent e) {
        if (e.getCode().toString().equals("ENTER")) {
            getDefinition();
            return;
        }
    }

    public void buttonPressedMW(KeyEvent e) {
        if (e.getCode().toString().equals("ENTER")) {
            getDefinitionMW();
            return;
        }
    }

    /*
     * Them vao My Word
     */
    public void addToMW() {
        String str = SearchBarWord.getText();
        data.insertMyWord(str);
        mwList.setItems(data.myword());
    }


    /*
     * Xoa tu
     */
    public void deleteWord() {
        String word = SearchBarWord.getText();
        data.delete(word);
        data.deleteMW(word);
        data.deleteRecent(word);
        dictionary = data.getEng();
        Collections.sort(dictionary);
        Wordlist.setItems(data.getListView());
        RecentList.setItems(data.recent());
        mwList.setItems(data.myword());
        SearchBarWord.clear();
        txtDefinition.setText("Done!");
    }

    public void deleteMWord() {
        String word = SearchMW.getText();
        data.deleteMW(word);
        Collections.sort(dictionary);
        mwList.setItems(data.myword());
        SearchMW.clear();
        mwDef.setText("Done!");
    }

    /*
     * Edit word
     */
    public void editWord() {
        if (txtDefinition.getText().trim().isEmpty()) {
            Parent parental = null;
            FXMLLoader fxmlLoaderal = new FXMLLoader(getClass().getResource("alertEdit.fxml"));
            try {
                parental = fxmlLoaderal.load();
                Scene sceneal = new Scene(parental, 300, 150);
                Stage stageal = new Stage();
                stageal.initModality(Modality.APPLICATION_MODAL);
                stageal.setScene(sceneal);
                stageal.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Parent parent = null;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit.fxml"));
                parent = fxmlLoader.load();
                Edit dialogController = fxmlLoader.<Edit>getController();
                dialogController.setTitle(SearchBarWord.getText());
                Scene scene = new Scene(parent, 300, 200);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                try {
                    String hold = SearchBarWord.getText();
                    data.updateWord(SearchBarWord.getText(), dialogController.getDef(), dialogController.getPronun());
                    SearchBarWord.setText(hold);
                    getDefinition();
                } catch (NullPointerException e) {
                    return;
                }
            } catch (IOException e) {
                return;
            }
        }
    }

    public void editWordMW() {
        if (mwDef.getText().trim().isEmpty()) {
            Parent parental = null;
            FXMLLoader fxmlLoaderal = new FXMLLoader(getClass().getResource("alertEdit.fxml"));
            try {
                parental = fxmlLoaderal.load();
                Scene sceneal = new Scene(parental, 300, 150);
                Stage stageal = new Stage();
                stageal.initModality(Modality.APPLICATION_MODAL);
                stageal.setScene(sceneal);
                stageal.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Parent parent = null;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit.fxml"));
                parent = fxmlLoader.load();
                Edit dialogController = fxmlLoader.<Edit>getController();
                dialogController.setTitle(SearchMW.getText());
                Scene scene = new Scene(parent, 300, 200);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                try {
                    String hold = SearchMW.getText();
                    data.updateWord(hold, dialogController.getDef(), dialogController.getPronun());
                    SearchMW.setText(hold);
                    getDefinitionMW();
                } catch (NullPointerException e) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * Them tu moi
     */
    public void addWord() {
        Parent parent = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addWindow.fxml"));
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AddWindow dialogController = fxmlLoader.<AddWindow>getController();
        Scene scene = new Scene(parent, 450, 280);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        Word word = dialogController.getWord();
        try {
            data.getWord(word.getWord_target()).equals(null);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Word is already in database. Do you want to edit?");
            SearchBarWord.setText(word.getWord_target());
            getDefinition();
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                editWord();
            }
        } catch (Exception e) {
            try {
                data.insertWord(word);
                Wordlist.setItems(data.getListView());
                if (dialogController.getCheck()) {
                    data.insertMyWord(word.getWord_target());
                    mwList.setItems(data.myword());
                }
            } catch (NullPointerException np) {
                return;
            }
        }
    }

    public void speech() {
        this.mrKevin.gay(SearchBarWord.getText());
    }

    public void speechMW() {
        this.mrKevin.gay(SearchMW.getText());
    }

    @FXML
    private TextArea ggInput;
    @FXML
    private TextArea ggResult;
    @FXML
    private Button ggTran;

    public void goGoogle() {
        try {
            if (!this.ggInput.getText().trim().isEmpty())
                ggResult.setText(Api.googleTranslateApi(this.ggInput.getText()));
        } catch (Exception e) {
        }
    }

    public void enterGG(KeyEvent e) {
        if (e.getCode().toString().equals("ENTER")) {
            ggResult.setText(Api.googleTranslateApi(this.ggInput.getText()));
        }
    }
}