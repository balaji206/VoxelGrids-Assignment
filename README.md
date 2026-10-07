# Scan Queue Console Application

A Java console application that manages and executes scan tasks in the background while continuing to accept commands.

## Features

- Add scans to the queue
- View all scans and their current state
- Start scans one at a time in queue order
- Run scans for the specified duration in seconds
- Continue accepting commands while a scan is running
- Pause the queue when a scan is configured with `Yes` for pause
- Stop/cancel the currently running scan
- Remove scans that have not started
- Exit the application
- Handles invalid commands and invalid input without terminating unexpectedly

## Scan States

Each scan can have one of these states:

- `IDLE`
- `RUNNING`
- `COMPLETE`
- `CANCELLED`

## Command Protocol

### Add a scan

```text
add:<id>, <name>, <duration>, <pause>
```

Example:

```text
add:1, Scan 1, 5, Yes
```

### View scans

```text
view
```

### Start scanning

```text
start
```

### Stop the current scan

```text
stop
```

### Remove a scan

```text
remove:<id>
```

Example:

```text
remove:3
```

### Exit

```text
exit
```

## Requirements

- Java JDK 27 or compatible JDK
- Command-line terminal

## Compile

Open a terminal in the project directory and run:

```powershell
javac *.java
```

## Run

```powershell
java Main
```

## Run Using the JAR

A runnable JAR is also available as:

```text
ScanApplication.jar
```

Run it with:

```powershell
java -jar ScanApplication.jar
```

## Example

Input:

```text
add:1, Scan A, 5, Yes
add:2, Scan B, 3, No
view
start
```

Example output:

```text
Scan added
Scan added
Current Queue -->
Scan:1, Scan A, 5, Yes, IDLE
Scan:2, Scan B, 3, No, IDLE

Starting Scan A
```

After the specified duration:

```text
Completed Scan A
Queue paused. Type start to continue.
```

The application uses background execution so that commands can still be entered while a scan is running.

## Project Structure

```text
Scan-Application/
├── Main.java
├── Scan.java
├── ScanController.java
├── ScanApplication.jar
└── README.md
```

## Author

Balaji S
