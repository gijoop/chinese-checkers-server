# Chinese Checkers Server

**Chinese Checkers Server** is the backend server for a multiplayer Chinese Checkers game. It handles game sessions, real-time communications, and game logic, making it easy to connect with compatible clients.

> **Note:** This project is part of a multi-repository system. It depends on the [Chinese Checkers Comms](https://github.com/gijoop/chinese-checkers-comms) library for communication protocols, and the client application is available at [Chinese Checkers Client](https://github.com/CoconutOnPalm/chinese-checkers-client).

## Features

- **Multiplayer Support:** Handles multiple concurrent game sessions.
- **Player vs. Computer:** Supports single-player games against simple AI opponents.
- **Real-Time Game Management:** Maintains game state and synchronizes moves among players.
- **Robust Communication:** Utilizes a dedicated communications library for seamless data exchange.
- **Extensible Architecture:** Modular design for easy expansion and maintenance.
- **Customizable Settings:** Configuration options for server parameters and game rules.

## Installation

Follow these steps to set up the Chinese Checkers Server:

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/gijoop/chinese-checkers-server.git
   cd chinese-checkers-server
   ```

2. **Install Dependencies:**

   - Ensure you have the [Chinese Checkers Comms](https://github.com/gijoop/chinese-checkers-comms) library available. Follow its instructions for installation if needed.
   - Verify that the client at [Chinese Checkers Client](https://github.com/CoconutOnPalm/chinese-checkers-client) is set up on your system if you plan to run end-to-end tests.

3. **Build the Project:**

   Depending on your build system, run the appropriate command. For example, if you are using Maven:

   ```bash
   mvn clean install
   ```

## Running the Server

After building the project, launch the server with the following steps:

1. **Start the Server:**

   ```bash
   mvn exec:java -Dexec.mainClass="com.chinese_checkers.server.Main"
   ```

   The server will start on the default port (adjustable via configuration).

2. **Configuration:**

![CLI](image.png)

   - The server can be configured via its CLI interface.
   - You can set the server port, maximum number of players, and other game settings.
   - Refer to the CLI help for more information on available options.


## Game rules
The game rules for Chinese Checkers can be viewed on the [Chinese Checkers Wikipedia page](https://en.wikipedia.org/wiki/Chinese_checkers).

## Usage

- **Client Connection:** Once the server is running, the Chinese Checkers Client can connect to it to start or join game sessions.
