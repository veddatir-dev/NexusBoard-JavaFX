package com.nexus.view;

/**
 * Manages application themes and provides theme switching functionality.
 */
public class ThemeManager {
    
    public static final String PROFESSIONAL_DARK = "Professional Dark";
    public static final String LIGHT_MODERN = "Light Modern";
    
    private static String currentTheme = PROFESSIONAL_DARK;

    /**
     * Get the CSS URL for the current theme
     */
    public static String getThemeCSS() {
        return getThemeCSS(currentTheme);
    }

    /**
     * Get the CSS URL for a specific theme
     */
    public static String getThemeCSS(String theme) {
        String cssFile = "/css/professional-dark.css";
        
        switch (theme) {
            case LIGHT_MODERN:
                cssFile = "/css/light-modern.css";
                break;
            case PROFESSIONAL_DARK:
            default:
                cssFile = "/css/professional-dark.css";
                break;
        }
        
        return ThemeManager.class.getResource(cssFile).toExternalForm();
    }

    /**
     * Set the current theme
     */
    public static void setCurrentTheme(String theme) {
        currentTheme = theme;
    }

    /**
     * Get the current theme name
     */
    public static String getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Get all available themes
     */
    public static String[] getAvailableThemes() {
        return new String[]{PROFESSIONAL_DARK, LIGHT_MODERN};
    }
}
