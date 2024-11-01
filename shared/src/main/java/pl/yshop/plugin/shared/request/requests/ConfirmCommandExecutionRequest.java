package pl.yshop.plugin.shared.request.requests;

import okhttp3.RequestBody;
import pl.yshop.plugin.api.request.BaseRequest;

import static okhttp3.internal.Util.EMPTY_REQUEST;

public class ConfirmCommandExecutionRequest implements BaseRequest<Void> {
    private final String commandId;

    public ConfirmCommandExecutionRequest(String commandId) {
        this.commandId = commandId;
    }

    @Override
    public String path() {
        return "/server/{serverId}/commands/" + this.commandId;
    }

    @Override
    public String method() {
        return "POST";
    }

    @Override
    public RequestBody requestBody() {
        return EMPTY_REQUEST;
    }

    @Override
    public Class<Void> responseClass() {
        return Void.class;
    }
}
