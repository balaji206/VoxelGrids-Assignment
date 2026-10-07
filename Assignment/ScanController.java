import java.util.*;

public class ScanController {

    private final List<Scan> scans = new ArrayList<>();

    private Thread worker;
    private volatile boolean stopRequested = false;
    private volatile boolean paused = false;

    public synchronized void handleCommand(String command) {

        if(command == null || command.trim().isEmpty()){
            System.out.print("Invalid command");
            return;
        }

        command = command.trim();

        try {

            if (command.equalsIgnoreCase("view")) {
                view();
            }

            else if (command.equalsIgnoreCase("start")) {
                start();
            }

            else if (command.equalsIgnoreCase("stop")) {
                stop();
            }

            else if (command.equalsIgnoreCase("exit")) {
                exit();
            }

            else if (command.toLowerCase().startsWith("add:")) {
                add(command);
            }

            else if (command.toLowerCase().startsWith("remove:")) {
                remove(command);
            }

            else {
                System.out.println("Invalid command");
            }

        } catch (Exception e) {
            System.out.print("Error processing command: "+e.getMessage());
        }
    }

    private void add(String command) {

        String data = command.substring(4);
        String[] parts = data.split(",");

        if (parts.length != 4) {
            System.out.println("Invalid add command");
            return;
        }

        int id = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();
        int duration = Integer.parseInt(parts[2].trim());

        String pauseValue = parts[3].trim();

        boolean pause;

        if (pauseValue.equalsIgnoreCase("yes")) {
            pause = true;
        } else if (pauseValue.equalsIgnoreCase("no")) {
            pause = false;
        } else {
            System.out.println("Pause must be Yes or No, invalid pause command");
            return;
        }

        synchronized (scans) {

            for (Scan scan : scans) {
                if (scan.getId() == id) {
                    System.out.println("Scan ID already exists, please use a unique ID");
                    return;
                }
            }

            scans.add(new Scan(id, name, duration, pause));
        }

        System.out.println("Scan added");
    }

    private void view() {

        synchronized (scans) {

            if (scans.isEmpty()) {
                System.out.println("Queue is empty");
                return;
            }

            System.out.println("Current Queue -->");

            for (Scan scan : scans) {
                System.out.println(scan);
            }
        }
    }

    private void start() {

        if (paused) {
            paused = false;
            System.out.println("Resuming queue...");
            return;
        }

        if (worker != null && worker.isAlive()) {
            System.out.println("Scan already running");
            return;
        }

        stopRequested = false;

        worker = new Thread(() -> processScans());
        worker.start();
    }

    private void processScans() {

        while (true) {

            Scan current = null;

            synchronized (scans) {

                for (Scan scan : scans) {

                    if (scan.getState() == Scan.State.IDLE) {
                        current = scan;
                        break;
                    }
                }
            }

            if (current == null) {
                System.out.println("No pending scans available");
                return;
            }

            current.setState(Scan.State.RUNNING);

            System.out.println("Starting " + current.getName());

            try {

                for (int i = 0; i < current.getDuration(); i++) {

                    Thread.sleep(1000);

                    if (stopRequested) {
                        current.setState(Scan.State.CANCELLED);
                        System.out.println("Cancelled " + current.getName());

                        stopRequested = false;
                        break;
                    }
                }

                if (current.getState() == Scan.State.RUNNING) {

                    current.setState(Scan.State.COMPLETE);

                    System.out.println("Completed " + current.getName());

                    if (current.isPause()) {
                        paused = true;
                        System.out.println("Queue paused. Type start to continue.");
                        return;
                    }
                }

            } catch (InterruptedException e) {

                current.setState(Scan.State.CANCELLED);
                System.out.println("Cancelled " + current.getName());
                return;
            }
        }
    }

    private void stop() {

        if (worker == null || !worker.isAlive()) {
            System.out.println("No scan is running");
            return;
        }

        stopRequested = true;
    }

    private void remove(String command) {

        try {

            int id = Integer.parseInt(command.substring(7).trim());

            synchronized (scans) {

                Iterator<Scan> iterator = scans.iterator();

                while (iterator.hasNext()) {

                    Scan scan = iterator.next();

                    if (scan.getId() == id) {

                        if (scan.getState() != Scan.State.IDLE) {
                            System.out.println("Cannot remove a started scan");
                            return;
                        }

                        iterator.remove();
                        System.out.println("Scan removed");
                        return;
                    }
                }
            }

            System.out.println("Scan not found");

        } catch (NumberFormatException e) {
            System.out.println("Invalid scan ID");
        }
    }

    private void exit() {

        stopRequested = true;

        if (worker != null && worker.isAlive()) {
            worker.interrupt();
        }

        System.out.println("Exiting application...");
        System.exit(0);
    }
}