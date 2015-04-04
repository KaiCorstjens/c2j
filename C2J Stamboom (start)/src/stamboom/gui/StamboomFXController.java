/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import stamboom.controller.StamboomController;
import stamboom.domain.Administratie;
import stamboom.domain.Geslacht;
import stamboom.domain.Gezin;
import stamboom.domain.Persoon;
import stamboom.util.StringUtilities;

/**
 *
 * @author frankpeeters
 */
public class StamboomFXController extends StamboomController implements Initializable {

    //MENUs en TABs
    @FXML MenuBar menuBar;
    @FXML MenuItem miNew;
    @FXML MenuItem miOpen;
    @FXML MenuItem miSave;
    @FXML CheckMenuItem cmDatabase;
    @FXML MenuItem miClose;
    @FXML Tab tabPersoon;
    @FXML Tab tabGezin;
    @FXML Tab tabPersoonInvoer;
    @FXML Tab tabGezinInvoer;

    //PERSOON
    @FXML ComboBox cbPersonen;
    @FXML TextField tfPersoonNr;
    @FXML TextField tfVoornamen;
    @FXML TextField tfTussenvoegsel;
    @FXML TextField tfAchternaam;
    @FXML TextField tfGeslacht;
    @FXML TextField tfGebDatum;
    @FXML TextField tfGebPlaats;
    @FXML ComboBox cbOuderlijkGezin;
    @FXML ListView lvAlsOuderBetrokkenBij;
    @FXML Button btStamboom;

    //Gezin
    @FXML ComboBox cb_kiesGezin;
    @FXML TextField tfGezinsNr;
    @FXML TextField tfOuder1;
    @FXML TextField tfOuder2;
    @FXML TextField tfHuwelijk;
    @FXML TextField tfScheiding;
    @FXML TextField tfKinderen;

    //INVOER GEZIN
    @FXML ComboBox cbOuder1Invoer;
    @FXML ComboBox cbOuder2Invoer;
    @FXML TextField tfHuwelijkInvoer;
    @FXML TextField tfScheidingInvoer;
    @FXML Button btOKGezinInvoer;
    @FXML Button btCancelGezinInvoer;

    //New PERSOON
    @FXML TextField tfNewVoornamen;
    @FXML TextField tfNewTussenvoegsel;
    @FXML TextField tfNewAchternaam;
    @FXML ComboBox cbNewGeslacht;
    @FXML TextField tfNewGebDatum;
    @FXML TextField tfNewGebPlaats;
    @FXML ComboBox cbNewOuderlijkgezin;
    @FXML Button btOKPersoonInvoer;
    @FXML Button btCancelPersoonInvoer;
    //opgave 4
    private boolean withDatabase;
    Administratie admin = new Administratie();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initComboboxes();
        withDatabase = false;

    }

    private void initComboboxes() {
        //todo opgave 3 

        ObservableList<Persoon> personen = admin.getPersonen();
        this.cbPersonen.setItems(personen);
        ObservableList<Gezin> gezinnen = admin.getGezinnen();
        this.cb_kiesGezin.setItems(gezinnen);

        this.cbNewOuderlijkgezin.setItems(gezinnen);
        this.cbOuderlijkGezin.setItems(gezinnen);
        this.cbOuder1Invoer.setItems(personen);
        this.cbOuder2Invoer.setItems(personen);

    }

    public void selectPersoon(Event evt) {
        Persoon persoon = (Persoon) this.cbPersonen.getSelectionModel().getSelectedItem();
        showPersoon(persoon);

    }

    private void showPersoon(Persoon persoon) {
        if (persoon == null) {
            clearTabPersoon();
        } else {
            tfPersoonNr.setText(persoon.getNr() + "");
            tfVoornamen.setText(persoon.getVoornamen());
            tfTussenvoegsel.setText(persoon.getTussenvoegsel());
            tfAchternaam.setText(persoon.getAchternaam());
            tfGeslacht.setText(persoon.getGeslacht().toString());
            tfGebDatum.setText(StringUtilities.datumString(persoon.getGebDat()));
            tfGebPlaats.setText(persoon.getGebPlaats());
            if (persoon.getOuderlijkGezin() != null) {
                cbOuderlijkGezin.getSelectionModel().select(persoon.getOuderlijkGezin().toString());
            } else {
                cbOuderlijkGezin.getSelectionModel().select("Ouderlijk gezin onbekend");
            }
            this.lvAlsOuderBetrokkenBij.setItems(persoon.getAlsOuderBetrokkenIn());

        }
    }

    public void setOuders(Event evt) {
        if (tfPersoonNr.getText().isEmpty()) {
            return;
        }
        Gezin ouderlijkGezin = (Gezin) cbOuderlijkGezin.getSelectionModel().getSelectedItem();
        if (ouderlijkGezin == null) {
            return;
        }

        int nr = Integer.parseInt(tfPersoonNr.getText());
        Persoon p = admin.getPersoon(nr);
        if (admin.setOuders(p, ouderlijkGezin)) {
            showDialog("Success", ouderlijkGezin.toString()
                    + " is nu het ouderlijk gezin van " + p.getNaam());
        }

    }

    public void selectGezin(Event evt) {
        // todo opgave 3
        Gezin gezin = (Gezin) this.cb_kiesGezin.getSelectionModel().getSelectedItem();
        showGezin(gezin);

    }

    private void showGezin(Gezin gezin) {
        // todo opgave 3
        if (gezin == null) {
            clearTabGezin();
        } else {
            tfGezinsNr.setText(gezin.getNr() + "");
            tfOuder1.setText(gezin.getOuder1().getNaam());
            tfOuder2.setText(gezin.getOuder2().getNaam());
            if (gezin.isOngehuwd()) {
                this.tfHuwelijk.setText("");
                this.tfHuwelijk.setEditable(true);
            } else {
                this.tfHuwelijk.setText(StringUtilities.datumString(gezin.getHuwelijksdatum()));
                this.tfHuwelijk.setEditable(false);
            }
            if (gezin.getScheidingsdatum() != null) {
                tfScheiding.setText(StringUtilities.datumString(gezin.getScheidingsdatum()));
            } else {
                this.tfScheiding.setText("");
                this.tfScheiding.setEditable(!gezin.isOngehuwd());
            }
            for (Persoon kind : gezin.getKinderen()) {
                tfKinderen.setText(tfKinderen.getText() + kind.getNaam());
            }

        }

    }

    public void setHuwdatum(Event evt) {
        // todo opgave 3

        try {
            Calendar huwdatum = StringUtilities.datum(this.tfHuwelijk.getText());
            int nr = Integer.parseInt(this.tfGezinsNr.getText());
            Gezin gezin = admin.getGezin(nr);
            boolean gelukt = admin.setHuwelijk(gezin, huwdatum);
            if (!gelukt) {
                showDialog("Warning", "huwdatum is niet geaccepteerd");
            } else {
                showGezin(gezin);
            }
        } catch (IllegalArgumentException exc) {
            showDialog("Warning", exc.getMessage());
            this.tfHuwelijk.setPromptText("dd-mm-jjjj");
        }
    }

    public void setScheidingsdatum(Event evt) {
        // todo opgave 3
        try {
            Calendar scheidingsdatum = StringUtilities.datum(this.tfScheiding.getText());
            int nr = Integer.parseInt(this.tfGezinsNr.getText());
            Gezin gezin = admin.getGezin(nr);
            boolean gelukt = admin.setScheiding(gezin, scheidingsdatum);
            if (!gelukt) {
                showDialog("Warning", "scheidingsdatum is niet geaccepteerd");
            }
        } catch (IllegalArgumentException exc) {
            showDialog("Warning", exc.getMessage());
            this.tfScheiding.setPromptText("dd-mm-jjjj");
        }
    }

    public void cancelPersoonInvoer(Event evt) {
        // todo opgave 3
        clearTabPersoonInvoer();

    }

    public void okPersoonInvoer(Event evt) throws ParseException {
        // todo opgave 3

        Gezin gezin = (Gezin) this.cbNewOuderlijkgezin.getSelectionModel().getSelectedItem();

        if (tfNewGebDatum.getText().equals("")) {
            showDialog("Warning", "geboortedatum mag niet leeg zijn");
            return;
        }

        Calendar geboortedatum;
        try {

            geboortedatum = StringUtilities.datum(tfNewGebDatum.getText());
        } catch (IllegalArgumentException exc) {
            showDialog("Warning", "geboortedatum :" + exc.getMessage());
            return;
        }

        String[] voornamen = tfNewVoornamen.getText().split(" ");
        if (voornamen.length == 0) {
            showDialog("Warning", "voornaam mag niet leeg zijn");
            return;
        }

        for (String s : voornamen) {
            if (s.matches(".*\\d+.*")) {
                showDialog("Warning", "Voornaam mag  geen nummers bevatten");
                return;
            }
        }

        String anaam = this.tfNewAchternaam.getText().trim();
        if (anaam.isEmpty()) {
            showDialog("Warning", "achternaam mag niet leeg zijn");
            return;
        }

        String gebplaats = this.tfNewGebDatum.getText().trim();
        if (gebplaats.isEmpty()) {
            showDialog("Warning", "geboorteplaats mag niet leeg zijn");
            return;
        }

        if ("Man".equals((String) cbNewGeslacht.getSelectionModel().getSelectedItem())) {
            Persoon p = admin.addPersoon(Geslacht.MAN, voornamen, tfNewAchternaam.getText(), tfNewTussenvoegsel.getText(), geboortedatum, tfNewGebPlaats.getText(), gezin);
        } else if (("Vrouw").equals((String) cbNewGeslacht.getSelectionModel().getSelectedItem())) {
            Persoon p = admin.addPersoon(Geslacht.VROUW, voornamen, tfNewAchternaam.getText(), tfNewTussenvoegsel.getText(), geboortedatum, tfNewGebPlaats.getText(), gezin);

        } else {
            showDialog("Warning", "Vul geslacht in");
            return;
        }
        clearTabPersoonInvoer();

    }

    public void okGezinInvoer(Event evt) {
        Persoon ouder1 = (Persoon) cbOuder1Invoer.getSelectionModel().getSelectedItem();
        if (ouder1 == null) {
            showDialog("Warning", "eerste ouder is niet ingevoerd");
            return;
        }
        Persoon ouder2 = (Persoon) cbOuder2Invoer.getSelectionModel().getSelectedItem();
        Calendar huwdatum;
        try {
            huwdatum = StringUtilities.datum(tfHuwelijkInvoer.getText());
        } catch (IllegalArgumentException exc) {
            showDialog("Warning", "huwelijksdatum :" + exc.getMessage());
            return;
        }
        Gezin g;
        if (huwdatum != null) {
            g = admin.addHuwelijk(ouder1, ouder2, huwdatum);
            if (g == null) {
                showDialog("Warning", "Invoer huwelijk is niet geaccepteerd");
            } else {
                Calendar scheidingsdatum;
                try {
                    scheidingsdatum = StringUtilities.datum(tfScheidingInvoer.getText());
                    if (scheidingsdatum != null) {
                        admin.setScheiding(g, scheidingsdatum);
                    }
                } catch (IllegalArgumentException exc) {
                    showDialog("Warning", "scheidingsdatum :" + exc.getMessage());
                }
            }
        } else {
            g = admin.addOngehuwdGezin(ouder1, ouder2);
            if (g == null) {
                showDialog("Warning", "Invoer ongehuwd gezin is niet geaccepteerd");
            }
        }

        clearTabGezinInvoer();
    }

    public void cancelGezinInvoer(Event evt) {
        clearTabGezinInvoer();
    }

    public void showStamboom(Event evt) {
        // todo opgave 3
        Persoon persoon = (Persoon) this.cbPersonen.getSelectionModel().getSelectedItem();
        if (persoon == null) {
            return;
        }
        showDialog("stamboom van " + persoon.getNaam(), persoon.stamboomAlsString());
    }

    public void createEmptyStamboom(Event evt) {
        this.clearAdministratie();
        clearTabs();
        initComboboxes();
    }

    public void openStamboom(Event evt) {
        // todo opgave 3
     
    }

    public void saveStamboom(Event evt) {
        // todo opgave 3

    }

    public void closeApplication(Event evt) {
        saveStamboom(evt);
        getStage().close();
    }

    public void configureStorage(Event evt) {
        withDatabase = cmDatabase.isSelected();
    }

    public void selectTab(Event evt) {
        Object source = evt.getSource();
        if (source == tabPersoon) {
            clearTabPersoon();
        } else if (source == tabGezin) {
            clearTabGezin();
        } else if (source == tabPersoonInvoer) {
            clearTabPersoonInvoer();
        } else if (source == tabGezinInvoer) {
            clearTabGezinInvoer();
        }
    }

    private void clearTabs() {
        clearTabPersoon();
        clearTabPersoonInvoer();
        clearTabGezin();
        clearTabGezinInvoer();
    }

    private void clearTabPersoonInvoer() {
        //todo opgave 3
        cbNewGeslacht.getSelectionModel().clearSelection();
        cbNewOuderlijkgezin.getSelectionModel().clearSelection();
        tfNewAchternaam.clear();
        tfNewGebDatum.clear();
        tfNewGebPlaats.clear();
        tfNewTussenvoegsel.clear();
        tfNewVoornamen.clear();

    }

    private void clearTabGezinInvoer() {
        //todo opgave 3
        tfHuwelijkInvoer.clear();
        tfScheidingInvoer.clear();
        cbOuder1Invoer.getSelectionModel().clearSelection();
        cbOuder2Invoer.getSelectionModel().clearSelection();
    }

    private void clearTabPersoon() {
        cbPersonen.getSelectionModel().clearSelection();
        tfPersoonNr.clear();
        tfVoornamen.clear();
        tfTussenvoegsel.clear();
        tfAchternaam.clear();
        tfGeslacht.clear();
        tfGebDatum.clear();
        tfGebPlaats.clear();
        cbOuderlijkGezin.getSelectionModel().clearSelection();
        lvAlsOuderBetrokkenBij.setItems(FXCollections.emptyObservableList());
    }

    private void clearTabGezin() {
        // todo opgave 3
        cb_kiesGezin.getSelectionModel().clearSelection();
        tfGezinsNr.clear();
        tfOuder1.clear();
        tfOuder2.clear();
        tfHuwelijk.clear();
        tfScheiding.clear();
        tfKinderen.clear();
    }

    private void showDialog(String type, String message) {
        Stage myDialog = new Dialog(getStage(), type, message);
        myDialog.show();
    }

    private Stage getStage() {
        return (Stage) menuBar.getScene().getWindow();
    }

}
