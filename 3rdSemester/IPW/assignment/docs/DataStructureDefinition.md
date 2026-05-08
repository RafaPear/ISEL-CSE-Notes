# Definition of Data Structures used in Foccacia Data Layer

This document outlines the data structures utilized in the Foccacia application's data layer, specifically for handling football-related data. The data structures are designed to facilitate efficient storage, retrieval, and manipulation of information related to football leagues, teams, players, matches, and statistics.

## 1. User Data Structure
```javascript
{
    username: String,       // Unique identifier for the user
    token: String,          // Universally unique identifier for the user (UUID format)
    groupAmount: Number     // Number of groups associated with the user
}
```

## 2. Player Data Structure
```javascript
{
    id: Number,
    name: String,
    teamId: Number,
    teamName: String,
    position: String,
    nationality: String,
    age: Number
}
```

## 3. Group Data Structure
```javascript
{
    token: String,        // User token associated with the group
    id: Number,            // Unique identifier for the group
    name: String,           // Name of the group
    description: String,    // Description of the group
    competition: String, // List of competition ID associated with the group
    year: Number,          // Year of the competition
    players: [Player Data Structure]  // Array of Player Data Structures
}
```