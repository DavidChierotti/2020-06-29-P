/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Month> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	if(this.model.getGrafo()==null) {
    		txtResult.setText("Creare Grafo!");
    	}
    	else {
    		txtResult.appendText("\n"+"\n"+"\n"+"Coppie con connessione massima:"+"\n");
    		for(Adiacenza a:model.massimo()) {
    			txtResult.appendText(a.toString()+"\n");
    		}
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	int x;
    	try {
    		x = Integer.parseInt(txtMinuti.getText());
    	} catch (NumberFormatException e) {
    		txtResult.setText("Devi inserire un codice numerico!");
    		return;
    	}
    	int m=cmbMese.getValue().get(ChronoField.MONTH_OF_YEAR);
    	model.creaGrafo(m,x);
    	txtResult.setText("GRAFO CREATO!"+"\n"+"VERTICI: "+this.model.nVert()+"  ARCHI: "+this.model.nArch());
    	List<Match> partite=new ArrayList<Match>(model.getGrafo().vertexSet());
    	cmbM1.getItems().clear();
    	cmbM2.getItems().clear();
    	cmbM1.getItems().addAll(partite);
    	cmbM2.getItems().addAll(partite);
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	if(cmbM1.getValue()==null||cmbM2.getValue()==null) {
    		txtResult.setText("Selezionare la partita di partenza e di arrivo");
    	}
    	else {
    		List<Match> cammino=new ArrayList<>(this.model.cercaCammino(cmbM1.getValue(),cmbM2.getValue()));
    		double pesotot=model.getPesototale();
    		txtResult.setText("CAMMINO TROVATO:"+"\n");
    		for(Match m:cammino) {
    			txtResult.appendText("\n"+m.toString());
    		}
    		txtResult.appendText("\n"+"\n"+"PESO: "+pesotot);
    		
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	for(int i=1;i<13;i++) {
    		Month m=Month.of(i);
    		cmbMese.getItems().add(m);
    	}
  
    }
    
    
}
