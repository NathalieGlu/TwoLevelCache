package com.cache;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileSystemCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {
    private Map<K, V> storage;
    private int size;
    private String path;

    FileSystemCache(int size) {
        this.storage = new HashMap<K, V>(size);
        this.size = size;
        this.path = "./cache";
    }

    @Override
    public void put(K key, V value) {
        try {
            String filename = key.toString();
            File file = new File(path + filename);

            FileOutputStream fos = new FileOutputStream(path + "/" + file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(value);
            oos.flush();
            storage.put(key, value);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public V get(K key) {
        if (storage.containsKey(key)) {
            V filename = storage.get(key);
            String filePath = path + "/" + filename.toString();
            try (FileInputStream fis = new FileInputStream(filePath);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                return (V) storage.get(ois.readObject());
            } catch (Exception e) {
                System.out.println(e);
            }

        } else {
            System.out.println("key not found");
        }
        return null;
    }

    @Override
    public void remove(K key) {
        V filename = storage.get(key);
        File file = new File(path + "/" + filename.toString());

        try {
            file.delete();
        } catch (Exception e) {
            System.out.println(e);
        }
        storage.remove(key);
    }

    @Override
    public void clear() {
        try {
            File path = new File(this.path);
            File[] files = path.listFiles();

            for (File file : files) {
                file.delete();
            }

            storage.clear();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public boolean containsKey(K key) {
        return storage.containsKey(key);
    }

    @Override
    public boolean isFull() {
        return storage.size() == size;
    }
}
