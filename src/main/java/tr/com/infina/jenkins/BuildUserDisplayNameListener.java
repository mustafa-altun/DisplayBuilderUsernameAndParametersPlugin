package tr.com.infina.jenkins;

import hudson.Extension;
import hudson.model.Cause;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Intercepts builds when they start and automatically appends 
 * the triggering user's name to the build display name.
 */
@Extension
public class BuildUserDisplayNameListener extends RunListener<Run<?, ?>> {

    private static final Logger LOGGER = Logger.getLogger(BuildUserDisplayNameListener.class.getName());

    @Override
    public void onStarted(Run<?, ?> r, TaskListener listener) {
        // Fetch the user cause to find out who triggered the build
        Cause.UserIdCause userIdCause = r.getCause(Cause.UserIdCause.class);
        String triggerUser;

        if (userIdCause != null) {
            triggerUser = userIdCause.getUserId();
            // Fallback to username if userId is not present
            if (triggerUser == null || triggerUser.trim().isEmpty()) {
                triggerUser = userIdCause.getUserName();
            }
        } else {
            triggerUser = "Auto";
        }
        
        // Handle case where it's still null somehow
        if (triggerUser == null || triggerUser.trim().isEmpty()) {
            triggerUser = "Auto";
        }

        // Format is: #5616 [mustafa.altun]
        String newDisplayName = "#" + r.getNumber() + " [" + triggerUser + "]";

        try {
            r.setDisplayName(newDisplayName);
            LOGGER.log(Level.INFO, "Updated build display name to {0}", newDisplayName);
        } catch (IOException e) {
            listener.getLogger().println("[BuildUserDisplayPlugin] Error setting build display name: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Failed to set display name for build " + r.getNumber(), e);
        }
    }
}
