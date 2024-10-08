# BeatBoard - A MIDI-based Beat Sequencer

**BeatBoard** is a simple graphical beat sequencer built using Java Swing and MIDI libraries. The program allows you to create rhythmic sequences by toggling instrument buttons on a 16x16 grid. You can start/stop playback, adjust the tempo, and save/load your sequences to/from files.

## Features

- **16x16 Beat Grid**: A grid of toggle buttons where each row represents a different instrument, and each column represents a beat in a sequence.
- **MIDI Playback**: The grid triggers MIDI events, and the beats are played back in real-time using a sequencer.
- **Tempo Control**: Increase or decrease the tempo using the "Tempo Up" and "Tempo Down" buttons.
- **Save/Load Sequences**: Save your beat patterns to a file and load them back later.
- **Instruments**: The sequencer supports 16 different instruments:
  - Bass Drum
  - Closed Hi-Hat
  - Open Hi-Hat
  - Acoustic Snare
  - Crash Cymbal
  - Hand Clap
  - High Tom
  - Hi Bongo
  - Maracas
  - Whistle
  - Low Conga
  - Cowbell
  - Vibraslap
  - Low-mid Tom
  - High Agogo
  - Open Hi Conga

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/BeatBoard.git
    ```
2. Compile the Java files:
    ```bash
    javac BeatBoard.java
    ```
3. Run the program:
    ```bash
    java BeatBoard
    ```

## Usage

### Controls

- **Start**: Begin playback of the current beat pattern.
- **Stop**: Stop playback.
- **Tempo Up**: Increase the tempo by 3%.
- **Tempo Down**: Decrease the tempo by 3%.
- **Save**: Save the current beat pattern to a file.
- **Load**: Load a previously saved beat pattern.

### Creating a Beat

1. Toggle the buttons in the grid to select beats for each instrument.
2. Press "Start" to hear your pattern.
3. Adjust the tempo using the "Tempo Up" or "Tempo Down" buttons as needed.
4. Save your pattern by clicking the "Save" button. You can load it later using the "Load" button.

## Dependencies

- Java 8 or higher.
- Java MIDI library (javax.sound.midi).
- Java Swing for the graphical user interface (javax.swing).

## How It Works

- The 16x16 grid represents a 16-step sequencer for 16 instruments.
- The `JToggleButton` components are used for the beat grid, where a selected button means a beat is activated for that instrument at that step.
- MIDI events are generated based on the selected grid pattern, and the MIDI sequencer plays them in a loop.
- You can save the state of the grid to a serialized file and load it back later to continue editing or playback.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Author

- Beqa Kopadze
