Module reversi-app

## Overview

A modern desktop GUI application for playing Reversi using Jetpack Compose for Desktop. This module provides a rich,
interactive user experience while maintaining clean separation from the core game logic and storage systems. The app
follows MVVM (Model-View-ViewModel) architecture with reactive state management for responsive, testable code.

The module builds upon the immutable core domain model and storage layer, providing a layer of UI logic and user
interaction management. All game rules and state are delegated to the core module, keeping the app lightweight and
focused on presentation.

![Reversi App Structure](../images/UML_Structure_APP.png)

## Architecture

The app is organized into layers:

- **Main Entry Point** — Application initialization and window setup
- **UI Pages/Screens** — Individual game screens and menus
- **ViewModels** — State management and business logic coordination
- **UI Components** — Reusable Compose components
- **Configuration** — App-specific settings and theming

### MVVM Pattern

**ViewModel Layer:**

- Manages state for each screen
- Coordinates between UI and core logic
- Handles user input (moves, commands)
- Manages coroutines for async operations
- Exposes state as observable flows

**View Layer (Compose):**

- Renders UI based on ViewModel state
- Sends user events to ViewModel
- Handles animations and transitions
- Provides visual feedback

**Model Layer (Core):**

- Immutable Game and GameState objects
- Pure game logic (via core module)
- Persistence (via storage module)

## Key Components

### Main Application

The entry point that:

1. Initializes configuration
2. Creates the main window
3. Sets up theming and styling
4. Launches the root composable
5. Manages application lifecycle

### Pages/Screens

#### Menu Screen

- New Game creation
- Load Game selection
- Settings
- Exit

#### Game Screen

- Interactive board display
- Piece rendering with colors
- Legal move highlighting
- Player scores
- Game state display
- Move submission
- Pass button
- Game menu

#### Game Over Screen

- Winner announcement
- Final scores
- Play Again option
- Menu option

### ViewModels

Each screen has a corresponding ViewModel:

**MenuViewModel:**

- Available games list
- New game parameters
- Navigation state

**GameViewModel:**

- Current game state
- Board representation
- Available moves
- Player information
- Move execution coordination

**GameOverViewModel:**

- Winner determination
- Final statistics
- Return to menu

### Composable Components

**Board Display:**

- Grid layout for 8x8 board
- Piece rendering with symbols/colors
- Legal move highlighting
- Clickable squares for move input
- Animations for piece placement

**Player Info:**

- Current player indicator
- Score display
- Piece color indicators
- Turn status

**Buttons and Controls:**

- Move buttons
- Pass button
- Game menu button
- New Game button
- Load Game button

## User Experience

### Visual Feedback

- Board highlights legal moves
- Pieces animated when placed
- Colors distinguish Black and White
- Turn indicator shows who plays next
- Score updates in real-time

### Game Flow

1. Start app → See main menu
2. Create new game or load saved game
3. View board with current player highlighted
4. Click square to play move
5. Move executes with animation
6. Board updates with captured pieces
7. Turn passes to other player
8. Repeat until game ends
9. See final score and results
10. Return to menu to play again

### Save/Load

- Current game auto-saves to storage
- Can resume interrupted games
- Load screen shows all saved games
- Delete games from UI

## Reactive State Management

Uses Kotlin Flow for reactive updates:

- Board state flows to UI
- Player scores flow to UI
- Available moves flow to UI
- Game over state flows to UI
- All updates are immutable and predictable

Benefits:

- UI automatically updates when state changes
- Testable state transformations
- No callback hell
- Easy to implement undo/redo
- Easy to implement multiplayer

## Configuration

Reads from `reversi-app.properties`:

- Theme settings (colors, fonts)
- Animation speeds
- Board display options
- Default game parameters
- Audio settings
- Accessibility options

## Integration

The app integrates with:

- **reversi-core** — Game logic and types
- **reversi-storage** — Save/load functionality
- **reversi-utils** — Configuration and logging
- **Jetpack Compose** — UI framework
- **Kotlin Coroutines** — Async operations

## Advantages Over CLI

- **Visual Board** — Much easier to understand board state
- **Click Interface** — Faster than typing coordinates
- **Live Feedback** — Immediate visual response
- **Animation** — Shows piece captures visually
- **Save/Load UI** — Browse saved games
- **Multi-player Friendly** — Can rotate device for hotseat play
- **Modern Look** — Attractive, native desktop appearance

## Design Principles

1. **Separation of Concerns** — UI, logic, and persistence are separate
2. **Immutability** — State is immutable; changes create new objects
3. **Reactivity** — UI automatically responds to state changes
4. **Testability** — ViewModels can be tested without UI
5. **Responsiveness** — Heavy operations run in background coroutines
6. **Reusability** — Components can be used in multiple screens
7. **Composability** — Large screens built from small components

## Testing

**Unit Tests:**

- ViewModel logic without UI
- State transitions
- Move validation
- Configuration loading

**UI Tests (Future):**

- Component rendering
- User interaction
- Visual regression
- Accessibility

**Integration Tests:**

- Full game flow
- Save/load round-trips
- Config persistence

## Future Enhancements

Potential improvements:

- Network multiplayer (play against remote opponent)
- AI opponent using minimax or neural network
- Game statistics and analytics
- Elo rating system
- Game replay and analysis tools
- Custom board sizes
- Themes and customization
- Sound effects and music
- Undo/redo during gameplay
- Move suggestions/hints

#Package pt.isel.reversi.app

#Package pt.isel.reversi.app

## Overview

Hosts the main application entry point and desktop GUI components. The application integrates with the core game
logic and storage system while providing a rich user interface for playing Reversi.

### Key Classes

- `Main` — Application entry point and window setup
- `MenuBar` — Top menu bar for the application window
- `ScaffoldView` — Main scaffold structure that hosts all pages
- Game UI Components — Interactive board rendering and move handling
- State Management — ViewModel-based state management for game session

### Responsibilities

- Providing an interactive desktop GUI for the Reversi game
- Managing UI state and user interactions
- Coordinating with the core game logic and storage modules
- Handling player input and rendering the game board
- Managing window structure and navigation between pages

#Package pt.isel.reversi.app.app

## Overview

Contains the core application infrastructure, theme management, and global state management for the Reversi desktop app.

### Key Components

- `App` — Main application composable and setup
- `AppTheme` — Theme configuration interface with color schemes, fonts, and resource definitions
- `AppThemes` — Collection of predefined themes (default, dark, light themes)
- `AppState` — Interface for managing global application state
- `AppStateImpl` — Implementation of AppState with reactive state management
- `AppStateUtils` — Helper utilities for state operations
- `ReversiScope` — Coroutine scope management for async operations

### Responsibilities

- Initializing and managing application-wide state
- Providing theme configurations and switching
- Managing coroutine scopes for background operations
- Coordinating between different pages and view models
- Handling application lifecycle events

#Package pt.isel.reversi.app.pages

## Overview

Contains all UI pages/screens of the application, their view models, and page navigation logic.

### Key Components

- `Page` — Sealed interface defining all possible pages in the app
- `ViewModel` — Base interface for all page view models
- `createPageView` — Factory function to create page composables
- `createViewModel` — Factory function to create view models for pages

### Sub-packages

- `menu` — Main menu page with New Game, Load Game, Settings, Exit options
- `newGamePage` — Game creation interface with player configuration
- `lobby` — Game lobby for joining/managing multiplayer games
- `game` — Active game page with board, controls, and game state display
- `winnerPage` — End game screen showing winner and final scores
- `aboutPage` — About/credits page
- `settingsPage` — Application settings and configuration

### Responsibilities

- Defining the structure of each application screen
- Managing page-specific state through ViewModels
- Handling navigation between pages
- Providing reusable page creation patterns

#Package pt.isel.reversi.app.pages.menu

## Overview

Main menu screen where users can start a new game, load saved games, access settings, or exit the application.

### Components

- `MainMenu` — Main menu page composable
- `MainMenuBg` — Background rendering for the menu
- `MainMenuViewModel` — State management for menu interactions

### Responsibilities

- Displaying main menu options
- Handling user navigation to other pages
- Managing menu state and animations

#Package pt.isel.reversi.app.pages.newGamePage

## Overview

Screen for creating a new game with player configuration options.

### Components

- `NewGamePage` — New game creation page composable
- `NewGameViewModel` — State management for game creation

### Responsibilities

- Configuring game parameters (player names, types, board size)
- Validating game creation inputs
- Initiating new game sessions

#Package pt.isel.reversi.app.pages.lobby

## Overview

Lobby interface for browsing, joining, and managing multiplayer games.

### Components

- `LobbyMenu` — Main lobby composable
- `LobbyViewModel` — State management for lobby operations
- `lobbyViews/` — Sub-components for displaying game lists and details

### Responsibilities

- Displaying available games
- Allowing players to join existing games
- Managing game lobby state
- Refreshing game lists from storage

#Package pt.isel.reversi.app.pages.game

## Overview

Active game screen with interactive board, move controls, and real-time game state display.

### Components

- `GamePage` — Main game page composable
- `GamePageView` — Game board and controls view
- `GamePageViewModel` — Game state management and move coordination
- `utils/` — Helper utilities for game rendering and interactions

### Responsibilities

- Rendering the interactive game board
- Handling player move input
- Displaying current game state (scores, turn, available moves)
- Managing game flow (play, pass, forfeit)
- Providing visual feedback for moves and captures

#Package pt.isel.reversi.app.pages.winnerPage

## Overview

End game screen displaying final results, winner announcement, and options to play again.

### Responsibilities

- Showing winner and final scores
- Displaying game statistics
- Offering options to return to menu or start a new game

#Package pt.isel.reversi.app.pages.aboutPage

## Overview

About page with application information, credits, and version details.

### Responsibilities

- Displaying application information
- Showing credits and acknowledgments
- Providing version and build information

#Package pt.isel.reversi.app.pages.settingsPage

## Overview

Settings screen for configuring application preferences and game options.

### Responsibilities

- Managing theme selection
- Configuring audio settings
- Adjusting game preferences
- Persisting user settings

#Package pt.isel.reversi.app.exceptions

## Overview

Application-specific exceptions for handling GUI-related errors and user-facing error states.

### Exception Types

- `CouldNotLoadAsset` — Thrown when audio/visual assets fail to load
- `ErrorMessage` — Base exception for user-displayable error messages
- `ExitApp` — Exception to signal application exit
- `GameCorrupted` — Thrown when loaded game data is corrupted
- `GameIsFull` — Thrown when attempting to join a full game
- `GameNotStartedYet` — Thrown when operations require an active game
- `NoPieceSelected` — Thrown when move requires piece selection

### Responsibilities

- Providing user-friendly error messages
- Distinguishing recoverable from fatal errors
- Supporting graceful error handling in the UI

#Package pt.isel.reversi.app.gameAudio

## Overview

Audio system management for game sound effects and background music.

### Components

- `loadGameAudioPool` — Loads and configures the application's audio pool based on theme

### Responsibilities

- Loading audio assets from resources
- Configuring music tracks to loop
- Managing sound effects
- Handling missing audio files gracefully
- Integrating with theme-based audio configurations

#Package pt.isel.reversi.app.utils

## Overview

Utility functions and helper components for the app UI layer.

### Components

- `ButtonPreviousPage` — Reusable back button component
- `InitializationUtils` — Application initialization helpers
- `Loading` — Loading indicator composable
- `PageSwitcher` — Navigation helper for page transitions
- `ProcessUtils` — Process and lifecycle management utilities

### Responsibilities

- Providing reusable UI components
- Supporting app initialization
- Managing page transitions
- Handling loading states
