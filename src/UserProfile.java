public class UserProfile {
    private String username;
    private Library library;

    public UserProfile(String username) {
        this.username = username;
        library = new Library();
    }

    public Library getLibrary() {
        return this.library;
    }

    public void viewSummary() {
        System.out.printf("Hello, %s!\n", this.username);
        System.out.println(this.library.getSummary());
    }
}
