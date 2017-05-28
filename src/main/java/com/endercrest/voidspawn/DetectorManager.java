package com.endercrest.voidspawn;

import com.endercrest.voidspawn.detectors.NetherDetector;
import com.endercrest.voidspawn.detectors.SubDetector;
import com.endercrest.voidspawn.detectors.VoidDetector;

import java.util.HashMap;

public class DetectorManager {

    private static DetectorManager instance = new DetectorManager();

    public static DetectorManager getInstance(){
        return instance;
    }

    private SubDetector defaultDetector;
    private HashMap<String, SubDetector> detectors;

    /**
     * Setup the DetectorManager, should only be called once.
     */
    public void setUp(){
        defaultDetector = new VoidDetector();

        detectors = new HashMap<String, SubDetector>() {{
            put("void", defaultDetector);
            put("nether", new NetherDetector());
        }};
    }

    /**
     * Gets a HashMap of the detectors.
     * @return A clone of the detectors HashMap where string is the key of the detector and SubDetector is detector class.
     */
    public HashMap<String, SubDetector> getDetectors(){
        return new HashMap<>(detectors);
    }

    /**
     * Get the world detector or returns the default detector (VoidDetector)
     * @param worldName The world name.
     * @return Detector if set or defaults to VoidDetector.
     */
    public SubDetector getWorldDetector(String worldName){
        String detector = ConfigManager.getInstance().getDetector(worldName);
        return getDetector(detector);
    }

    /**
     * Get the detector by it's name/id. Returns default if does not exist.
     * @param detector The detector name/id.
     * @return Detector or default if not exists.
     */
    public SubDetector getDetector(String detector){
        SubDetector sd = detectors.get(detector);
        return sd == null ? defaultDetector : sd;
    }

    /**
     * Get the default detectors names.
     * @return The default detectors name.
     */
    public String getDefaultDetectorName(){
        return defaultDetector.getName();
    }

    /**
     * Get the default detector's class.
     * @return Detector's class.
     */
    public SubDetector getDefaultDetector(){
        return defaultDetector;
    }
}
