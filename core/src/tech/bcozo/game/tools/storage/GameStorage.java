/**
 * 
 */
package tech.bcozo.game.tools.storage;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Disposable;

/**
 * <p>
 * Javadoc description
 * </p>
 * 
 * @ClassName: GameStorage
 * @author Jayden Liang
 * @version 1.0
 * @date Dec 21, 2015 1:27:25 AM
 */
public class GameStorage implements Disposable {
    private static ArrayList<GameStorage> instances;
    private String name;
    private ArrayList<String[]> storageData;

    /**
     * <p>
     * This is the constructor of GameStorage
     * </p>
     */
    public GameStorage(String name) {
        this.name = name;
        storageData = new ArrayList<String[]>();
    }

    public static GameStorage getInstanceOf(String name) {
        if (instances == null)
            instances = new ArrayList<GameStorage>();
        for (GameStorage gameStorage : instances) {
            if (gameStorage.getName().equals(name)) {
                return gameStorage;
            }
        }
        GameStorage instance = new GameStorage(name);
        instances.add(instance);
        return instance;
    }

    public static void clear() {
        for (GameStorage gameStorage : instances) {
            gameStorage.dispose();
        }
        instances = null;
    }

    public void clearData() {
        storageData.removeAll(storageData);
    }

    public String getName() {
        return name;
    }

    public void setValue(String key, String val) {
        for (String[] data : storageData) {
            if (data[0].equals(key)) {
                data[1] = val;
                return;
            }
        }
        storageData.add(new String[] { key, val });
    }

    public String getValue(String key) {
        for (String[] data : storageData) {
            if (data[0].equals(key)) {
                return data[1];
            }
        }
        return "";
    }

    @Override
    public void dispose() {
        clearData();
        storageData = null;
        instances.remove(this);
    }
}
