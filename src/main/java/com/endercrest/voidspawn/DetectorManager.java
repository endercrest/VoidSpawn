package com.endercrest.voidspawn;

import com.endercrest.voidspawn.detectors.NetherDetector;
import com.endercrest.voidspawn.detectors.IDetector;
import com.endercrest.voidspawn.detectors.VoidDetector;

import javax.naming.NameAlreadyBoundException;
import java.util.HashMap;

public class DetectorManager {

    private static DetectorManager instance = new DetectorManager();

    public static DetectorManager getInstance() {
        return instance;
    }

    private IDetector defaultDetector;
    private HashMap<String, IDetector> detectors;

    /**
     * Setup the DetectorManager, should only be called once.
     */
    public void setUp() {
        defaultDetector = new VoidDetector();

        detectors = new HashMap<String, IDetector>() {{
            put("void", defaultDetector);
            put("nether", new NetherDetector());
        }};
    }

    /**
     * Gets a HashMap of the detectors.
     *
     * @return A clone of the detectors HashMap where string is the key of the detector and SubDetector is detector class.
     */
    public HashMap<String, IDetector> getDetectors() {
        return new HashMap<>(detectors);
    }

    /**
     * Add a new detector that can be accessed in-game and be set for worlds.
     *
     * @param name     The name of the detector.
     * @param detector The class definition.
     * @throws NameAlreadyBoundException Thrown when the detector has already been set with that name.
     */
    public void addDetector(String name, IDetector detector) throws NameAlreadyBoundException {
        name = name.toLowerCase();

        if (detectors.containsKey(name))
            throw new NameAlreadyBoundException(String.format("A detector with the name %s has already been set.", name));

        detectors.put(name, detector);
    }

    /**
     * Removes detector, from being selectable and stops world from using this detector.
     *
     * @param name The name of the detector.
     */
    public void removeDetector(String name) {
        detectors.remove(name.toLowerCase());
    }

    /**
     * Get the world detector or returns the default detector (VoidDetector)
     *
     * @param worldName The world name.
     * @return Detector if set or defaults to VoidDetector.
     */
    public IDetector getWorldDetector(String worldName) {
        String detector = ConfigManager.getInstance().getDetector(worldName);
        return getDetector(detector.toLowerCase());
    }

    /**
     * Get the detector by it's name/id. Returns default if does not exist.
     *
     * @param detector The detector name/id.
     * @return Detector or default if not exists.
     */
    public IDetector getDetector(String detector) {
        IDetector sd = detectors.get(detector.toLowerCase());
        return sd == null ? defaultDetector : sd;
    }

    /**
     * Get the default detectors names.
     *
     * @return The default detectors name.
     */
    public String getDefaultDetectorName() {
        return defaultDetector.getName();
    }

    /**
     * Get the default detector's class.
     *
     * @return Detector's class.
     */
    public IDetector getDefaultDetector() {
        return defaultDetector;
    }

    /**
     * Set a new default detector.
     *
     * @param name The name of the detector.
     * @return Returns true if the default detector has successfully been set.
     */
    public boolean setDefaultDetector(String name) {
        IDetector newDetector = getDetector(name);

        if (newDetector != null)
            defaultDetector = newDetector;

        return newDetector != null;
    }
}
