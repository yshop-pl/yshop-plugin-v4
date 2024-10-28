package pl.yshop.plugin.shared;

import com.google.gson.Gson;
import pl.yshop.plugin.api.Configuration;
import pl.yshop.plugin.api.Extension;
import pl.yshop.plugin.api.PlatformLogger;
import pl.yshop.plugin.shared.entities.ExtensionConfig;
import pl.yshop.plugin.api.Platform;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExtensionsLoader {
    private final File extensionDir;
    private final PlatformLogger logger;
    private final Platform platform;
    private final Configuration configuration;
    public final Set<Extension> extensions = new HashSet<>();
    private final Set<Class<?>> loadedClasses = new HashSet<>();

    public ExtensionsLoader(File dataFolder, Platform platform, PlatformLogger logger, Bootstrap bootstrap) {
        this.logger = logger;
        this.platform = platform;
        this.extensionDir = new File(dataFolder.getPath(), "extensions");
        if (this.extensionDir.mkdirs()) {
            this.logger.info("Created extensions directory!");
        }
        this.configuration = platform.getConfiguration();
    }

    public void load() {
        if(this.extensionDir.exists() && this.extensionDir.isDirectory()) {
            File[] files = this.extensionDir.listFiles((dir, name) -> name.endsWith(".jar"));
            if (files == null) return;
            String mainClass = null;
            for (File file : files) {
                try {
                    ZipFile zipFile = new ZipFile(file);
                    ZipEntry entry = zipFile.getEntry("extension.json");
                    if (entry == null) {
                        this.logger.error("Extension file " + file.getName() + " doesn't have extension.json!");
                        continue;
                    }

                    InputStream inputStream = zipFile.getInputStream(entry);

                    ExtensionConfig config = new Gson().fromJson(new InputStreamReader(inputStream), ExtensionConfig.class);

                    if (config.getMainClass() == null) throw new ClassNotFoundException();
                    mainClass = config.getMainClass();

                    if (!config.getSupportedPlatforms().contains(this.platform.engine())) continue;

                    ClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
                    Class<?> clazz = loader.loadClass(mainClass);
                    this.loadedClasses.add(clazz);
                } catch (IOException e) {
                    this.logger.error("Invalid extension " + file.getName());
                    if (this.configuration.debug()) e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    this.logger.error("Class not found! Wrong main defined in extension.json?: " + file.getName() + " class: " + mainClass);
                    if (this.configuration.debug()) e.printStackTrace();
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
                    extension.init(this.logger, this.platform);
                    extension.onEnable();
                    this.extensions.add(extension);
                    this.logger.info(String.format("Extension %s successfully enabled!", extension.getExtensionName()));
                }
            } catch (InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
                this.logger.error("Can't enable extension " + clazz.getName());
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
}
