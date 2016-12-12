package ru.sbt.CacheUtil;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Acer on 04.12.2016.
 */
public class FileCacheMaster implements CacheMaster {
    private String absolutePath;
    private final String FILE_PATH = "/src/ru/sbt/CacheUtil/CacheFiles";

    public FileCacheMaster() {
        absolutePath = System.getProperty("user.dir") + FILE_PATH;
        File file = new File(absolutePath);
        if (file.exists()) {
            String[] children = file.list();
            for (String child : children) {
                new File(file, child).delete();
            }
            file.delete();///NOT ZIP + SERIALAZIBLE
        }
        file.mkdir();
        absolutePath = absolutePath + "/";
    }

    @Override
    public Object checkCache(List<Object> key, Cache annotation) {
        String file = absolutePath + key.get(0);
        boolean zip = annotation.zip();
        if (!new File(file + (zip ? ".zip" : ".txt")).exists()) return null;
        if (zip) extractFileFromZip(file);
        file = file + ".txt";
        try (InputStream in = new FileInputStream(file)) {
            Object result;
            ObjectInput objectInput;
            while (in.available() != 0) {
                objectInput = new ObjectInputStream(in);
                List<Object> list = (List) objectInput.readObject();
                result = objectInput.readObject();
                if (key.equals(list)) {
                    in.close();
                    new File(file).delete();
                    return result;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found.");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class cast exception.");
        }
        return null;
    }

    private void extractFileFromZip(String file) {
        try {
            ZipFile zipFile = new ZipFile(file + ".zip");
            String name = file.substring(file.lastIndexOf("/") + 1) + ".txt";
            ZipEntry entry = zipFile.getEntry(name);
            try (InputStream in = zipFile.getInputStream(entry);
                 OutputStream out = new FileOutputStream(file + ".txt")) {
                byte[] buffer = new byte[in.available()];
                in.read(buffer);                // считываем содержимое архива в массив byte
                out.write(buffer);                // распоковываем
            }
        } catch (IOException e) {
            throw new RuntimeException("Error in reading zip");
        }
    }

    @Override
    public void updateCache(List<Object> key, Object result, Cache annotation) {
        String file = absolutePath + key.get(0) + ".txt";
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file, true))) {
            ObjectOutput objectOutput = new ObjectOutputStream(out);
            objectOutput.writeObject(key);
            objectOutput.writeObject(result);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found.");
        } catch (NotSerializableException e) {
            new File(file).delete();
            throw new RuntimeException("Returned type of method must be serializable. Method was worked without cache.");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        if (annotation.zip()) createZip(file);
    }

    private void createZip(String file) {
        try (ZipOutputStream zout = new ZipOutputStream(
                new FileOutputStream(file.substring(0, file.length() - 3) + "zip", true));
             FileInputStream fis = new FileInputStream(file)) {
            ZipEntry entry1 = new ZipEntry(file.substring(file.lastIndexOf("/") + 1));
            zout.putNextEntry(entry1);
            byte[] buffer = new byte[fis.available()];// считываем содержимое файла в массив byte
            fis.read(buffer);                         // добавляем содержимое к архиву
            zout.write(buffer);                       // закрываем текущую запись для новой записи
            zout.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException("Error in writing zip.");
        }
        new File(file).delete();
    }
}
