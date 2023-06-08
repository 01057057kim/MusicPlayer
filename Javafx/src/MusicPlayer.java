import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioEqualizer;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.image.Image;

// 01057057
// 01057059
public class MusicPlayer extends Application {
    private static final double WINDOW_WIDTH = 750;
    private static final double WINDOW_HEIGHT = 450;
    private double x = 0;
    private double y = 0;
    private MediaPlayer mediaPlayer;
    private Label timeLabel = new Label("00:00");
    private Slider timeSlider = new Slider();
    private Slider volumeSlider = new Slider();
    private ToggleButton playButton = new ToggleButton();
    private ToggleButton repeatButton = new ToggleButton();
    private ListView<String> songList = new ListView<>();
    private List<File> fileList = new ArrayList<>();
    private List<File> filtered = new ArrayList<>();
    private List<Slider> bandSliders = new ArrayList<>();
    private List<Double> saved = new ArrayList<>();
    private Scene equalizerScene;
    private boolean equalizerOpen = false;
    private static final List<Double> EQUALIZER_BAND = List.of(
            60.0, 170.0, 310.0, 600.0, 1000.0, 3000.0, 6000.0, 12000.0, 14000.0, 16000.0);
    private int currentSongIndex = 0;
    private boolean repeatEnable = false;
    private Label currentSongLabel = new Label("");
    String imagePath = "image.png";
    String gifPath = "Piring.gif";

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        // right
        AnchorPane rightSide = new AnchorPane();
        VBox controlBox = new VBox();
        controlBox.getChildren().add(songList);
        controlBox.setLayoutY(34.0);
        controlBox.setPrefHeight(325.0);
        controlBox.setPrefWidth(230.0);
        songList.setStyle("-fx-control-inner-background: #FFF6BD;");
        songList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Image playImage = new Image(new File("play.png").toURI().toString());
        Image pauseImage = new Image(new File("pause.png").toURI().toString());
        ImageView playImageView = new ImageView(playImage);
        ImageView pauseImageView = new ImageView(pauseImage);
        playImageView.setFitWidth(30);
        playImageView.setPreserveRatio(true);
        pauseImageView.setFitWidth(30);
        pauseImageView.setPreserveRatio(true);
        playButton.setGraphic(playImageView);
        playButton.setLayoutX(129.0);
        playButton.setLayoutY(334.0);
        playButton.setStyle(
                "-fx-background-color: transparent; " + "-fx-border-color: transparent;");
        playButton.setOnMouseEntered(e -> {
            playButton.setStyle("-fx-background-color: #7081aa;");
        });
        playButton.setOnMouseExited(e -> {
            playButton.setStyle("-fx-background-color: transparent;");
        });
        playButton.setOnMousePressed(e -> {
            playButton.setStyle("-fx-background-color: #3e4fae;");
        });
        playButton.setOnMouseReleased(e -> {
            playButton.setStyle("-fx-background-color: transparent;");
        });

        Image openFileImage = new Image(new File("file.png").toURI().toString()); // 13
        ImageView openFileView = new ImageView(openFileImage); // 13
        openFileView.setFitWidth(30); // 13
        openFileView.setPreserveRatio(true); // 13
        Button openFileButton = new Button(); // 13
        openFileButton.setGraphic(openFileView); // 13
        openFileButton.setLayoutY(362.0); // 13
        openFileButton.setStyle(
                "-fx-background-color: transparent; " + "-fx-border-color: transparent;");
        openFileButton.setOnMouseEntered(e -> {
            openFileButton.setStyle("-fx-background-color: #7081aa;");
        });
        openFileButton.setOnMouseExited(e -> {
            openFileButton.setStyle("-fx-background-color: transparent;");
        });
        openFileButton.setOnMousePressed(e -> {
            openFileButton.setStyle("-fx-background-color: #3e4fae;");
        });
        openFileButton.setOnMouseReleased(e -> {
            openFileButton.setStyle("-fx-background-color: transparent;");
        });

        Image openFolderImage = new Image(new File("folder.png").toURI().toString()); // 12
        ImageView openFolderView = new ImageView(openFolderImage); // 12
        openFolderView.setFitWidth(30); // 12
        openFolderView.setPreserveRatio(true); // 12
        Button openFolderButton = new Button(); // 12
        openFolderButton.setGraphic(openFolderView); // 12
        openFolderButton.setLayoutX(60.0); // 12
        openFolderButton.setLayoutY(364.0); // 12
        openFolderButton.setStyle(
                "-fx-background-color: transparent; " + "-fx-border-color: transparent;");
        openFolderButton.setOnMouseEntered(e -> {
            openFolderButton.setStyle("-fx-background-color: #7081aa;");
        });
        openFolderButton.setOnMouseExited(e -> {
            openFolderButton.setStyle("-fx-background-color: transparent;");
        });
        openFolderButton.setOnMousePressed(e -> {
            openFolderButton.setStyle("-fx-background-color: #3e4fae;");
        });
        openFolderButton.setOnMouseReleased(e -> {
            openFolderButton.setStyle("-fx-background-color: transparent;");
        });

        Image deleteImage = new Image(new File("delete.png").toURI().toString()); // 11
        ImageView deleteView = new ImageView(deleteImage); // 11
        deleteView.setFitWidth(30); // 11
        deleteView.setPreserveRatio(true); // 11
        Button deleteButton = new Button(); // 11
        deleteButton.setGraphic(deleteView); // 11
        deleteButton.setLayoutX(120.0); // 11
        deleteButton.setLayoutY(362.0); // 11
        deleteButton.setStyle(
                "-fx-background-color: transparent; " + "-fx-border-color: transparent;");
        deleteButton.setOnMouseEntered(e -> {
            deleteButton.setStyle("-fx-background-color: #7081aa;");
        });
        deleteButton.setOnMouseExited(e -> {
            deleteButton.setStyle("-fx-background-color: transparent;");
        });
        deleteButton.setOnMousePressed(e -> {
            deleteButton.setStyle("-fx-background-color: #3e4fae;");
        });
        deleteButton.setOnMouseReleased(e -> {
            deleteButton.setStyle("-fx-background-color: transparent;");
        });

        Image clearAllImage = new Image(new File("clear.png").toURI().toString()); // 10
        ImageView clearAllView = new ImageView(clearAllImage); // 10
        clearAllView.setFitWidth(30); // 10
        clearAllView.setPreserveRatio(true); // 10
        Button clearAllButton = new Button(); // 10
        clearAllButton.setGraphic(clearAllView); // 10
        clearAllButton.setLayoutX(180.0); // 10
        clearAllButton.setLayoutY(362.0); // 10
        clearAllButton.setStyle(
                "-fx-background-color: transparent; " + "-fx-border-color: transparent;");
        clearAllButton.setOnMouseEntered(e -> {
            clearAllButton.setStyle("-fx-background-color: #7081aa;");
        });
        clearAllButton.setOnMouseExited(e -> {
            clearAllButton.setStyle("-fx-background-color: transparent;");
        });
        clearAllButton.setOnMousePressed(e -> {
            clearAllButton.setStyle("-fx-background-color: #3e4fae;");
        });
        clearAllButton.setOnMouseReleased(e -> {
            clearAllButton.setStyle("-fx-background-color: transparent;");
        });

        Text libraryText = new Text("Library :");
        libraryText.setLayoutY(25.0);
        libraryText.setWrappingWidth(245);
        libraryText.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search...");
        searchBar.setLayoutY(8.0);
        searchBar.setLayoutX(82.0);

        rightSide.getChildren().addAll(controlBox, openFileButton, openFolderButton, deleteButton, clearAllButton,
                libraryText, searchBar);
        root.setRight(rightSide);

        // Center
        AnchorPane center = new AnchorPane();
        Image previousImage = new Image(new File("previous.png").toURI().toString()); // 9
        ImageView previousImageView = new ImageView(previousImage); // 9
        previousImageView.setFitWidth(30); // 9
        previousImageView.setPreserveRatio(true); // 9
        Button previousButton = new Button(); // 9
        previousButton.setGraphic(previousImageView); // 9
        previousButton.setLayoutX(136.0); // 9
        previousButton.setLayoutY(332.0); // 9
        previousButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: transparent;");
        previousButton.setOnMouseEntered(e -> {
            previousButton.setStyle("-fx-background-color: #7081aa;");
        });
        previousButton.setOnMouseExited(e -> {
            previousButton.setStyle("-fx-background-color: transparent;");
        });
        previousButton.setOnMousePressed(e -> {
            previousButton.setStyle("-fx-background-color: #3e4fae;");
        });
        previousButton.setOnMouseReleased(e -> {
            previousButton.setStyle("-fx-background-color: transparent;");
        });

        Image nextImage = new Image(new File("next.png").toURI().toString()); // 7
        ImageView nextImageView = new ImageView(nextImage); // 7
        nextImageView.setFitWidth(30); // 7
        nextImageView.setPreserveRatio(true); // 7
        Button nextButton = new Button(); // 7
        nextButton.setGraphic(nextImageView); // 7
        nextButton.setLayoutX(260.0); // 7
        nextButton.setLayoutY(332.0); // 7
        nextButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: transparent;");
        nextButton.setOnMouseEntered(e -> {
            nextButton.setStyle("-fx-background-color: #7081aa;");
        });
        nextButton.setOnMouseExited(e -> {
            nextButton.setStyle("-fx-background-color: transparent;");
        });
        nextButton.setOnMousePressed(e -> {
            nextButton.setStyle("-fx-background-color: #3e4fae;");
        });
        nextButton.setOnMouseReleased(e -> {
            nextButton.setStyle("-fx-background-color: transparent;");
        });

        timeSlider.setLayoutX(33.0);
        timeSlider.setLayoutY(307.0);
        timeSlider.setPrefHeight(14.0);
        timeSlider.setPrefWidth(275.0);

        volumeSlider.setLayoutX(278.0);
        volumeSlider.setLayoutY(340.0);
        volumeSlider.setPrefHeight(14.0);
        volumeSlider.setPrefWidth(61.0);

        ImageView albumCover = new ImageView(); // 1
        albumCover.setFitHeight(250.0); // 1
        albumCover.setFitWidth(250.0); // 1
        albumCover.setLayoutX(100.0); // 1
        albumCover.setLayoutY(22.0); // 1
        albumCover.setPreserveRatio(true); // 1

        Image image = new Image(new File(imagePath).toURI().toString());
        albumCover.setImage(image);

        Image volumeImage = new Image(new File("volume.png").toURI().toString()); // 6
        ImageView volumeView = new ImageView(volumeImage); // 6
        volumeView.setFitWidth(13); // 6
        volumeView.setPreserveRatio(true); // 6
        Button volumeLabel = new Button(); // 6
        volumeLabel.setGraphic(volumeView); // 6
        volumeLabel.setLayoutX(346.0); // 6
        volumeLabel.setLayoutY(338.0); // 6
        volumeLabel.setStyle("-fx-background-color: transparent; " + "-fx-border-color: transparent;"); // 6

        currentSongLabel.setLayoutX(32.0); // 4
        currentSongLabel.setLayoutY(290.0); // 4

        timeLabel.setLayoutX(430.0); // 2
        timeLabel.setLayoutY(308.0); // 2

        HBox volumeBox = new HBox(volumeSlider); // 5
        volumeSlider.setLayoutX(370.0); // 5
        volumeSlider.setLayoutY(337.0); // 5
        volumeSlider.setPrefHeight(30.0); // 5
        volumeSlider.setPrefWidth(90.0); // 5

        HBox timeBox = new HBox(timeSlider); // 3
        timeSlider.setLayoutX(28.0); // 3
        timeSlider.setLayoutY(297.0); // 3
        timeSlider.setPrefHeight(40.0); // 3
        timeSlider.setPrefWidth(400.0); // 3

        Image shuffleImage = new Image(new File("shuffle.png").toURI().toString());
        ImageView shuffleView = new ImageView(shuffleImage);
        shuffleView.setFitWidth(15);
        shuffleView.setPreserveRatio(true);
        Button shuffleButton = new Button();
        shuffleButton.setGraphic(shuffleView);
        shuffleButton.setLayoutX(100.0);
        shuffleButton.setLayoutY(338.0);
        shuffleButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: transparent;");
        shuffleButton.setOnMouseEntered(e -> {
            shuffleButton.setStyle("-fx-background-color: #7081aa;");
        });
        shuffleButton.setOnMouseExited(e -> {
            shuffleButton.setStyle("-fx-background-color: transparent;");
        });
        shuffleButton.setOnMousePressed(e -> {
            shuffleButton.setStyle("-fx-background-color: #3e4fae;");
        });
        shuffleButton.setOnMouseReleased(e -> {
            shuffleButton.setStyle("-fx-background-color: transparent;");
        });

        Image equalizerImage = new Image(new File("equalizer.png").toURI().toString());
        ImageView equalizerView = new ImageView(equalizerImage);
        equalizerView.setFitWidth(20);
        equalizerView.setPreserveRatio(true);
        Button equalizerButton = new Button("");
        equalizerButton.setGraphic(equalizerView);
        equalizerButton.setLayoutX(30.0);
        equalizerButton.setLayoutY(334.0);
        equalizerButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: transparent;");
        equalizerButton.setOnMouseEntered(e -> {
            equalizerButton.setStyle("-fx-background-color: #7081aa;");
        });
        equalizerButton.setOnMouseExited(e -> {
            equalizerButton.setStyle("-fx-background-color: transparent;");
        });
        equalizerButton.setOnMousePressed(e -> {
            equalizerButton.setStyle("-fx-background-color: #3e4fae;");
        });
        equalizerButton.setOnMouseReleased(e -> {
            equalizerButton.setStyle("-fx-background-color: transparent;");
        });

        Image repeatImage = new Image(new File("repeat.png").toURI().toString());
        Image repeatpauseImage = new Image(new File("repeatpause.png").toURI().toString());
        ImageView repeatView = new ImageView(repeatImage);
        ImageView repeatpauseView = new ImageView(repeatpauseImage);
        repeatView.setFitWidth(15);
        repeatView.setPreserveRatio(true);
        repeatpauseView.setFitWidth(15);
        repeatpauseView.setPreserveRatio(true);
        repeatButton.setGraphic(repeatView);
        repeatButton.setLayoutX(310.0);
        repeatButton.setLayoutY(339.0);
        repeatButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: transparent;");
        repeatButton.setOnMouseEntered(e -> {
            repeatButton.setStyle("-fx-background-color: #7081aa;");
        });
        repeatButton.setOnMouseExited(e -> {
            repeatButton.setStyle("-fx-background-color: transparent;");
        });
        repeatButton.setOnMousePressed(e -> {
            repeatButton.setStyle("-fx-background-color: #3e4fae;");
        });
        repeatButton.setOnMouseReleased(e -> {
            repeatButton.setStyle("-fx-background-color: transparent;");
        });
        playButton.setLayoutX(200.0); // 8
        playButton.setLayoutY(332.0); // 8

        center.getChildren().addAll(previousButton, nextButton, timeSlider, volumeSlider, albumCover, volumeLabel,
                currentSongLabel, timeLabel, volumeBox, timeBox, playButton, repeatButton, shuffleButton,
                equalizerButton);
        root.setCenter(center);

        playButton.setDisable(true);
        repeatButton.setDisable(true);
        shuffleButton.setDisable(true);
        timeSlider.setDisable(true);
        searchBar.setDisable(true);
        equalizerButton.setDisable(true);
        volumeSlider.setValue(50);
        previousButton.setDisable(true);
        nextButton.setDisable(true);

        // primaryStage.setTitle("Music App - 057,059");
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

        Rectangle rect = new Rectangle(WINDOW_WIDTH, WINDOW_HEIGHT);
        rect.setArcHeight(30.0);
        rect.setArcWidth(30.0);
        root.setClip(rect);

        HBox header = new HBox();
        header.setStyle("-fx-background-color: #86C8BC;"); // HEADER
        header.setPrefHeight(40);
        header.setAlignment(Pos.CENTER_RIGHT);

        Image closeImage = new Image(new File("close.png").toURI().toString());
        ImageView closeView = new ImageView(closeImage);
        closeView.setFitWidth(30);
        closeView.setPreserveRatio(true);
        Button closeButton = new Button();
        closeButton.setGraphic(closeView);
        closeButton.setStyle("-fx-background-color: transparent; " + "-fx-border-color: transparent;");
        closeButton.setOnAction(event -> primaryStage.close());
        header.getChildren().add(closeButton);
        root.setTop(header);

        Label titleLabel = new Label("Music Player ");
        titleLabel.setStyle("-fx-text-fill: white;");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        HBox headerContent = new HBox(titleLabel, closeButton);
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setSpacing(590);
        header.getChildren().add(headerContent);
        root.setTop(header);

        header.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        header.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });

        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });

        root.setStyle("-fx-background-color: #CEEDC7;"); // BACKGROUND
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        openFileButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
            if (selectedFiles != null) {
                fileList.addAll(selectedFiles);
                if (!filtered.isEmpty()) {
                    filterSongList(searchBar.getText());
                } else {
                    updateSongList();
                }
                playButton.setDisable(false);
                nextButton.setDisable(false);
                repeatButton.setDisable(false);
                shuffleButton.setDisable(false);
                searchBar.setDisable(false);
                previousButton.setDisable(false);
                equalizerButton.setDisable(false);
                currentSongLabel.setText("Now Playing: None");
            }
        });

        openFolderButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File folder = directoryChooser.showDialog(primaryStage);
            if (folder != null) {
                List<File> folderFiles = Arrays.asList(folder.listFiles((dir, name) -> name.endsWith(".mp3")));
                fileList.addAll(folderFiles);
                if (!filtered.isEmpty()) {
                    filterSongList(searchBar.getText());
                } else {
                    updateSongList();
                }
                playButton.setDisable(false);
                nextButton.setDisable(false);
                repeatButton.setDisable(false);
                shuffleButton.setDisable(false);
                searchBar.setDisable(false);
                equalizerButton.setDisable(false);
                previousButton.setDisable(false);
                currentSongLabel.setText("Now Playing: None");
            }
        });

        songList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedSongName = songList.getSelectionModel().getSelectedItem();
                File selectedSongFile = fileList.stream().filter(file -> file.getName().equals(selectedSongName))
                        .findFirst().orElse(null);
                if (selectedSongFile != null) {
                    currentSongIndex = fileList.indexOf(selectedSongFile);
                    playSong(selectedSongFile);
                    Image gifImage = new Image(new File(gifPath).toURI().toString());
                    albumCover.setImage(gifImage);
                    playButton.setGraphic(pauseImageView);
                }
            }
        });

        playButton.setOnAction(event -> {
            List<File> playList;
            if (filtered.isEmpty()) {
                playList = fileList;
            } else {
                playList = filtered;
            }
            if (mediaPlayer == null && !playList.isEmpty()) {
                currentSongIndex = 0;
                playSong(playList.get(currentSongIndex));
                Image gifImage = new Image(new File(gifPath).toURI().toString());
                albumCover.setImage(gifImage);
                playButton.setGraphic(pauseImageView);
            } else if (mediaPlayer != null) {
                if (mediaPlayer.getStatus() == Status.PLAYING) {
                    mediaPlayer.pause();
                    albumCover.setImage(image);
                    playButton.setGraphic(playImageView);
                } else {
                    mediaPlayer.play();
                    Image gifImage = new Image(new File(gifPath).toURI().toString());
                    albumCover.setImage(gifImage);
                    playButton.setGraphic(pauseImageView);
                }
            }
        });

        previousButton.setOnAction(event -> {
            List<File> playList;
            if (filtered.isEmpty()) {
                playList = fileList;
            } else {
                playList = filtered;
            }
            currentSongIndex = (currentSongIndex - 1 + playList.size()) % playList.size();
            playSong(playList.get(currentSongIndex));
            Image gifImage = new Image(new File(gifPath).toURI().toString());
            albumCover.setImage(gifImage);
            playButton.setGraphic(pauseImageView);
        });

        nextButton.setOnAction(event -> {
            List<File> playList;
            if (filtered.isEmpty()) {
                playList = fileList;
            } else {
                playList = filtered;
            }
            currentSongIndex = (currentSongIndex + 1) % playList.size();
            playSong(playList.get(currentSongIndex));
            Image gifImage = new Image(new File(gifPath).toURI().toString());
            albumCover.setImage(gifImage);
            playButton.setGraphic(pauseImageView);
        });

        repeatButton.setOnAction(event -> {
            repeatEnable = !repeatEnable;
            repeatButton.setSelected(repeatEnable);
            if (repeatEnable) {
                repeatButton.setGraphic(repeatpauseView);
            } else {
                repeatButton.setGraphic(repeatView);
            }
        });

        shuffleButton.setOnAction(event -> {
            List<File> playList;
            if (filtered.isEmpty()) {
                playList = fileList;
            } else {
                playList = filtered;
            }
            Collections.shuffle(playList);
            songList.getItems().clear();
            for (File file : playList) {
                String songName = file.getName();
                songList.getItems().add(songName);
            }
            Image gifImage = new Image(new File(gifPath).toURI().toString());
            albumCover.setImage(gifImage);
            playSong(playList.get(currentSongIndex));
            updateSongList();
        });

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> filterSongList(newValue));

        equalizerButton.setOnAction(event -> openEqualizer());

        deleteButton.setOnAction(event -> {
            List<File> playList;
            if (filtered.isEmpty()) {
                playList = fileList;
            } else {
                playList = filtered;
            }
            ObservableList<Integer> selected = songList.getSelectionModel().getSelectedIndices();
            List<Integer> sorted = new ArrayList<>(selected);
            Collections.sort(sorted);

            boolean isCurrentSongDeleted = false;
            for (int i = sorted.size() - 1; i >= 0; i--) {
                int selectedIndex = sorted.get(i);
                if (selectedIndex >= 0 && selectedIndex < playList.size()) {
                    File fileToRemove = playList.get(selectedIndex);
                    if (mediaPlayer != null && fileToRemove.equals(playList.get(currentSongIndex))) {
                        isCurrentSongDeleted = true;
                    }
                    fileList.remove(fileToRemove);
                    if (!filtered.isEmpty()) {
                        filtered.remove(fileToRemove);
                    }
                    if (selectedIndex < currentSongIndex) {
                        currentSongIndex--;
                    } else if (selectedIndex == currentSongIndex) {
                        mediaPlayer.stop();
                        mediaPlayer = null;
                        timeSlider.setDisable(true);
                        playButton.setGraphic(playImageView);
                    }
                }
            }

            updateSongList();
            if (mediaPlayer == null || isCurrentSongDeleted) {
                currentSongLabel.setText("Now Playing: None");
                timeLabel.setText("00:00");
                volumeSlider.setValue(50);
                albumCover.setImage(image);
            }
        });

        clearAllButton.setOnAction(event -> {
            fileList.clear();
            filtered.clear();
            currentSongLabel.setText("Playlist Cleared");
            timeLabel.setText("00:00");
            updateSongList();
            mediaPlayer.stop();
            mediaPlayer = null;
            playButton.setDisable(true);
            timeSlider.setDisable(true);
            nextButton.setDisable(true);
            shuffleButton.setDisable(true);
            repeatButton.setDisable(true);
            searchBar.setDisable(true);
            equalizerButton.setDisable(true);
            previousButton.setDisable(true);
            volumeSlider.setValue(50);
            albumCover.setImage(image);
            playButton.setGraphic(playImageView);
        });

        timeSlider.setOnMousePressed(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
                updateElapsedTime(mediaPlayer.getCurrentTime());
            }
        });

        timeSlider.setOnMouseDragged(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
                updateElapsedTime(mediaPlayer.getCurrentTime());
            }
        });

        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                }
            }
        });
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void applyEqualizer() {
        saved.clear();
        for (Slider slider : bandSliders) {
            saved.add(slider.getValue());
        }
        if (mediaPlayer != null) {
            updateEqualizerSettings();
        } else {
            mediaPlayer.setOnReady(() -> updateEqualizerSettings());
        }
    }

    private void openEqualizer() {
        Stage equalizerStage = new Stage();
        equalizerStage.setTitle("Audio Equalizer");
        equalizerStage.initModality(Modality.NONE);
        equalizerStage.initStyle(StageStyle.UTILITY);
        equalizerStage.setOnShown(event -> {
            equalizerOpen = true;
            applyEqualizer();
            updateEqualizerSettings();
        });
        equalizerStage.setOnHidden(event -> equalizerOpen = false);
        HBox equalizerBox = new HBox(10);
        equalizerBox.setAlignment(Pos.CENTER);
        equalizerBox.setPadding(new Insets(10, 10, 10, 10));
        createEqualizerControls(equalizerBox);

        Button confirmButton = new Button("Confirm");
        confirmButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: black; " +
                        "-fx-border-width: 1px;");
        confirmButton.setOnMouseEntered(e -> {
            confirmButton.setStyle(
                    "-fx-background-color: #7081aa; " +
                            "-fx-border-color: black; " +
                            "-fx-border-width: 1px;");
        });
        confirmButton.setOnMouseExited(e -> {
            confirmButton.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-border-color: black; " +
                            "-fx-border-width: 1px;");
        });
        confirmButton.setOnMousePressed(e -> {
            confirmButton.setStyle(
                    "-fx-background-color: #3e4fae; " +
                            "-fx-border-color: black; " +
                            "-fx-border-width: 1px;");
        });
        confirmButton.setOnMouseReleased(e -> {
            confirmButton.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-border-color: black; " +
                            "-fx-border-width: 1px;");
        });
        confirmButton.setOnAction(event -> {
            applyEqualizer();
            updateEqualizerSettings();
        });
        Button restoreDefaultsButton = new Button("Defaults");
        restoreDefaultsButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: black; " +
                        "-fx-border-width: 1px;");
        restoreDefaultsButton.setOnMouseEntered(e -> {
            restoreDefaultsButton.setStyle(
                    "-fx-background-color: #7081aa; " +
                            "-fx-border-color: black; " +
                            "-fx-border-width: 1px;");
        });
        restoreDefaultsButton.setOnMouseExited(e -> {
            restoreDefaultsButton.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-border-color: black; " +
                            "-fx-border-width: 1px;");
        });
        restoreDefaultsButton.setOnMousePressed(e -> {
            restoreDefaultsButton.setStyle(
                    "-fx-background-color: #3e4fae; " +
                            "-fx-border-color: black; " +
                            "-fx-border-width: 1px;");
        });
        restoreDefaultsButton.setOnMouseReleased(e -> {
            restoreDefaultsButton.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-border-color: black; " +
                            "-fx-border-width: 1px;");
        });
        restoreDefaultsButton.setOnAction(event -> {
            for (Slider slider : bandSliders) {
                slider.setValue(0.0);
            }
            saved = new ArrayList<>(Collections.nCopies(EQUALIZER_BAND.size(), 0.0));
            updateEqualizerSettings();
        });
        equalizerBox.getChildren().add(restoreDefaultsButton);
        equalizerBox.getChildren().add(confirmButton);
        BackgroundFill backgroundFill = new BackgroundFill(Color.web("#FFDAB9"), CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        equalizerBox.setBackground(background);
        equalizerScene = new Scene(equalizerBox);
        equalizerStage.setScene(equalizerScene);
        equalizerStage.show();
    }

    private void createEqualizerControls(HBox equalizerBox) {
        bandSliders.clear();
        for (int i = 0; i < EQUALIZER_BAND.size(); i++) {
            double freq = EQUALIZER_BAND.get(i);
            VBox bandBox = new VBox(5);
            bandBox.setAlignment(Pos.CENTER);
            Label freqLabel = new Label(String.format("%.0f Hz", freq));
            Slider bandSlider = new Slider(-24.0, 12.0, 0.0);
            if (i < saved.size()) {
                bandSlider.setValue(saved.get(i));
            } else {
                saved.add(bandSlider.getValue());
            }
            bandSlider.setOrientation(Orientation.VERTICAL);
            bandSlider.setPrefHeight(100);
            bandSliders.add(bandSlider);
            bandBox.getChildren().addAll(bandSlider, freqLabel);
            equalizerBox.getChildren().add(bandBox);
            bandSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                updateEqualizer(freq, newValue.floatValue());
            });
        }
    }

    private void initializeEqualizerBands(MediaPlayer mediaPlayer) {
        AudioEqualizer audioEqualizer = mediaPlayer.getAudioEqualizer();
        audioEqualizer.getBands().clear();
        double min = EQUALIZER_BAND.get(0);
        double max = EQUALIZER_BAND.get(EQUALIZER_BAND.size() - 1);
        int count = EQUALIZER_BAND.size();

        for (int i = 0; i < count; i++) {
            double fraction = i / (double) (count - 1);
            double freq = min + fraction * (max - min);
            EqualizerBand band = new EqualizerBand(freq, freq / 2, 0.0);
            audioEqualizer.getBands().add(band);
        }
        updateEqualizerSettings();
    }

    private void updateEqualizerSettings() {
        if (mediaPlayer != null && equalizerOpen) {
            AudioEqualizer equalizer = mediaPlayer.getAudioEqualizer();
            equalizer.setEnabled(true);

            for (int i = 0; i < equalizer.getBands().size() && i < saved.size(); i++) {
                equalizer.getBands().get(i).setGain(saved.get(i));
            }
        }
    }

    private void updateEqualizer(double freq, float gain) {
        if (mediaPlayer != null) {
            AudioEqualizer equalizer = mediaPlayer.getAudioEqualizer();
            for (EqualizerBand band : equalizer.getBands()) {
                if (band.getCenterFrequency() == freq) {
                    band.setGain(gain);
                    break;
                }
            }
        }
    }

    private void filterSongList(String text) {
        if (text == null || text.isEmpty()) {
            updateSongList();
            filtered.clear();
        } else {
            filtered.clear();
            String LowerCase = text.toLowerCase();

            for (File file : fileList) {
                if (file.getName().toLowerCase().contains(LowerCase)) {
                    filtered.add(file);
                }
            }
            songList.getItems().clear();
            for (File file : filtered) {
                songList.getItems().add(file.getName());
            }
        }
    }

    private void updateSongList() {
        songList.getItems().clear();
        List<File> playList;
        if (filtered.isEmpty()) {
            playList = fileList;
        } else {
            playList = filtered;
        }
        for (File file : playList) {
            songList.getItems().add(file.getName());
        }
    }

    private String getFormattedSongName(File file) {
        String fileName = file.getName();
        fileName = fileName.substring(0, fileName.lastIndexOf(".mp3"));
        return fileName;
    }

    private List<File> getCurrentSong() {
        if (filtered.isEmpty()) {
            return fileList;
        } else {
            return filtered;
        }
    }

    private void playSong(File file) {
        List<File> playList = getCurrentSong();
        currentSongIndex = playList.indexOf(file);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        playButton.setSelected(true);
        currentSongLabel.setText("Now Playing: " + getFormattedSongName(file));
        currentSongLabel.setStyle("-fx-font-weight: bold;");
        initializeEqualizerBands(mediaPlayer);
        updateEqualizerSettings();
        mediaPlayer.setOnReady(() -> {
            timeSlider.setDisable(false);
            timeSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
            updateElapsedTime(Duration.ZERO);
            updateRemainingTime(Duration.ZERO, mediaPlayer.getTotalDuration());
        });

        mediaPlayer.setOnError(() -> {
            System.out.println("Error playing the media.");
            mediaPlayer.stop();
            mediaPlayer = null;
            playButton.setSelected(false);
        });

        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                updateElapsedTime(mediaPlayer.getCurrentTime());
                updateRemainingTime(mediaPlayer.getCurrentTime(), mediaPlayer.getTotalDuration());
            }
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            if (repeatEnable) {
                playSong(playList.get(currentSongIndex));
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            } else {
                currentSongIndex = (currentSongIndex + 1) % playList.size();
                playSong(playList.get(currentSongIndex));
            }
        });

        if (playList.isEmpty()) {
            timeLabel.setText("00:00");
        }

    }

    private void updateElapsedTime(Duration elapsed) {
        timeSlider.setValue(elapsed.toSeconds());
        timeLabel.setText(formatTime(elapsed));
    }

    private void updateRemainingTime(Duration currentTime, Duration totalDuration) {
        if (mediaPlayer != null && mediaPlayer.getStatus() == Status.PLAYING) {
            Duration remainingTime = totalDuration.subtract(currentTime);
            timeLabel.setText(" " + formatTime(remainingTime));
        } else {
            timeLabel.setText("00:00");
        }
    }

    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////