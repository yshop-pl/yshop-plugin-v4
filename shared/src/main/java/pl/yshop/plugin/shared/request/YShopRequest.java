package pl.yshop.plugin.shared.request;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.yshop.plugin.api.Configuration;
import pl.yshop.plugin.api.PlatformLogger;
import pl.yshop.plugin.api.Platform;

import java.io.IOException;

public class YShopRequest {
    private final Gson gson = new Gson();
    private final Configuration configuration;
    private final Platform platform;
    public final PlatformLogger logger;
    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    public YShopRequest(Configuration config, Platform platform, PlatformLogger logger) {
        this.configuration = config;
        this.platform = platform;
        this.logger = logger;
    }

    public void shutdown() {
        this.httpClient.dispatcher().executorService().shutdown();
    }

    public <T> T make(BaseRequest<T> baseRequest) throws RequestException {
        String requestPath = String.format("%s/public/license%s",
                this.configuration.apiUrl(),
                baseRequest.path()
                        .replace("{serverId}", this.configuration.serverId())
        );
        Request request = new Request.Builder()
                .url(requestPath)
                .header("User-Agent", "yShopPlugin/4.1.0")
                .header("x-api-key", this.configuration.apiKey())
//                .header("x-api-server-key", this.configuration.serverKey())
                .header("x-app-platform", "platform/minecraft-java")
                .header("x-app-platform-version", this.platform.version())
                .header("x-app-platform-engine", this.platform.engine())
                .method(baseRequest.method(), baseRequest.requestBody())
                .build();

        if (this.configuration.debug()) {
            this.logger.debug(String.format("Sending %s request to url: %s", baseRequest.method(), requestPath));
            this.logger.debug(String.format("With API key: %s", this.configuration.apiKey()));
            this.logger.debug(String.format("With API server key: %s", this.configuration.serverKey()));
        }

        try(final Response response = this.httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : null;
            if (this.configuration.debug()) {
                this.logger.debug(String.format("Response (with code %s) form request: %s", response.code(), responseBody));
            }
            if (!response.isSuccessful()) {
                throw new RequestException("Otrzymano kod HTTP " + response.code() + " " + response.message());
            }

            try {
                return this.gson.fromJson(responseBody, baseRequest.responseClass());
            } catch (JsonSyntaxException e) {
                throw new RequestException(e.getMessage());
            }
        } catch (final IOException exception) {
            throw new RequestException(exception.getMessage());
        }
    }
}
