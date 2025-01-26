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
#### Creator note: 
- There are still issues with setting the application icon
- A bug was detected where the screen was not saved upon returning into, causing navigation error, needs rectification
- Background music had a few bugs that is fixed temporarily, there may be some additional problems, additional testing needed
- Screens should be henceforth named "scene" in the sourcecode, for IDE consistency 
- There will be more consistency to the screen background image and button dimensions, the current screen size is 1280x720, button dimensions are presumably 43x27. These will be standardized
- Transition animation between scenes will be worked upon  
- Images for cards and pieces is blurry. Therefore, an upscale of them is needed. Current solution: Topaz Gigapixel AI (paid), Let’s Enhance (free trial), Waifu2x (free) =>Solution: Thanh draws them himself
- Function naming convention issue: goBack(ActionEvent e) and QuitGame(ActionEvent e)
- Return button's size is not standardized: The return button in DifficultyScene.fxml is bigger then the rest
- Pikachu card in SinglePlayer() is magically disappeared
- [In production] Making a difficulty selections window for single player (SingleConfig.fxml)

