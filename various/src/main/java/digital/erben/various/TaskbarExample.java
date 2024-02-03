package digital.erben.various;

import javax.swing.*;
import java.awt.*;
import java.awt.Taskbar.Feature;
import java.net.*;
import java.util.Arrays;
import java.util.function.Consumer;

public class TaskbarExample {
    public static void main(final String[] args) throws Exception {
        if (Taskbar.isTaskbarSupported()) {
            final Taskbar taskbar = Taskbar.getTaskbar();
            showProvidedFeatures(taskbar);
            performSomeTaskbarChanges(taskbar);
        } else {
            System.err.println("Taskbar is not supported on your platform.");
        }
    }

    private static void showProvidedFeatures(final Taskbar taskbar) {
        System.out.println("Taskbar is supported and provides the " +
            "following features:");

        Arrays.stream(Feature.values()).forEach(feature ->
            System.out.printf("Feature %s " + "is supported: %s%n", feature,
                taskbar.isSupported(feature)));
    }

    private static void performSomeTaskbarChanges(final Taskbar taskbar)
        throws InterruptedException {
        setIcon(taskbar);
        setBadge(taskbar, "1");
        requestUserAttention(taskbar, false); // springt einmal

        setBadge(taskbar, "2");
        setBadge(taskbar, "progress");

        requestUserAttention(taskbar, true); // springt fortwährend

        performProgressInteraction(taskbar);
    }

    private static void setIcon(Taskbar taskbar) {
        if (taskbar.isSupported(Feature.ICON_IMAGE)) {
            try {
                URL imageUrl = URL.of(
                    URI.create("https://cdn-icons-png.flaticon.com/512/5360/5360938.png"), null);
                taskbar.setIconImage(new ImageIcon(imageUrl).getImage());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static void setBadge(final Taskbar taskbar,
                                 final String text) throws InterruptedException {
        if (taskbar.isSupported(Feature.ICON_BADGE_TEXT)) {
            taskbar.setIconBadge(text);
            Thread.sleep(1000);
        }
    }

    private static void requestUserAttention(Taskbar taskbar, boolean critical) throws InterruptedException {
        if (taskbar.isSupported(Feature.USER_ATTENTION)) {
            taskbar.requestUserAttention(true, critical);
            Thread.sleep(2500);
        }
    }

    private static void performProgressInteraction(final Taskbar taskbar)
        throws InterruptedException {
        if (taskbar.isSupported(Feature.PROGRESS_VALUE)) {
            for (int i = 0; i < 100; i++) {
                taskbar.setProgressValue(i);
                Thread.sleep(100);
            }
        }
    }

}
