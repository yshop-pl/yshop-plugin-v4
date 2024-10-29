package pl.yshop.plugin.api.request;

import okhttp3.RequestBody;

public interface BaseRequest<T> {
    String path();
    String method();
    RequestBody requestBody();

    Class<T> responseClass();
}
