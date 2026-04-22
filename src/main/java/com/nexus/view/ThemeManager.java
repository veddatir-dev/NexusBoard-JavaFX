package com.nexus.view;

/**
 * Manages application themes and provides theme switching functionality.
 */
public class ThemeManager {
    
    public static final String LIGHT_THEME = "light";
    public static final String DARK_THEME = "dark";
    public static final String PROFESSIONAL_THEME = "professional";
    public static final String MODERN_THEME = "modern";
    
    private static String currentTheme = PROFESSIONAL_THEME;

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
        String cssFile = "/css/style.css";
        
        switch (theme.toLowerCase()) {
            case LIGHT_THEME:
                cssFile = "/css/theme-light.css";
                break;
            case DARK_THEME:
                cssFile = "/css/theme-dark.css";
                break;
            case PROFESSIONAL_THEME:
                cssFile = "/css/theme-professional.css";
                break;
            case MODERN_THEME:
                cssFile = "/css/theme-modern.css";
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
        return new String[]{LIGHT_THEME, DARK_THEME, PROFESSIONAL_THEME, MODERN_THEME};
    }
}
