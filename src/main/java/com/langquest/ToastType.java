package com.langquest;

public enum ToastType {
    XP("⭐", "toast-xp"),
    BEST("🏆", "toast-best"),
    WELCOME("👋", "toast-welcome"),
    CREATED("🎉", "toast-created");

    private final String emoji;
    private final String styleClass;

    ToastType(String emoji, String styleClass) {
        this.emoji = emoji;
        this.styleClass = styleClass;
    }

    public String emoji() { return emoji; }
    public String styleClass() { return styleClass; }
}