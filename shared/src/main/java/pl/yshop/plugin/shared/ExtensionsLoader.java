package pl.yshop.plugin.shared;

import org.yaml.snakeyaml.Yaml;
import pl.yshop.plugin.api.Extension;
import pl.yshop.plugin.shared.configuration.PluginConfiguration;
import pl.yshop.plugin.shared.logger.YShopLogger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipFile;

public class ExtensionsLoader {
    private final File extensionDir;
    private final YShopLogger logger;
    private final PluginConfiguration configuration;
    private final Set<Extension> extensions = new HashSet<>();
    private final Set<Class<?>> loadedClasses = new HashSet<>();

    public ExtensionsLoader(File dataFolder, YShopLogger logger, PluginConfiguration configuration) {
        this.extensionDir = new File(dataFolder.getPath(), "extensions");
        this.extensionDir.mkdirs();
        this.logger = logger;
        this.configuration = configuration;
    }

    public void load() {
        if(this.extensionDir.exists() && this.extensionDir.isDirectory()) {
            File[] files = this.extensionDir.listFiles((dir, name) -> name.endsWith(".jar"));
            if (files == null) return;
            String mainClass = null;
            for (File file : files) {
                try {
                    ZipFile zipFile = new ZipFile(file);
                    InputStream inputStream = zipFile.getInputStream(zipFile.getEntry("extension.yml"));

                    Yaml yaml = new Yaml();
                    Map<String, Object> data = yaml.load(new InputStreamReader(inputStream));
                    if (!data.containsKey("main_class")) throw new ClassNotFoundException();
                    mainClass = data.get("main_class").toString();

                    ClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
                    Class<?> clazz = loader.loadClass(mainClass);
                    this.loadedClasses.add(clazz);
                } catch (IOException e) {
                    this.logger.error("Invalid extension " + file.getName());
                    if (this.configuration.debug) e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    this.logger.error("Class not found! Wrong main defined in extension.yml?: " + file.getName() + " class: " + mainClass);
                    if (this.configuration.debug) e.printStackTrace();
                }
            }
        }
        this.logger.info(String.format("Successfully loaded %s extensions!", this.loadedClasses.size()));
    }

    public void enable() {
        for (Class<?> clazz : this.loadedClasses) {
            try {
                Object object = clazz.getDeclaredConstructor().newInstance();
                if (object instanceof Extension extension) {
                    extension.onEnable();
                    this.extensions.add(extension);
                    this.logger.info(String.format("Extension %s successfully enabled!", extension.getExtensionName()));
                }
            } catch (InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        this.logger.info(String.format("Successfully enabled %s extensions!", this.extensions.size()));
    }
    public void disable() {
        this.extensions.forEach(Extension::onDisable);
        this.logger.info(String.format("Successfully disabled %s extensions!", this.extensions.size()));
        this.extensions.clear();
        this.loadedClasses.clear();
    }

    public Set<Extension> getExtensions() {
        return this.extensions;
    }
}
