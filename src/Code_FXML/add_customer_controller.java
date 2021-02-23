package Code_FXML;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import utils.ConnectionUtil;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class add_customer_controller implements Initializable {
    @FXML
    private ComboBox cbcity;
    ObservableList<String> datacity;
    Integer caretpos;
    boolean moveCaretToPos=false;
    ResultSet rs=null;
    PreparedStatement ps=null;
    Connection con=(Connection) ConnectionUtil.conDB();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datacity=FXCollections.observableArrayList();
        readdatacity();
        cbcity.setItems(datacity);
        new AutoCompleteComboBoxListener<>(cbcity);
    }
    public void readdatacity(){
        String sqlcity="SELECT * FROM country";
        try {
            rs = con.createStatement().executeQuery(sqlcity);
            while(rs.next()){
                datacity.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
