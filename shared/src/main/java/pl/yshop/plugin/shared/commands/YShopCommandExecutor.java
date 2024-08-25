package pl.yshop.plugin.shared.commands;

public interface YShopCommandExecutor<SENDER> {
    void executeCommand(SENDER sender, String[] args);
    boolean hasPermission(SENDER sender);
}
