package pl.yshop.plugin.api.request;

public interface Requester {
    <T> T make(BaseRequest<T> request) throws RequestException;
}
