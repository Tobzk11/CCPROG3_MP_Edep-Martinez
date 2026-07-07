import java.util.Scanner;

/**
 * Entry point for the MediaVault console application. Displays a
 * text-based menu and delegates all user actions to UserProfile
 * and Library. All business logic stays in the model classes;
 * this class only handles input and output.
 *
 * @author Group 16
 */
public class MediaVaultApp {

    private static Scanner scanner = new Scanner(System.in);
    private static UserProfile userProfile;

    /**
     * Launches the application, prompts for a username, then runs
     * the main menu loop until the user chooses to exit.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=============================");
        System.out.println("     Welcome to MediaVault   ");
        System.out.println("=============================");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine().trim();
        userProfile = new UserProfile(username);
        System.out.println("Hello, " + username + "! Your library is ready.\n");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            System.out.println();
            switch (choice) {
                case "1": handleAddEntry();          break;
                case "2": handleUpdateStatus();      break;
                case "3": handleRateAndReview();     break;
                case "4": handleDisplayEntries();    break;
                case "5": handleFilter();            break;
                case "6": userProfile.viewSummary(); break;
                case "7":
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1-7.");
            }
            System.out.println();
        }
        scanner.close();
    }

    /**
     * Prints the main menu to the console.
     */
    private static void printMenu() {
        System.out.println("-----------------------------");
        System.out.println("         MAIN MENU           ");
        System.out.println("-----------------------------");
        System.out.println("[1] Add a new entry");
        System.out.println("[2] Update entry status");
        System.out.println("[3] Rate and review an entry");
        System.out.println("[4] Display all entries");
        System.out.println("[5] Filter entries");
        System.out.println("[6] View library summary");
        System.out.println("[7] Exit");
        System.out.print("Choice: ");
    }

    /**
     * Prompts the user to add a new Movie, TVSeries, or VideoGame
     * to their library. The initial status may only be PLANNED or
     * IN_PROGRESS, per the spec requirement.
     */
    private static void handleAddEntry() {
        System.out.println("--- Add New Entry ---");
        System.out.println("[1] Movie");
        System.out.println("[2] TV Series");
        System.out.println("[3] Video Game");
        System.out.print("Media type: ");
        String typeChoice = scanner.nextLine().trim();

        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Genre: ");
        String genre = scanner.nextLine().trim();

        System.out.println("Initial status:");
        System.out.println("[1] Planned");
        System.out.println("[2] In Progress");
        System.out.print("Status: ");
        String statusChoice = scanner.nextLine().trim();

        MediaStatus status;
        boolean validStatus = true;
        if (statusChoice.equals("1"))
            status = MediaStatus.PLANNED;
        else if (statusChoice.equals("2"))
            status = MediaStatus.IN_PROGRESS;
        else {
            System.out.println("Invalid status. Entry not added.");
            status = null;
            validStatus = false;
        }

        if (validStatus) {
            MediaEntry entry = new MediaEntry(title, genre, status);
            Library lib = userProfile.getLibrary();

            switch (typeChoice) {
                case "1":
                    System.out.print("Director: ");
                    String director = scanner.nextLine().trim();
                    System.out.print("Duration (minutes): ");
                    int duration = readInt();
                    System.out.print("Release Year: ");
                    int year = readInt();
                    lib.addMovie(new Movie(entry, director, duration, year));
                    System.out.println("Movie \"" + title + "\" added.");
                    break;
                case "2":
                    System.out.print("Total Episodes: ");
                    int totalEps = readInt();
                    System.out.print("Watched Episodes: ");
                    int watchedEps = readInt();
                    System.out.print("Season Count: ");
                    int seasons = readInt();
                    lib.addTVSeries(new TVSeries(entry, totalEps, watchedEps, seasons));
                    System.out.println("TV Series \"" + title + "\" added.");
                    break;
                case "3":
                    System.out.print("Platform: ");
                    String platform = scanner.nextLine().trim();
                    System.out.print("Required Specs: ");
                    String specs = scanner.nextLine().trim();
                    System.out.print("Developer: ");
                    String developer = scanner.nextLine().trim();
                    System.out.print("Hours Played: ");
                    double hours = readDouble();
                    lib.addVideoGame(new VideoGame(entry, platform, specs, developer, hours));
                    System.out.println("Video Game \"" + title + "\" added.");
                    break;
                default:
                    System.out.println("Invalid media type. Entry not added.");
            }
        }
    }

    /**
     * Prompts the user to update the status of an existing entry
     * by title and media type.
     */
    private static void handleUpdateStatus() {
        System.out.println("--- Update Entry Status ---");
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.println("Media type: [1] Movie  [2] TV Series  [3] Video Game");
        System.out.print("Choice: ");
        String typeChoice = scanner.nextLine().trim();

        MediaEntry entry = findEntryByTypeChoice(title, typeChoice);
        boolean found = entry != null;

        if (!found) {
            System.out.println("Entry not found.");
        } else {
            System.out.println("New status: [1] Planned  [2] In Progress  [3] Completed");
            System.out.print("Choice: ");
            String statusChoice = scanner.nextLine().trim();

            MediaStatus newStatus;
            boolean validStatus = true;
            switch (statusChoice) {
                case "1": newStatus = MediaStatus.PLANNED;     break;
                case "2": newStatus = MediaStatus.IN_PROGRESS; break;
                case "3": newStatus = MediaStatus.COMPLETED;   break;
                default:
                    System.out.println("Invalid status.");
                    newStatus = null;
                    validStatus = false;
            }

            if (validStatus) {
                entry.updateStatus(newStatus);
                System.out.println("Status updated to " + newStatus + ".");
            }
        }
    }

    /**
     * Prompts the user to assign a rating (1-10) and a short review
     * to a Completed entry. Rejects the action if the entry is not
     * yet marked Completed or if the rating is outside 1-10.
     */
    private static void handleRateAndReview() {
        System.out.println("--- Rate and Review ---");
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.println("Media type: [1] Movie  [2] TV Series  [3] Video Game");
        System.out.print("Choice: ");
        String typeChoice = scanner.nextLine().trim();

        MediaEntry entry = findEntryByTypeChoice(title, typeChoice);

        if (entry == null) {
            System.out.println("Entry not found.");
        } else if (!entry.isCompleted()) {
            System.out.println("Cannot rate \"" + entry.getTitle()
                    + "\": status is " + entry.getStatus()
                    + ". Mark it as Completed first.");
        } else {
            System.out.print("Rating (1-10): ");
            int rating = readRating();

            if (rating == -1) {
                System.out.println("Invalid rating. Must be between 1 and 10.");
            } else {
                System.out.print("Review: ");
                String review = scanner.nextLine().trim();
                boolean success = entry.setRatingAndReview(rating, review);
                if (success)
                    System.out.println("Rating and review saved.");
            }
        }
    }

    /**
     * Displays all entries in the library to the console.
     */
    private static void handleDisplayEntries() {
        System.out.println("--- All Entries ---");
        userProfile.getLibrary().displayAllEntries();
    }

    /**
     * Prompts the user to filter entries by status or by media type,
     * then prints the matching entries.
     */
    private static void handleFilter() {
        System.out.println("--- Filter Entries ---");
        System.out.println("[1] Filter by Status");
        System.out.println("[2] Filter by Media Type");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        Library lib = userProfile.getLibrary();

        if (choice.equals("1")) {
            System.out.println("[1] Planned  [2] In Progress  [3] Completed");
            System.out.print("Status: ");
            String s = scanner.nextLine().trim();
            MediaStatus status;
            boolean validStatus = true;
            switch (s) {
                case "1": status = MediaStatus.PLANNED;     break;
                case "2": status = MediaStatus.IN_PROGRESS; break;
                case "3": status = MediaStatus.COMPLETED;   break;
                default:
                    System.out.println("Invalid status.");
                    status = null;
                    validStatus = false;
            }
            if (validStatus)
                lib.filterByStatus(status);

        } else if (choice.equals("2")) {
            System.out.println("[1] Movie  [2] TV Series  [3] Video Game");
            System.out.print("Type: ");
            String t = scanner.nextLine().trim();
            switch (t) {
                case "1": lib.filterByType("Movie");     break;
                case "2": lib.filterByType("TVSeries");  break;
                case "3": lib.filterByType("VideoGame"); break;
                default: System.out.println("Invalid type.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    /**
     * Looks up a MediaEntry by title and media type choice string
     * ("1" = Movie, "2" = TVSeries, "3" = VideoGame).
     *
     * @param title      the title to search for
     * @param typeChoice the menu choice string identifying media type
     * @return the matching MediaEntry, or null if not found
     */
    private static MediaEntry findEntryByTypeChoice(String title, String typeChoice) {
        Library lib = userProfile.getLibrary();
        switch (typeChoice) {
            case "1":
                Movie m = lib.findMovie(title);
                return m != null ? m.getEntry() : null;
            case "2":
                TVSeries t = lib.findTVSeries(title);
                return t != null ? t.getEntry() : null;
            case "3":
                VideoGame g = lib.findVideoGame(title);
                return g != null ? g.getEntry() : null;
            default:
                return null;
        }
    }

    /**
     * Reads a rating integer from the console. Returns -1 if the
     * value entered is not a number or is outside the range 1-10.
     *
     * @return the rating entered (1-10), or -1 if invalid
     */
    private static int readRating() {
        int rating;
        try {
            rating = Integer.parseInt(scanner.nextLine().trim());
            if (rating < 1 || rating > 10)
                rating = -1;
        } catch (NumberFormatException e) {
            rating = -1;
        }
        return rating;
    }

    /**
     * Reads an integer from the console, re-prompting if the input
     * is not a valid integer.
     *
     * @return the integer entered by the user
     */
    private static int readInt() {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    /**
     * Reads a double from the console, re-prompting if the input
     * is not a valid decimal number.
     *
     * @return the double entered by the user
     */
    private static double readDouble() {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}