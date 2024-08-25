package pl.yshop.plugin.shared.request.requests;

import okhttp3.RequestBody;
import pl.yshop.plugin.shared.entities.Command;
import pl.yshop.plugin.shared.request.BaseRequest;

public class CommandsToExecuteRequest implements BaseRequest<Command[]> {
    @Override
    public String path() {
        return "/commands";
    }

    @Override
    public String method() {
        return "GET";
    }

    @Override
    public RequestBody requestBody() {
        return null;
    }

    @Override
    public Class<Command[]> responseClass() {
        return Command[].class;
    }
}
