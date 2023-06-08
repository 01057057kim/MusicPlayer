01057057蔡金保-01057059康維成

-if Music Player.jar doesn't work use the runMusicPlayer.bat
and if you want to execute from vscode need change the launch.json, setting.json and add the lib to Referenced Libraries.
and use cmd to run the code  VVV

javac --module-path PATH\javafx-sdk-17.0.7\lib --add-modules javafx.controls,javafx.media,javafx.fxml -d bin MusicPlayer.java   

java --module-path PATH\javafx-sdk-17.0.7\lib --add-modules javafx.controls,javafx.media,javafx.fxml -cp bin MusicPlayer


And this the demo video VVVV
-Youtube link https://youtu.be/22MVv1zyI9U

-Alternative link https://drive.google.com/file/d/1cJ_5tRXyHRmAqIewNIRBE-YFSTd9aZfs/view?usp=sharing
