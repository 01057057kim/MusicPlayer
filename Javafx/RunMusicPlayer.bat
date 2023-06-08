@echo off
set "PATH_TO_FX=javafx-sdk-17.0.7\lib"
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.media,javafx.fxml -cp MusicPlayer.jar MusicPlayer
