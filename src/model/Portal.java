package model;

import java.util.*;

import static util.Util.*;

/**
 * The Portal class represents a teleportation portal within the game. Each portal has a unique identifier
 * and a corresponding key identifier that allows players to interact with it. The class keeps track of all
 * created portals and provides methods to find related portals for teleportation.
 */
public class Portal extends Entity {

    // A static list to hold all portal instances
    private static final List<Portal> portals = new ArrayList<>();

    // Unique identifiers for each portal
    private final String PORTAL_IDENTIFIER;
    private final String PORTAL_KEY_IDENTIFIER;

    /**
     * Constructs a new Portal object with the specified attributes.
     *
     * @param type                 The type of the entity (e.g., "PORTAL").
     * @param x                    The x-coordinate of the portal.
     * @param y                    The y-coordinate of the portal.
     * @param PORTAL_IDENTIFIER    A unique identifier for the portal group.
     * @param PORTAL_KEY_IDENTIFIER A key that pairs with the portal identifier for linking portals.
     */
    public Portal(String type, int x, int y, String PORTAL_IDENTIFIER, String PORTAL_KEY_IDENTIFIER) {
        super(x, y, type);
        this.PORTAL_IDENTIFIER = PORTAL_IDENTIFIER;
        this.PORTAL_KEY_IDENTIFIER = PORTAL_KEY_IDENTIFIER;
        portals.add(this);
    }

    /**
     * Returns the unique portal identifier.
     *
     * @return The portal identifier.
     */
    public String getPortalIdentifier() {
        return PORTAL_IDENTIFIER;
    }

    /**
     * Returns the portal key identifier.
     *
     * @return The portal key identifier.
     */
    public String getPortalKeyIdentifier() {
        return PORTAL_KEY_IDENTIFIER;
    }

    /**
     * Retrieves a list of all created portals.
     *
     * @return A list of all portals.
     */
    public static List<Portal> getPortals() {
        return Collections.unmodifiableList(portals);
    }

    /**
     * Finds a random portal that matches the current portal's identifiers (excluding itself) for teleportation.
     *
     * @return A random linked portal, or null if no such portal exists.
     */
    public Portal getRandomLinkedPortal() {
        List<Portal> samePortalsToTeleport = new ArrayList<>();
        for (Portal p : portals) {
            if (!p.equals(this)) {
                if (this.getPortalIdentifier().equals(p.getPortalIdentifier()) && this.getPortalKeyIdentifier().equals(p.getPortalKeyIdentifier())) {
                    samePortalsToTeleport.add(p);
                }
            }
        }
        return samePortalsToTeleport.isEmpty() ? null : samePortalsToTeleport.get(rand.nextInt(samePortalsToTeleport.size()));
    }


    /**
     * Checks if this portal is equal to another object.
     *
     * @param o The object to compare with.
     * @return true if the other object is a Portal with the same identifiers and coordinates; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Portal)) return false;
        Portal portal = (Portal) o;
        return Objects.equals(PORTAL_IDENTIFIER, portal.PORTAL_IDENTIFIER) && Objects.equals(PORTAL_KEY_IDENTIFIER, portal.PORTAL_KEY_IDENTIFIER) && getX() == portal.getX() && getY() == portal.getY();
    }

    /**
     * Generates a hash code for this portal.
     *
     * @return The hash code based on the portal's identifiers and coordinates.
     */
    @Override
    public int hashCode() {
        return Objects.hash(PORTAL_IDENTIFIER, PORTAL_KEY_IDENTIFIER);
    }
}
