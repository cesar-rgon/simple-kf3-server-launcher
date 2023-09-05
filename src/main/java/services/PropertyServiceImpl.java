package services;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Properties;

public class PropertyServiceImpl implements PropertyService {

    @Override
    public String getProperty(String propFileRelativePath, String propKey) throws Exception {
        Properties prop = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("./" + propFileRelativePath);
        } catch (FileNotFoundException e) {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileRelativePath);
        }
        Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        prop.load(reader);
        inputStream.close();
        return prop.getProperty(propKey);
    }

    @Override
    public void setProperty(String propFileRelativePath, String propKey, String propValue) throws Exception {
        if (StringUtils.isBlank(propValue)) {
            removeProperty(propFileRelativePath, propKey);
            return;
        }
        Properties prop = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("./" + propFileRelativePath);
        } catch (FileNotFoundException e) {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileRelativePath);
        }
        Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        prop.load(reader);
        prop.setProperty(propKey, propValue);

        File propFile = new File("./" + propFileRelativePath);
        if (!propFile.exists()) {
            propFile = new File(getClass().getResource("/" + propFileRelativePath).toURI());
        }
        OutputStream outputStream = new FileOutputStream(propFile);
        prop.store(outputStream, null);
        inputStream.close();
        outputStream.close();
    }

    @Override
    public void removeProperty(String propFileRelativePath, String propKey) throws Exception {
        Properties prop = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("./" + propFileRelativePath);
        } catch (FileNotFoundException e) {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileRelativePath);
        }
        prop.load(inputStream);
        prop.remove(propKey);

        File propFile = new File("./" + propFileRelativePath);
        if (!propFile.exists()) {
            propFile = new File(getClass().getResource("/" + propFileRelativePath).toURI());
        }
        OutputStream outputStream = new FileOutputStream(propFile);
        prop.store(outputStream, null);
        inputStream.close();
        outputStream.close();
    }
}
