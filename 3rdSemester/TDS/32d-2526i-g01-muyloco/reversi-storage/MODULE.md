Module reversi-storage

## Overview

Provides persistent storage for game snapshots using the local filesystem. This module implements a simple,
text-based storage system that favors human-readable saves for easy debugging and manual inspection. Games are stored
as plain text files in a configurable `saves` folder, allowing users to view and manually edit saved games if needed.

The module follows a clean separation of concerns: storage logic is decoupled from the serialization format.
Different serializers can be plugged in to convert domain types to/from various formats (currently text-based).

![Reversi App Screenshot](../images/UML_Structure_storage.png)

## Architecture

The module consists of:

- **Storage Contracts** — Generic Storage and AsyncStorage interfaces
- **File-Based Implementations** — FileStorage and AsyncFileStorage
- **Serializer Contract** — Generic Serializer interface
- **Optional Database Support** — MongoDB implementations (AsyncMongoDBStorage)

### Storage Contracts

#### Storage<K, T, U>

Synchronous storage contract:

- `K` — Key type (typically String for game names)
- `T` — Domain type being stored (e.g., GameState)
- `U` — Storage format (typically String)

Methods:

- `new(id, factory)` — Creates a new entity and stores it
- `load(id)` — Retrieves an entity by key
- `save(id, obj)` — Persists an entity
- `delete(id)` — Removes an entity
- `lastModified(id)` — Gets modification timestamp
- `loadAllIds()` — Lists all stored keys
- `close()` — Releases resources

#### AsyncStorage<K, T, U>

Same contract as Storage but with suspend functions for non-blocking I/O:

- All methods are `suspend fun`
- Suitable for coroutine-based applications
- Used by reversi-cli for responsive user experience

### Serializer Contract

#### Serializer<T, U>

Converts between domain types and storage format:

- `serialize(obj: T): U` — Domain object to storage format
- `deserialize(obj: U): T` — Storage format to domain object

Must preserve round-trip identity: `deserialize(serialize(x)) == x`

## File-Based Storage

### FileStorage

Implements synchronous Storage contract using text files:

- Stores each entity in a separate `.txt` file
- Files named after the entity key (game name)
- Uses provided Serializer to convert GameState to/from text
- Creates saves folder and files as needed

Example: Game "mygame" is stored in `saves/mygame.txt`

### AsyncFileStorage

Non-blocking version of FileStorage:

- Uses Kotlin coroutines for I/O
- Suitable for interactive applications (CLI)
- Prevents UI freezing during save/load operations

### File Format

Each saved game file contains:

1. **Players Line** — Serialized player data (symbol,points;...)
2. **Last Player Line** — Who played last (# or @)
3. **Winner Line** — Winner data if game ended (empty if ongoing)
4. **Board Lines** — Board size, then pieces (row,col,symbol)

Example:

```
#,12;@,5;
#

8
4,4,@
4,5,#
5,4,#
5,5,@
```

This format is:

- **Human-readable** — Can be manually inspected
- **Line-oriented** — Easy to parse and debug
- **Sparse** — Only stores pieces, not empty squares
- **Versionable** — Can be enhanced with format markers

## Database Support

### AsyncMongoDBStorage

Optional MongoDB backend for storing games:

- Stores game documents in MongoDB
- Implements AsyncStorage interface
- Provides scalability for server deployments
- Used by desktop app or server versions

## Serialization Layers

The storage module uses serializers from the core module:

### Core Serializers (in reversi-core)

- `GameStateSerializer` — Entire game state
- `BoardSerializer` — Board and pieces
- `PlayerSerializer` — Player data
- `PieceSerializer` — Individual pieces
- `PieceTypeSerializer` — Piece symbols

### Storage Module Role

Storage module knows nothing about GameState; it only:

1. Calls the provided Serializer
2. Writes/reads bytes from files
3. Manages directory structure and file lifecycle

This separation means:

- Serialization logic lives with domain types (core module)
- Storage logic is reusable with any Serializable type
- Different storage backends can be used without changing serializers

## Configuration

Storage uses configuration from core module via `CoreConfig`:

- `savesPath` — Directory for saved games (default: "saves")
- Can be changed at runtime to test different storage locations

## Error Handling

Storage exceptions (typically from FileSystem):

- File not found when loading non-existent game
- I/O errors when reading/writing files
- Permission errors when accessing file system
- Serialization errors from malformed files

These propagate to the caller as `Exception` or `IOException`.

## Design Principles

1. **Simplicity** — Plain text files, easy to inspect
2. **Human-Readable** — Users can view and edit saves
3. **Separation of Concerns** — Storage doesn't know about domain types
4. **Pluggable Serializers** — Can support multiple formats
5. **Async Support** — Non-blocking I/O for interactive apps
6. **Optional Backends** — MongoDB support for scalability

## Usage Patterns

### Loading a Game

```
val storage = AsyncFileStorage(GameStateSerializer(), "saves")
val gameState = storage.load("mygame")
```

### Saving a Game

```
storage.save("mygame", gameState)
```

### Listing All Games

```
val allGameNames = storage.loadAllIds()
```

### Cleaning Up

```
storage.close()
```

## Integration

The storage module integrates with:

- **reversi-core** — Domain types and serializers
- **reversi-cli** — Uses AsyncFileStorage for game persistence
- **File System** — Reads/writes `.txt` files in saves folder
- **Optional: MongoDB** — Alternative backend for AsyncMongoDBStorage

#Package pt.isel.reversi.storage

#Package pt.isel.reversi.storage

## Overview

Implements persistence using plain text files placed in a `saves` folder by default, with optional MongoDB support
for scalable storage. The storage code is intentionally simple: domain objects are converted to a textual 
representation via `Serializer` implementations and written to files. This approach favors human-readable saves 
that are easy to debug and test.

### Key Components

- `Storage<K, T, U>` — Generic synchronous storage interface
    - `K` — Key type (typically String for game names)
    - `T` — Domain type (e.g., GameState)
    - `U` — Storage format (typically String)
    - Operations: new, load, save, delete, lastModified, loadAllIds, close

- `AsyncStorage<K, T, U>` — Generic asynchronous storage interface
    - Same operations as Storage but with suspend functions
    - Suitable for coroutine-based applications
    - Prevents blocking UI during I/O operations

- `FileStorage` — Synchronous file-based storage implementation
    - Stores each game in a separate `.txt` file
    - Uses provided Serializer for domain object conversion
    - Creates directories and files as needed

- `AsyncFileStorage` — Asynchronous file-based storage implementation
    - Non-blocking version using Kotlin coroutines
    - Same functionality as FileStorage with async I/O
    - Preferred for interactive applications

- `MongoDBStorage` — Synchronous MongoDB storage implementation
    - Stores games in MongoDB collections
    - Provides scalability for server deployments

- `AsyncMongoDBStorage` — Asynchronous MongoDB storage implementation
    - Non-blocking MongoDB operations with coroutines
    - Suitable for high-concurrency scenarios

- `MongoDBConnection` — MongoDB connection management
    - Handles connection pooling and configuration

- `Serializer<T, U>` — Interface for domain-to-storage conversion
    - `serialize(obj: T): U` — Convert domain object to storage format
    - `deserialize(obj: U): T` — Convert storage format back to domain
    - Must preserve round-trip identity

### Responsibilities

- Reading and writing game snapshots to local text files or MongoDB
- Providing `Serializer` interface for domain type conversion
- Managing file/database lifecycle (creation, deletion, updates)
- Validating file structure and reporting errors when data is malformed
- Supporting both synchronous and asynchronous I/O patterns

### Storage Backends

**File Storage** (default):
- Human-readable text files
- Easy to inspect and debug
- Suitable for single-user applications

**MongoDB Storage** (optional):
- Document-based storage
- Scalable for multiple users
- Requires MongoDB server

### Notes

- The concrete serializers for core types live in `reversi-core` under `core.storage.serializers` and are composed by
  storage implementations through the `Storage` contract.
- Default folder used by the project is `saves` (see `Environment.kt` in core).
- File format is intentionally simple and human-readable for debugging.
