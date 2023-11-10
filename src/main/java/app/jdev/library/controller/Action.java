package app.jdev.library.controller;

public enum Action {
    HOME("Home"),
    AUTHORS("Authors"),
    PUBLISHERS("Publishers"),
    BOOKS("Books");

    private final String action;

    private Action(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
