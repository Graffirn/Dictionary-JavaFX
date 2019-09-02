package sqlite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import word.Word;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class DataBase {
    final String url = "jdbc:sqlite:" + "src\\data\\dictionary.db";
    final String select = "SELECT en, vn, sound FROM dictionary WHERE en = ?;";
    final String search = "SELECT en, vn, sound FROM dictionary WHERE en LIKE ?;";
    final String searchMW = "SELECT en FROM myword WHERE en LIKE ?;";
    final String selectAll = "SELECT en, vn, sound FROM dictionary;";
    final String selectAllRecent = "SELECT en FROM recent;";
    final String selectAllMW = "SELECT en FROM myword;";
    final String insert = "INSERT INTO dictionary (en, vn, sound) VALUES (?, ?, ?);";
    final String insertRecently = "INSERT INTO recent (en) VALUES (\"";//?);";
    final String insertMW = "INSERT INTO myword (en) VALUES (\"";
    final String dlt = "DELETE FROM dictionary WHERE en = ?;";
    final String dltRecent = "DELETE FROM recent WHERE en = ?;";
    final String dltMW = "DELETE FROM myword WHERE en = ?;";
    final String update = "UPDATE dictionary SET vn = ?, sound = ? WHERE en = ?;";

    private Connection connection;

    public DataBase() {
        this.getConnection(url);
    }

    Connection getConnection(String url) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.connection = null;

        try {
            this.connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /*
     * Lay nghia tu
     */
    public Word getWord(String eng) {
        Word word = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(select);
            preparedStatement.setString(1, eng);
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            word = new Word(result.getString("en"), result.getString("vn"), result.getString("sound"));
            return word;
        } catch (SQLException e) {
            return null;
        }
    }

    /*
     * Lay danh sach cac tu tieng anh
     */
    public ArrayList<String> getEng() {
        ArrayList<String> listEn = new ArrayList<String>();
        try {
            Statement statement = this.connection.createStatement();
            ResultSet result = statement.executeQuery(selectAll);
            while (result.next()) {
                listEn.add(result.getString("en").toLowerCase());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listEn;
    }

    /*
     * Lay list tieng anh dang observable
     */
    public ObservableList<String> getListView() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            Statement statement = this.connection.createStatement();
            ResultSet result = statement.executeQuery(selectAll);
            while (result.next()) {
                list.add(result.getString("en").toLowerCase());
            }
            Collections.sort(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*
     * Tim tu bat dau bang word trong tu dien
     */
    public ObservableList<String> searchWord(String word) {
        ObservableList<String> listWord = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(search);
            preparedStatement.setString(1, word + "%");
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                listWord.add(result.getString("en").toLowerCase());
            }
            Collections.sort(listWord);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listWord;
    }

    /*
     * Tim tu bat dau bang word trong myword
     */
    public ObservableList<String> searchMW(String word) {
        ObservableList<String> myWord = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(searchMW);
            preparedStatement.setString(1, word + "%");
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                myWord.add(result.getString("en").toLowerCase());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myWord;
    }

    /*
     * Lay list hien o muc recent
     */
    public ObservableList<String> recent() {
        ObservableList<String> listRecent = FXCollections.observableArrayList();
        try {
            Statement statement = this.connection.createStatement();
            ResultSet result = statement.executeQuery(selectAllRecent);
            while (result.next()) {
                listRecent.add(result.getString("en").toLowerCase());
            }
            Collections.reverse(listRecent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listRecent;
    }

    /*
     * Lay list hien o muc myword
     */
    public ObservableList<String> myword() {
        ObservableList<String> myword = FXCollections.observableArrayList();
        try {
            Statement statement = this.connection.createStatement();
            ResultSet result = statement.executeQuery(selectAllMW);
            while (result.next()) {
                myword.add(result.getString("en").toLowerCase());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myword;
    }

    /*
     * Insert tu moi vao dictionary
     */
    public void insertWord(Word word) {
        try {
//            Statement statement = this.connection.createStatement();
//            statement.executeUpdate("INSERT INTO dictionary (en, vn, sound) VALUES (\"" + word.getWord_target() +"\", \"" + word.getWord_explain() + "\", \""+ word.getSound() +"\");");
            PreparedStatement preparedStatement = this.connection.prepareStatement(insert);
            preparedStatement.setString(1, word.getWord_target());
            preparedStatement.setString(2, word.getWord_explain());
            preparedStatement.setString(3, word.getSound());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            return;
        }
    }

    /*
     * Insert tu moi vao recent
     */
    public void insertRecent(String str) {
        try {
//            PreparedStatement preparedStatement = this.connection.prepareStatement(insertRecently);
//            preparedStatement.setString(1, str);
//            preparedStatement.executeUpdate();
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(insertRecently + str + "\");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Xoa tu khoi myword
     */
    public void deleteMW(String str) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(dltMW);
            preparedStatement.setString(1, str);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Insert tu moi vao myword
     */
    public void insertMyWord(String str) {
        if (str.equals(null)) {
            return;
        } else {
            try {
                this.deleteMW(str);
                this.deleteMW("");
                Statement statement = this.connection.createStatement();
                statement.executeUpdate(insertMW + str + "\");");
            } catch (SQLException e) {
                return;
            }
        }
    }

    /*
     * Xoa tu khoi tu dien
     */
    public void delete(String str) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(dlt);
            preparedStatement.setString(1, str);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Xoa tu khoi recent
     */
    public void deleteRecent(String str) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(dltRecent);
            preparedStatement.setString(1, str);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Update dictionary
     */
    public void updateWord(String eng, String vietnam, String sound) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(update);
            preparedStatement.setString(1, vietnam);
            preparedStatement.setString(2, sound);
            preparedStatement.setString(3, eng);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            return;
        }
    }
}
