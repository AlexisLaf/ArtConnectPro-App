package com.project.artconnect.ui;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.util.ServiceProvider;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class ArtistController {
    @FXML private TextField searchField;
    @FXML private ComboBox<Discipline> disciplineFilter;
    @FXML private TableView<Artist> artistTable;
    @FXML private TableColumn<Artist, String> nameColumn;
    @FXML private TableColumn<Artist, String> cityColumn;
    @FXML private TableColumn<Artist, String> emailColumn;
    @FXML private TableColumn<Artist, Integer> yearColumn;

    private final ArtistService artistService = ServiceProvider.getArtistService();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("birthYear"));

        disciplineFilter.setItems(FXCollections.observableArrayList(artistService.getAllDisciplines()));
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        Discipline d = disciplineFilter.getValue();
        String dName = d != null ? d.getName() : null;
        artistTable.setItems(FXCollections.observableArrayList(artistService.searchArtists(query, dName, null)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        disciplineFilter.setValue(null);
        refreshTable();
    }

    @FXML
    private void handleAddArtist() {
        Optional<Artist> result = showArtistDialog(null);
        result.ifPresent(artist -> {
            artistService.createArtist(artist);
            refreshTable();
        });
    }

    @FXML
    private void handleEditArtist() {
        Artist selected = artistTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Please select an artist to edit.");
            return;
        }

        Optional<Artist> result = showArtistDialog(selected);
        result.ifPresent(artist -> {
            artistService.updateArtist(artist);
            refreshTable();
        });
    }

    @FXML
    private void handleDeleteArtist() {
        Artist selected = artistTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Please select an artist to delete.");
            return;
        }

        artistService.deleteArtist(selected.getName());
        refreshTable();
    }

    private Optional<Artist> showArtistDialog(Artist existingArtist) {
        Dialog<Artist> dialog = new Dialog<>();
        dialog.setTitle(existingArtist == null ? "Add Artist" : "Edit Artist");

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        TextField nameField = new TextField();
        TextField cityField = new TextField();
        TextField emailField = new TextField();
        TextField birthYearField = new TextField();
        TextField bioField = new TextField();
        TextField phoneField = new TextField();

        if (existingArtist != null) {
            nameField.setText(existingArtist.getName());
            nameField.setDisable(true);
            cityField.setText(existingArtist.getCity());
            emailField.setText(existingArtist.getContactEmail());
            birthYearField.setText(existingArtist.getBirthYear() != null ? existingArtist.getBirthYear().toString() : "");
            bioField.setText(existingArtist.getBio());
            phoneField.setText(existingArtist.getPhone());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("City:"), 0, 1);
        grid.add(cityField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Birth Year:"), 0, 3);
        grid.add(birthYearField, 1, 3);
        grid.add(new Label("Bio:"), 0, 4);
        grid.add(bioField, 1, 4);
        grid.add(new Label("Phone:"), 0, 5);
        grid.add(phoneField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == saveButton) {
                Artist artist = existingArtist == null ? new Artist() : existingArtist;

                artist.setName(nameField.getText());
                artist.setCity(cityField.getText());
                artist.setContactEmail(emailField.getText());
                artist.setBio(bioField.getText());
                artist.setPhone(phoneField.getText());
                artist.setActive(true);

                if (birthYearField.getText() == null || birthYearField.getText().isBlank()) {
                    artist.setBirthYear(null);
                } else {
                    artist.setBirthYear(Integer.parseInt(birthYearField.getText()));
                }

                return artist;
            }

            return null;
        });

        return dialog.showAndWait();
    }

    private void refreshTable() {
        artistTable.setItems(FXCollections.observableArrayList(artistService.getAllArtists()));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}