# How to run GUI
## Setup:
- Import the files from this branch into your local machine and open it in any IDE to your liking (I use Eclipse btw).

*Note: Currently, on VS Code, the project can only be run with JDK 23. Backward compatibility with JDK 21 was unsuccessful upon testing.*
## Run:
- If you successfully installed the files and opened it, navigate to src>application>Main.java, then run the file.
## Details:
### Main Class:
- The gameplay experience will execute starting from this class, it includes the application setups such as game title, screensize, etc.
### Controller Class:
- Handles events, scene switching, and background music
- Return capability now available
### application.css:
- Creates a few tweaks for the UI designs that can't be done in SceneBuilder

## Development Plan for Ubongo Junior App - By Baked Beans

### Objectives
This document outlines the development plan for creating a Windows-based Ubongo Junior board game application. The project will be developed using Java with JavaFX for GUI, with iterative delivery over 6 weeks.

#### Primary Objectives:
1. **Core Game Functionality:** Implement the rules of the game, including board setup, tile placement, and timer-based gameplay.
2. **Graphical User Interface (GUI):** Design an intuitive and visually appealing user interface using JavaFX.
3. **User Interactivity:** Ensure smooth drag-and-drop functionality for placing tiles on the game board.
4. **Multiplayer Mode:** Implement both single-player (against a timer) and local multiplayer modes.
5. **Score Tracking:** Include a scoring mechanism to track player performance across multiple rounds.
6. **Instructions and Tutorial:** Provide an in-app tutorial to help new players understand the game.
7. **Testing and Debugging:** Ensure the app runs smoothly with no critical bugs.
8. **Packaging and Deployment:** Create an executable JAR file for easy installation and use on Windows.

#### Creator note: 
- There are still issues with setting the application icon
- A bug was detected where the screen was not saved upon returning into, causing navigation error, needs rectification
- Background music had a few bugs that is fixed temporarily, there may be some additional problems, additional testing needed
- Screens should be henceforth named "scene" in the sourcecode, for IDE consistency 
- There will be more consistency to the screen background image and button dimensions, the current screen size is 1280x720, button dimensions are presumably 43x27. These will be standardized
- Transition animation between scenes will be worked upon  
- ``Images for cards and pieces is blurry. Therefore, an upscale of them is needed. Current solution: Topaz Gigapixel AI (paid), Let’s Enhance (free trial), Waifu2x (free) =>Solution: Thanh draws them himself``
- Function naming convention issue: goBack(ActionEvent e) and QuitGame(ActionEvent e)
- ``Return button's size is not standardized: The return button in DifficultyScene.fxml is bigger then the rest``
- ``Pikachu card in SinglePlayer() is magically disappeared``
- Add new difficulty selection window for singleplaying: SingleConfig.fxml (via Start ->SingleConfig -> GameScreen)
- ~~Remove return button in GameScreen.fxml as requested~~
- ~~Recover GameScreen.fxml to original state. However, the pikachu card is gone.~~
- Added a scoring system based on time (see details below)
- [EXPERIMENTAL - SUCCESSFUL] Added a testing mechanic for Scoring System based on clicking button


#### Revamped Scoring System:
- Score are calculated by time used to solve a card (per second, accurate to 2 decimal)
- To make use of the gems (in some variants), we change it into multipliers (x3,x2,x1)
- Let RemainingTime = RoundDuration - UsedTime
- We have the following:
1. UsedTime <= 25% * RoundDuration => Score = RemainingTime * 3
2. UsedTime <= 50% * RoundDuration => Score = RemainingTime * 2
3. UsedTime <= 75% * RoundDuration => Score = RemainingTime * 1
4. UsedTime >=RoundDuration => Score = 0